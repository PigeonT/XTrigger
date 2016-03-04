package de.darmstadt.tu.kom.XTrigger.helper;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import de.darmstadt.tu.kom.XTrigger.broadcastreceiver.AlgorithmServiceReceiver;
import de.darmstadt.tu.kom.XTrigger.config.Config;


public final class Constants {

    public static final String[] SETTING_ITEMS_LABEL = {"Zeit", "Wochentag", "Wetter", "Temperatur", "Ort", "Feedback Einstellung"};
    public static final int REQUEST_CODE_FOR_ENABLE_LOCATION_SERVICE = 1001;

    public enum SETTING_CATEGORY {
        TIME(0), WEEKDAY(1), WEATHER(2), TEMPERATURE(3), PLACE(4), FEEDBACKSETTING(5);

        private int index = 0;
        SETTING_CATEGORY(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

    public static final Context CONTEXT = Config.getContext();

//    public static final String API_KEY_FOR_GEOCODING = "AIzaSyAKvQXeLrIL50UwKKDtcKR5JbUABlWJLr8";

    public static final String GOOGLE_MAP_URL = "http://maps.googleapis.com/maps/api/geocode/json?address=";

    public static final String SENSOR_FALSE = "&sensor=false";

    public static final SharedPreferences SHARED_PREFERENCES = CONTEXT.getSharedPreferences("de.darmstadt.tu.kom.xtrigger.SHARED_PREFERENCE", Context.MODE_PRIVATE);

    public static final int NOTIFICATION_ID = 10023;

    public static final int PENDING_INTENT_ID = 1322;

    public static final Intent START_ALGORITHM_INTENT = new Intent(CONTEXT, AlgorithmServiceReceiver.class);

    public static final PendingIntent START_ALGORITHM_PENDING_INTENT = PendingIntent.getBroadcast(CONTEXT, Constants.PENDING_INTENT_ID, START_ALGORITHM_INTENT, PendingIntent.FLAG_UPDATE_CURRENT);

    public static final int TRIGGER_INTERVAL = 900000;

    public static final int TRIGGER_INTERVAL_TRAINING = 1800000;//300000;

    public static final String DATABASE_NAME = "db_backbone";

    public static final String TABLE_NAME = "HistroyData";

    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_TIME = "time";

    public static final String COLUMN_WEEKDAY ="weekday";

    public static final String COLUMN_WEATHER = "weather";

    public static final String COLUMN_TEMPERATURE = "temperature";

    public static final String COLUMN_POSITIONGPSTUPLE = "positionGPSTuple";

    public static final String COLUMN_POSITIONIDENTITY = "positionIdentity";

    public static final String COLUMN_CREATEDATE = "createDate";

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_CHOSEN = "chosen";

}
