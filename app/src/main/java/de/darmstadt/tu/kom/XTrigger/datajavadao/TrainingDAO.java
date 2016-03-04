package de.darmstadt.tu.kom.XTrigger.datajavadao;

import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public final class TrainingDAO implements IDAO<Boolean> {


    public TrainingDAO() {
        super();
    }

    @Override
    public void update(Boolean b) {
        Constants.SHARED_PREFERENCES.edit().putBoolean("Training", b).apply();
    }

    @Override
    public void create(Boolean b) {
        Constants.SHARED_PREFERENCES.edit().putBoolean("Training", b).apply();
    }

    @Override
    public Boolean get() {
        return Constants.SHARED_PREFERENCES.getBoolean("Training", false);
    }

    @Override
    public void delete(Boolean b) {
        Constants.SHARED_PREFERENCES.edit().remove("Training").apply();
    }

}

