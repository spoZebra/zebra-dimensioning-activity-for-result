# Zebra Mobile Parcel API "Activity For Result"

Zebra Mobile Parcel API works with Pending Intents, but it requires getting a token before invoking it (https://techdocs.zebra.com/mobile-parcel/1-0/guide/api/#2generateapitoken).

Even if this token can be obtained using Google's standards like Android Content Provider, it might not be possible to get it in some specific integrations.

Therefore, this kind of middleware allows other apps to invoke Mobile Parcel API and get the dimensioning in a one-shot call getting the result directly from the activity: https://developer.android.com/training/basics/intents/result.

Moreover, this app automatically requests permissions to use Mobile Parcel API to ZDM (Zebra Device Manager) thru EMDK: https://techdocs.zebra.com/flux/query/

## Velocity Use Case

https://github.com/spoZebra/velocity-emulation-zebra-dimensioning

##
