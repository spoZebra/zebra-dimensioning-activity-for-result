package com.spozebra.zebradimensioningactivityforresult

object Constants {

    // EMDK
    const val SERVICE_ACCESS_PROFILE = "ConfigProfile"
    const val IS_SERVICE_ACCESS_GRANTED = "isServiceAccessGranted"

    // ZEBRA SSM
    const val SSM_TARGET_APP_PACKAGE = "target_app_package"
    const val DATA_NAME = "data_name"
    const val DATA_VALUE = "data_value"
    const val DATA_INPUT_FORM = "data_input_form"
    const val DATA_OUTPUT_FORM = "data_output_form"
    const val DATA_PERSIST_REQUIRED = "data_persist_required"
    const val MULTI_INSTANCE_REQUIRED = "multi_instance_required"
    const val SSM_AUTHORITY = "content://com.zebra.securestoragemanager.securecontentprovider/data"

    // DIMENSIONING
    const val REQUEST_TOKEN_URI ="content://com.zebra.devicemanager.zdmcontentprovider/AcquireToken";
    const val COLUMN_QUERY_RESULT = "query_result"
    const val SERVICE_IDENTIFIER = "delegation-zebra-com.zebra.mobiledimensioning-Enable"
    const val ZEBRA_DIMENSIONING_PACKAGE = "com.zebra.dimensioning"
    const val INTENT_ACTION_APPLICATION_CRASH = "com.zebra.dimensioning.APPLICATION_CRASH"
    const val INTENT_ACTION_ENABLE_DIMENSION = "com.zebra.dimensioning.ENABLE_DIMENSION"
    const val INTENT_ACTION_DISABLE_DIMENSION = "com.zebra.dimensioning.DISABLE_DIMENSION"
    const val INTENT_ACTION_GET_DIMENSION = "com.zebra.parceldimensioning.GET_DIMENSION"
    const val INTENT_ACTION_GET_DIMENSION_PARAMETER = "com.zebra.parceldimensioning.GET_DIMENSION_PARAMETER"
    const val INTENT_ACTION_SET_DIMENSION_PARAMETER = "com.zebra.parceldimensioning.SET_DIMENSION_PARAMETER"
    const val TOKEN = "TOKEN"
    const val APPLICATION_PACKAGE = "APPLICATION_PACKAGE"
    const val CALLBACK_RESPONSE = "CALLBACK_RESPONSE"
    const val MODULE = "MODULE"
    const val RESULT_CODE = "RESULT_CODE"
    const val RESULT_MESSAGE = "RESULT_MESSAGE"
    const val READY_LENGTH = "READY_LENGTH"
    const val READY_WIDTH = "READY_WIDTH"
    const val READY_HEIGHT = "READY_HEIGHT"
    const val DIMENSIONING_UNIT = "DIMENSIONING_UNIT"
    const val FRAMEWORK_VERSION = "FRAMEWORK_VERSION"
    const val SERVICE_VERSION = "SERVICE_VERSION"
    const val BUNDLE_VERSION = "BUNDLE_VERSION"
    const val REGULATORY_APPROVAL = "REGULATORY_APPROVAL"
    const val SUPPORTED_UNITS = "SUPPORTED_UNITS"
    const val REPORT_IMAGE = "REPORT_IMAGE"
    const val LENGTH = "LENGTH"
    const val WIDTH = "WIDTH"
    const val HEIGHT = "HEIGHT"
    const val LENGTH_STATUS = "LENGTH_STATUS"
    const val WIDTH_STATUS = "WIDTH_STATUS"
    const val HEIGHT_STATUS = "HEIGHT_STATUS"
    const val TIMESTAMP = "TIMESTAMP"
    const val IMAGE = "IMAGE"
    const val PARCEL_ID = "PARCEL_ID"
    const val PARCEL_MODULE = "parcel"
    const val SUCCESS = 0
    const val FAILURE = 1
    const val ERROR = 2
    const val CANCELED = 3
    const val NO_DIM = "NoDim"
    const val BELOW_RANGE = "BelowRange"
    const val IN_RANGE = "InRange"
    const val ABOVE_RANGE = "AboveRange"
    const val INCH = "Inch"
    const val CM = "CM"
}