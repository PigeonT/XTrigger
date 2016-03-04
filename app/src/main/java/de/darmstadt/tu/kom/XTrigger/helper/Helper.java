package de.darmstadt.tu.kom.XTrigger.helper;

import android.support.v4.app.DialogFragment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import de.darmstadt.tu.kom.XTrigger.fragments.dialog.FeedbackSettingDialogFragment;
import de.darmstadt.tu.kom.XTrigger.fragments.dialog.PlaceDialogFragment;
import de.darmstadt.tu.kom.XTrigger.fragments.dialog.TemperatureDialogFragment;
import de.darmstadt.tu.kom.XTrigger.fragments.dialog.TimeDialogFragment;
import de.darmstadt.tu.kom.XTrigger.fragments.dialog.WeatherDialogFragment;
import de.darmstadt.tu.kom.XTrigger.fragments.dialog.WeekdayDialogFragment;

/**
 * Created by pigeon on 09/01/16.
 */
public final class Helper {

    /*
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    /***
     * factory method to create dialog fragment
     * @param category category of this to be created Dialog
     * @return DialogFragment
     */
    public static DialogFragment DialogFactory(Constants.SETTING_CATEGORY category) {
        DialogFragment df;
        switch(category) {
            case TIME:
                df = new TimeDialogFragment();
                break;
            case WEEKDAY:
                df = new WeekdayDialogFragment();
                break;
            case WEATHER:
                df = new WeatherDialogFragment();
                break;
            case TEMPERATURE:
                df = new TemperatureDialogFragment();
                break;
            case PLACE:
                df = new PlaceDialogFragment();
                break;
            case FEEDBACKSETTING:
                df = new FeedbackSettingDialogFragment();
                break;
            default :
                return null;
        }
        return df;
    }

    ///http weather fetcher
    public static final class WeatherHttpClient {

        private final static String APIKEY = "022002b045b5a921d60e04e68c8f585d";

        private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";

        static InputStream is = null;
        static JSONObject jObj = null;
        static String json = "";

        // function get json from url
        // by making HTTP GET mehtod
        public static JSONObject getWeather(double lat, double lon) {

            // Making HTTP request
            try {

                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(BASE_URL + "lat=" + String.valueOf(lat) +"&lon=" + String.valueOf(lon)  + "&APPID="
                        + APIKEY);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            // return JSON String
            return jObj;

        }

    }

}
