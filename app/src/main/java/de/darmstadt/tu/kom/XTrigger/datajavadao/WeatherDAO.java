package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import de.darmstadt.tu.kom.XTrigger.datamodel.Weather;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public final class WeatherDAO implements IDAO<Weather> {

    public WeatherDAO() {
        super();
    }

    @Override
    public void update(Weather weather) {
        Gson gson = new Gson();
        String json = gson.toJson(weather);
        Constants.SHARED_PREFERENCES.edit().putString("Weather", json).apply();
    }

    @Override
    public void create(Weather weather) {
        Gson gson = new Gson();
        String json = gson.toJson(weather);
        Constants.SHARED_PREFERENCES.edit().putString("Weather", json).apply();
    }

    @Nullable
    @Override
    public Weather get() {

        Gson gson = new Gson();
        Weather weather;
        if(Constants.SHARED_PREFERENCES.getString("Weather", null) != null) {
            String json = Constants.SHARED_PREFERENCES.getString("Weather", null);
            weather = gson.fromJson(json, Weather.class);
        }else {
            weather = null;
        }
        return weather;
    }

    @Override
    public void delete(Weather weather) {
        Constants.SHARED_PREFERENCES.edit().remove("Weather").apply();

    }
}
