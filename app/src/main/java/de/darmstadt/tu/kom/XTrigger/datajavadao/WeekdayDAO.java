package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weekday;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public final class WeekdayDAO implements IDAO<Weekday> {

    public WeekdayDAO() {
        super();
    }

    @Override
    public void update(Weekday weekday) {
        Gson gson = new Gson();
        String json = gson.toJson(weekday);
        Constants.SHARED_PREFERENCES.edit().putString("Weekday", json).apply();
    }

    @Override
    public void create(Weekday weekday) {
        Gson gson = new Gson();
        String json = gson.toJson(weekday);
        Constants.SHARED_PREFERENCES.edit().putString("Weekday", json).apply();

    }

    @Nullable
    @Override
    public Weekday get() {
        Weekday weekday;

        if(Constants.SHARED_PREFERENCES.getString("Weekday", null) != null) {
            Gson gson = new Gson();
            weekday = gson.fromJson(Constants.SHARED_PREFERENCES.getString("Weekday",null), Weekday.class);
        }else {
            weekday = null;
        }
        return weekday;
    }

    @Override
    public void delete(Weekday weekday) {
        Constants.SHARED_PREFERENCES.edit().remove("Weekday").apply();

    }
}
