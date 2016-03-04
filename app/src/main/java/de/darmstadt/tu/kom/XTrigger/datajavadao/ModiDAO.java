package de.darmstadt.tu.kom.XTrigger.datajavadao;

import android.content.Context;

import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public final class ModiDAO implements IDAO<Integer> {
    private final Context context;

    public ModiDAO() {
        super();
        this.context = Config.getContext();
    }

    @Override
    public void update(Integer s) {
        Constants.SHARED_PREFERENCES.edit().putInt("Modi", s).apply();
    }

    @Override
    public void create(Integer s) {
        Constants.SHARED_PREFERENCES.edit().putInt("Modi", s).apply();
    }

    @Override
    public Integer get() {
        return Constants.SHARED_PREFERENCES.getInt("Modi", 0);
    }

    @Override
    public void delete(Integer s) {
        Constants.SHARED_PREFERENCES.edit().remove("Modi").apply();
    }
}
