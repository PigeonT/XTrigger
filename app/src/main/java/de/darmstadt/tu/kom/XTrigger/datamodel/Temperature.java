package de.darmstadt.tu.kom.XTrigger.datamodel;

import java.util.ArrayList;
import java.util.List;

public final class Temperature {

    private boolean icy;
    private boolean mild;
    private boolean hot;
    private boolean warm;

    public Temperature() {
        super();
    }

    public Temperature setIcy(boolean icy) {
        this.icy = icy;
        return this;
    }

    public Temperature setMild(boolean mild) {
        this.mild = mild;
        return this;
    }

    public Temperature setHot(boolean hot) {
        this.hot = hot;
        return this;
    }

    public Temperature setWarm(boolean warm) {
        this.warm = warm;
        return this;
    }

    public boolean isIcy() {
        return icy;
    }

    public boolean isMild() {
        return mild;
    }

    public boolean isHot() {
        return hot;
    }

    public boolean isWarm() {
        return warm;
    }

    public SelectTuple getSelectTuple() {
        SelectTuple selectTuple = new SelectTuple();

        if (isIcy()) {
            selectTuple.selects.add("icy");
        } else {
            selectTuple.noSelects.add("icy");
        }

        if (isMild()) {
            selectTuple.selects.add("mild");
        } else {
            selectTuple.noSelects.add("mild");
        }

        if (isWarm()) {
            selectTuple.selects.add("warm");
        } else {
            selectTuple.noSelects.add("warm");
        }

        if (isHot()) {
            selectTuple.selects.add("hot");
        } else {
            selectTuple.noSelects.add("hot");
        }

        return selectTuple;
    }


    public static final class SelectTuple {
        private final List<String> selects;
        private final List<String> noSelects;

        private SelectTuple() {
            selects = new ArrayList<>();
            noSelects = new ArrayList<>();
        }

        public List<String> getSelects() {
            return selects;
        }

        public List<String> getNoSelects() {
            return noSelects;
        }

    }

}
