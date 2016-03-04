package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.datamodel.Time;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public final class TimeDAO implements IDAO<Time> {

    public TimeDAO() {
        super();
    }

    @Override
    public void update(Time time) {
        Gson gson = new Gson();
        String json = gson.toJson(time);
        Constants.SHARED_PREFERENCES.edit().putString("Time", json).apply();
    }

    @Override
    public void create(Time time) {
        Gson gson = new Gson();
        String json = gson.toJson(time);
        Constants.SHARED_PREFERENCES.edit().putString("Time", json).apply();
    }

    @Nullable
    @Override
    public Time get() {
        Gson gson = new Gson();
        Time time;

        if(Constants.SHARED_PREFERENCES.getString("Time", null) != null) {
            String json = Constants.SHARED_PREFERENCES.getString("Time", null);
            time = gson.fromJson(json, Time.class);
        }else {
            time = null;
        }
        return time;
    }

    @Override
    public void delete(Time time) {
        Constants.SHARED_PREFERENCES.edit().remove("Time").apply();
    }
}
