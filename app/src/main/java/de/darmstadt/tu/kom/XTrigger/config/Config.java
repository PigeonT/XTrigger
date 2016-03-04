package de.darmstadt.tu.kom.XTrigger.config;

import android.content.Context;

import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public final class Config {
    private static Context context = null;
    public static final String ON_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                                                + Constants.TABLE_NAME + " ( "
                                                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + "time TEXT NOT NULL, "
                                                + "weekday TEXT NOT NULL, "
                                                + "weather TEXT NOT NULL, "
                                                + "temperature TEXT NOT NULL, "
                                                + "positionGPSTuple TEXT NOT NULL, "
                                                + "positionIdentity INT NOT NULL,  "
                                                + "chosen TEXT NOT NULL, "
                                                + "createDate INTEGER NOT NULL "
                                                + ");";

    public static final String ON_UPGRADE_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME + ";";

    public static final String ON_QUERY = "SELECT * FROM " + Constants.TABLE_NAME + ";";

    public static void setContext(Context context) {
        if(Config.context == null) {
            Config.context = context;
        }
    }

    public static Context getContext() {
        return Config.context;
    }
}
