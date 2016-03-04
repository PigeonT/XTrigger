package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import de.darmstadt.tu.kom.XTrigger.datamodel.Temperature;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public final class TemperatureDAO implements IDAO<Temperature> {

    public TemperatureDAO(){
        super();
    }

    @Override
    public void update(Temperature temperatur) {
        Gson gson = new Gson();
        String json = gson.toJson(temperatur);
        Constants.SHARED_PREFERENCES.edit().putString("Temperature", json).apply();
    }

    @Override
    public void create(Temperature temperatur) {
        Gson gson = new Gson();
        String json = gson.toJson(temperatur);
        Constants.SHARED_PREFERENCES.edit().putString("Temperature", json).apply();
    }

    @Nullable
    @Override
    public Temperature get() {
        Gson gson = new Gson();
        Temperature temperature;
        if(Constants.SHARED_PREFERENCES.getString("Temperature", null) != null) {
            String json = Constants.SHARED_PREFERENCES.getString("Temperature", null);
            temperature = gson.fromJson(json, Temperature.class);
        }else {
            temperature = null;
        }
        return temperature;
    }

    @Override
    public void delete(Temperature temperatur) {
        Constants.SHARED_PREFERENCES.edit().remove("Temperature").apply();
    }
}
