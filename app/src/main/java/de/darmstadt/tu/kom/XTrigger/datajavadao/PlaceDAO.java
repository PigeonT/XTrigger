package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import de.darmstadt.tu.kom.XTrigger.datamodel.Place;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public final class PlaceDAO implements IDAO<Place> {

    public PlaceDAO() {
        super();
    }

    @Override
    public void update(Place place) {
        Gson gson = new Gson();
        String json = gson.toJson(place);
        Constants.SHARED_PREFERENCES.edit().putString("Place", json).apply();
    }

    @Override
    public void create(Place place) {
        Gson gson = new Gson();
        String json = gson.toJson(place);
        Constants.SHARED_PREFERENCES.edit().putString("Place", json).apply();
    }

    @Nullable
    @Override
    public Place get() {
        Gson gson = new Gson();
        Place place;

        if(Constants.SHARED_PREFERENCES.getString("Place", null) != null) {
            String _place = Constants.SHARED_PREFERENCES.getString("Place", null);
            place = gson.fromJson(_place, Place.class);
        }else {
            place = null;
        }

        return place;
    }

    @Override
    public void delete(Place place) {
        Gson gson = new Gson();
        Constants.SHARED_PREFERENCES.edit().remove("Place").apply();

    }
}
