package de.darmstadt.tu.kom.XTrigger.datamodel;

import java.util.ArrayList;
import java.util.List;

public final class Weather {

    private boolean clear;
    private boolean rainny;
    private boolean foggy;

    public Weather() {
        super();
    }

    public boolean isClear() {
        return clear;
    }

    public boolean isRainny() {
        return rainny;
    }

    public boolean isFoggy() {
        return foggy;
    }



    public Weather setClear(boolean clear) {
        this.clear = clear;
        return this;
    }
    public Weather setRainny(boolean rainny) {
        this.rainny = rainny;
        return this;
    }

    public Weather setFoggy(boolean foggy) {
        this.foggy = foggy;
        return this;
    }

    public SelectTuple getSelectTuple() {
        SelectTuple selectTuple = new SelectTuple();

        if (isClear()) {
            selectTuple.selects.add("clear");
        } else {
            selectTuple.noSelects.add("clear");
        }

        if (isRainny()) {
            selectTuple.selects.add("rainny");
        } else {
            selectTuple.noSelects.add("rainny");
        }

        if (isFoggy()) {
            selectTuple.selects.add("foggy");
        } else {
            selectTuple.noSelects.add("foggy");
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
