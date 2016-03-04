package de.darmstadt.tu.kom.XTrigger.broadcastreceiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.datajavadao.ModiDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TrainingDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;
import de.darmstadt.tu.kom.XTrigger.helper.Helper;
import de.darmstadt.tu.kom.XTrigger.service.DynamicReasoner;
import de.darmstadt.tu.kom.XTrigger.service.StaticReasoner;


public class AlgorithmServiceReceiver extends BroadcastReceiver implements LocationListener {
    private static Handler algorithmResultHanlder;
    private LocationManager locationManager;
    private Intent intentToStartService;
    private Location location;
    private Context context;
    private boolean alreadyGetLocation = false;


    @Override
    public void onReceive(final Context context, Intent intent) {

        if (Config.getContext() == null) {
            Config.setContext(context);
        }

//        PendingIntent pendingIntent = Constants.START_ALGORITHM_PENDING_INTENT;
//        AlarmManager alarmManger = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.TRIGGER_INTERVAL, pendingIntent);

        //Intent _intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        //startActivityForResult(_intent, Constants.REQUEST_CODE_FOR_ENABLE_LOCATION_SERVICE);

        this.context = context;

        int modi = new ModiDAO().get();

        switch (modi) {
            case R.id.modi_static:
                intentToStartService = new Intent(context, StaticReasoner.class);
                break;
            case R.id.modi_dynamic:
                intentToStartService = new Intent(context, DynamicReasoner.class);
                break;
        }

        algorithmResultHanlder = new Handler(new Handler.Callback() {


            @Override
            public boolean handleMessage(Message msg) {
                String _result;
                Notification notification;
                boolean btnSwitch = new TrainingDAO().get();

                Log.d("handleMessage", (String) msg.getData().get("result"));
                if (msg.getData() != null) {
                    try {
                        Bundle mBundle = msg.getData();
                        String result;
                        if ((result = (String) mBundle.get("result")) != null && result.equals("success")) {
                            // yes intent
                            Intent yesIntent = new Intent(context, NotificationServiceReceiver.class);
                            yesIntent.setAction("yes");
                            yesIntent.putExtra("contextModelBundle", mBundle);
                            PendingIntent yesNotificationAction = PendingIntent.getBroadcast(context, 1203, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            //no intent
                            Intent noIntent = new Intent(context, NotificationServiceReceiver.class);
                            noIntent.setAction("no");
                            noIntent.putExtra("contextModelBundle", mBundle);
                            PendingIntent noNotificationAction = PendingIntent.getBroadcast(context, 1203, noIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            //cancel intent
                            Intent cancelIntent = new Intent(context, NotificationServiceReceiver.class);
                            cancelIntent.setAction("cancel");
                            PendingIntent cancelNotificationAction = PendingIntent.getBroadcast(context, 1203, cancelIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                            long pattern[] = {1, 1000, 500};

                            if (!btnSwitch) {
                                notification = new Notification.Builder(Constants.CONTEXT)
                                        .setContentTitle("Zeit für TwoStone")
                                        .setContentText("Es ist Zeit, TwoStone zu spielen!")
                                        .setSmallIcon(R.drawable.notification_icon)
                                        .setVibrate(pattern)
                                        .addAction(R.drawable.done, "Yes", yesNotificationAction)
                                        .addAction(R.drawable.cancel, "Cancel", cancelNotificationAction)
                                        .addAction(R.drawable.close, "No", noNotificationAction)
                                        .setWhen(System.currentTimeMillis())
                                        .build();
                            } else {
                                notification = new Notification.Builder(Constants.CONTEXT)
                                        .setContentTitle("Training Module")
                                        .setContentText("Ob Ihnen das Triggering jetzt passt?")
                                        .setSmallIcon(R.drawable.notification_icon)
                                        .setVibrate(pattern)
                                        .addAction(R.drawable.done, "Yes", yesNotificationAction)
                                        .addAction(R.drawable.close, "No", noNotificationAction)
                                        .setWhen(System.currentTimeMillis())
                                        .build();
                            }


                            NotificationManager notificationManager = (NotificationManager) Constants.CONTEXT.getSystemService(Context.NOTIFICATION_SERVICE);

                            notificationManager.notify(Constants.NOTIFICATION_ID, notification);
                            return true;
                        } else {
                            return false;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });

        location = getLocation();

        new WeatherFetherAsyncTask().execute(location);
    }


    private Location getLocation() {

        boolean isGPSEnabled;
        boolean isNetworkEnabled;
        Location location = null;

        try {
            locationManager = (LocationManager) Constants.CONTEXT.getSystemService(this.context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.e("no context", "can not construct context");
            } else {
                // if gps Enabled get lat/long using gps Services
                if (isGPSEnabled) {
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
//                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, this);
                    Log.d("GPS", "GPS Enabled");
//                        Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) {
                        //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        location = getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.d("lat", String.valueOf(location.getLatitude()));
                            Log.d("lng", String.valueOf(location.getLongitude()));
                        }
                    }

                }
                // if network Enabled get lat/long using network Services
                else if (isNetworkEnabled) {

                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        //location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        location = getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Log.d("lastKnowLocation", "get last know location successfully");
                            //ContextDataModel contextDataModel = new ContextDataModel();


                            Log.d("lat", String.valueOf(location.getLatitude()));
                            Log.d("lng", String.valueOf(location.getLongitude()));
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    @Override
    public void onLocationChanged(Location location) {

        Log.i("INFO", "Location changed");
        Log.d("lat", String.valueOf(location.getLatitude()));
        Log.d("lng", String.valueOf(location.getLongitude()));
    }


//    private Location getLastKnownLocation() {
//        locationManager = (LocationManager) context.getApplicationContext().getSystemService(this.context.LOCATION_SERVICE);
//        List<String> providers = locationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers) {
//            Location l = locationManager.getLastKnownLocation(provider);
//            if (l == null) {
//                continue;
//            }
//
//            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                // Found best last known location: %s", l);
//                bestLocation = l;
//            }
//
////            if (bestLocation == null) {
////                // Found best last known location: %s", l);
////                bestLocation = l;
////            }
//
//        }
//
//        return bestLocation;
//    }

    private Location getLastKnownLocation(String lm) {
        locationManager = (LocationManager) context.getApplicationContext().getSystemService(this.context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }

            if ((provider.toString().equals("passive") || provider.toString().equals(lm)) && (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }

//            if (bestLocation == null) {
//                // Found best last known location: %s", l);
//                bestLocation = l;
//            }

        }

        return bestLocation;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static Handler getstartAlgorithmServiceReceiverHandler() {
        return AlgorithmServiceReceiver.algorithmResultHanlder;
    }

    private class WeatherFetherAsyncTask extends AsyncTask<Location, Void, JSONObject> {
        private JSONObject jsonObject;
        private int temp = 0;
        private int windSpeed = 0;
        private int weatherId = 0;
        double latitude;
        double longitude;
        float smartphonespeed;
        long currentTime = System.currentTimeMillis();
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


        @Override
        protected JSONObject doInBackground(Location... params) {
            Location location = params[0];
            if(location != null) {
                //locationManager = (LocationManager) context.getApplicationContext().getSystemService(Config.getContext().LOCATION_SERVICE);
                //location = getLastKnownLocation("passive");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                smartphonespeed = location.getSpeed();
                if (isNetworkAvailable()) {

                    jsonObject = Helper.WeatherHttpClient.getWeather(location.getLatitude(), location.getLongitude());
                }

            }
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//            smartphonespeed = location.getSpeed();
//
//            jsonObject = Helper.WeatherHttpClient.getWeather(location.getLatitude(), location.getLongitude());

            return jsonObject;
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }


        @Override
        protected void onPostExecute(JSONObject result) {
//            Log.d("JsonObject", jsonObject.toString());
//            jsonObject = result;
            try {
                if(result != null) {
                    temp = result.getJSONObject("main").getInt("temp");
                    //{
                    //  "id" : 802,
                    //  "main" : "Clouds",
                    //  "description" : "foo",
                    //  "icon" : "bar"
                    //}
                    //here get weather id, id for different weather group, see http://openweathermap.org/weather-conditions
                    //here we only need groupId, so get the first digit of this IntegerValue
                    Log.d("weather", jsonObject.toString());
//                weatherId = Character.getNumericValue(String.valueOf(jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id")).charAt(0));
                    weatherId = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
                    windSpeed = jsonObject.getJSONObject("wind").getInt("speed");


                    //construct context data model
                    ContextDataModel contextDataModel = new ContextDataModel()
                            .setSmartphoneSpeed(smartphonespeed)
                            .setLongitude(longitude)
                            .setLatitude(latitude)
                            .setTime(currentTime)
                            .setDayOfWeek(dayOfWeek)
                            .setTemp(getCelsius(temp))
                            .setWeatherCategoryID(weatherId)
                            .setWindSpeed(windSpeed);

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ContextDataModel", contextDataModel);
                    intentToStartService.putExtras(bundle);
                    context.startService(intentToStartService);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private int getCelsius(int kelvin) {
        return kelvin - 273;
    }

}
