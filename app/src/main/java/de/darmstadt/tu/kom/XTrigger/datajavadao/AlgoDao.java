package de.darmstadt.tu.kom.XTrigger.datajavadao;

import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public final class AlgoDao implements IDAO<Integer> {

    public AlgoDao() {
        super();
    }

    @Override
    public void update(Integer s) {
        Constants.SHARED_PREFERENCES.edit().putInt("Algo", s).apply();
    }

    @Override
    public void create(Integer s) {
        Constants.SHARED_PREFERENCES.edit().putInt("Algo", s).apply();
    }

    @Override
    public Integer get() {
        return Constants.SHARED_PREFERENCES.getInt("Algo", 0);
    }

    @Override
    public void delete(Integer s) {
        Constants.SHARED_PREFERENCES.edit().remove("Algo").apply();
    }
}
