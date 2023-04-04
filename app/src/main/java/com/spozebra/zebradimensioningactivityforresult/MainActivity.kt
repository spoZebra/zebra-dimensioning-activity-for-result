package com.spozebra.zebradimensioningactivityforresult

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.spozebra.zebradimensioningactivityforresult.emdk.EmdkEngine
import com.spozebra.zebradimensioningactivityforresult.emdk.IEmdkEngineListener
import com.spozebra.zebradimensioningactivityforresult.ssm.ConfigurationManager
import com.symbol.emdk.EMDKResults
import java.math.BigDecimal


class MainActivity : AppCompatActivity(), IEmdkEngineListener {

    private val TAG: String = MainActivity::class.java.name

    private var emdkEngine: EmdkEngine? = null
    lateinit var configurationManager: ConfigurationManager

    private var token: String = "";

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar);
        progressBar.visibility = View.GONE;

        configurationManager = ConfigurationManager(applicationContext)
        // Init Emdk (static, initialized once).
        this.emdkEngine = EmdkEngine.getInstance(applicationContext, this);
    }

    override fun emdkInitialized() {
        // Check if the access was granted already for this app
        // Configuration Manager uses SSM (Zebra Secure Storage Manager) which is in charge of saving/retrieving app params
        var isServiceAccessGranted = configurationManager.getValue(Constants.IS_SERVICE_ACCESS_GRANTED, "false");

        if(isServiceAccessGranted == "false"){
            // If not, download the profile which activates the access thru MX
            val result = emdkEngine!!.setProfile(Constants.SERVICE_ACCESS_PROFILE, null)
            if(result!!.extendedStatusCode == EMDKResults.EXTENDED_STATUS_CODE.NONE) {
                // Access granted, update the permissionsGranted flag
                configurationManager.updateValue(Constants.IS_SERVICE_ACCESS_GRANTED, "true")
                isServiceAccessGranted = "true"
            }
            else{
                Log.e(TAG, "Error Granting permissions thru MX")
                Toast.makeText(this, "Error Granting permissions thru MX", Toast.LENGTH_SHORT).show()
            }
        }

        if(isServiceAccessGranted == "true") {
            // Get token to be used for dimensioning API
            token = acquireToken(Constants.SERVICE_IDENTIFIER);
            // Start dimensioning
            startDimensioning()
        }
    }

    private fun acquireToken(delegation_scope: String): String {
        var token = ""
        try {
            // Retrieve token from ZDM (Zebra Device Manager) using delegation scopes
            val cursor: Cursor? = applicationContext.contentResolver.query(
                Uri.parse(Constants.REQUEST_TOKEN_URI), null as Array<String?>?, "delegation_scope=?", arrayOf(delegation_scope), null as String?
            )
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val colIndex = cursor.getColumnIndex(Constants.COLUMN_QUERY_RESULT)
                token = cursor.getString(colIndex)
                cursor.close()
            }
        } catch (var3: Exception) {
            if (var3 is SecurityException) {
                Log.e(TAG, "Invalid Token/Caller")
            } else {
                Log.e(TAG, "Unknown Caller to acquire token")
            }
        }
        return token
    }

    private fun startDimensioning(){
        // Enable API
        sendIntentApi(Constants.INTENT_ACTION_ENABLE_DIMENSION, Constants.MODULE, Constants.PARCEL_MODULE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        try {
            if (requestCode == 100) {
                if (intent != null) {
                    val actionName = intent.action
                    val dimResultCode = intent.getIntExtra(Constants.RESULT_CODE, Constants.FAILURE)
                    var dimResultMessage = intent.getStringExtra(Constants.RESULT_MESSAGE)
                    if (dimResultMessage == null) dimResultMessage = ""
                    Log.d(TAG,"onActivityResult: $actionName, $dimResultCode, $dimResultMessage")

                    if (dimResultCode != Constants.SUCCESS) {
                        Toast.makeText(this, dimResultMessage, Toast.LENGTH_SHORT).show()

                        // Disable API if something went wrong!
                        if(actionName != Constants.INTENT_ACTION_DISABLE_DIMENSION)
                            sendIntentApi(Constants.INTENT_ACTION_DISABLE_DIMENSION, Constants.MODULE, Constants.PARCEL_MODULE)
                    }
                    else{
                        // Otherwise, parse the results
                        when (actionName) {
                            Constants.INTENT_ACTION_ENABLE_DIMENSION ->  {
                                // set params once dimensioning is enabled
                                val params = Bundle()
                                params.putString(Constants.DIMENSIONING_UNIT, Constants.CM)
                                sendIntentApi(Constants.INTENT_ACTION_SET_DIMENSION_PARAMETER, params)
                            }
                            Constants.INTENT_ACTION_SET_DIMENSION_PARAMETER ->
                                // parameters are set, start dimensioning
                                sendIntentApi(Constants.INTENT_ACTION_GET_DIMENSION, Constants.PARCEL_ID, Constants.PARCEL_ID);
                            Constants.INTENT_ACTION_GET_DIMENSION ->
                                // Parse dimensioning response
                                parseDimensioningResponse(dimResultCode, dimResultMessage, intent)
                        }
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            Log.d(TAG, "Exception : $e")
        }
    }

    private fun parseDimensioningResponse(dimResultCode : Int, dimResultMessage : String, intent: Intent){
        var length: BigDecimal? = BigDecimal(0)
        var width: BigDecimal? = BigDecimal(0)
        var height: BigDecimal? = BigDecimal(0)

        // Get dimensioning values from intent
        val extras = intent.extras
        if (extras!!.containsKey(Constants.LENGTH))
            length = intent.getSerializableExtra(Constants.LENGTH) as BigDecimal?
        if (extras.containsKey(Constants.WIDTH))
            width = intent.getSerializableExtra(Constants.WIDTH) as BigDecimal?
        if (extras.containsKey(Constants.HEIGHT))
            height = intent.getSerializableExtra(Constants.HEIGHT) as BigDecimal?

        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

        // Return Result to the caller
        val data = Intent()
        data.putExtra("length", length)
        data.putExtra("width", width)
        data.putExtra("height", height)

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private fun sendIntentApi(action: String, extraKey: String?, extraValue: String?) {
        val extras = Bundle()
        extras.putString(extraKey, extraValue)
        sendIntentApi(action, extras)
    }

    private fun sendIntentApi(action: String, extras: Bundle?) {
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token is Null")
            return
        }
        Log.d(TAG, "sendIntentApi $action")
        val intent = Intent()
        intent.action = action
        intent.setPackage(Constants.ZEBRA_DIMENSIONING_PACKAGE)
        intent.putExtra(Constants.APPLICATION_PACKAGE, packageName)
        intent.putExtra(Constants.TOKEN, token)
        if (extras != null) {
            intent.putExtras(extras)
        }
        val pendingIntent = createPendingResult(100, Intent(),PendingIntent.FLAG_UPDATE_CURRENT)

        intent.putExtra(Constants.CALLBACK_RESPONSE, pendingIntent)
        if (intent.action.equals(Constants.INTENT_ACTION_ENABLE_DIMENSION)) {
            startForegroundService(intent)
        } else {
            sendBroadcast(intent)
        }
    }

}