package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.datamodel.Feedback;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public final class FeedbackDAO implements IDAO<Feedback> {
    private final Context context;

    public FeedbackDAO() {
        super();
        context = Config.getContext();
    }
    @Override
    public void update(Feedback feedback) {
        Gson gson = new Gson();
        String json = gson.toJson(feedback);
        Constants.SHARED_PREFERENCES.edit().putString("Feedback", json).apply();
    }

    @Override
    public void create(Feedback feedback) {
        Gson gson = new Gson();
        String json = gson.toJson(feedback);
        Constants.SHARED_PREFERENCES.edit().putString("Feedback", json).apply();
    }

    @Nullable
    @Override
    public Feedback get() {
        Gson gson = new Gson();
        return gson.fromJson(Constants.SHARED_PREFERENCES.getString("Feedback", null), Feedback.class);
        //return feedback;
    }

    @Override
    public void delete(Feedback feedback) {
        Constants.SHARED_PREFERENCES.edit().remove("Feedback").apply();
    }
}
