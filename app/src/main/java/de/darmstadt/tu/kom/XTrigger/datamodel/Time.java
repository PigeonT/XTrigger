package de.darmstadt.tu.kom.XTrigger.datamodel;

import android.support.annotation.Nullable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public final class Time {

    private boolean dayTime;
    private boolean night;
    private boolean midnight;

    public Time() {

    }

    public boolean isDayTime() {
        return dayTime;
    }

    public boolean isNight() {
        return night;
    }

    public boolean isMidnight() {
        return midnight;
    }

    public void setDayTime(boolean dayTime) {
        this.dayTime = dayTime;
    }

    public void setNight(boolean night) {
        this.night = night;
    }

    public void setMidnight(boolean midnight) {
        this.midnight = midnight;
    }

    public SelectTuple getSelectTuple() {
        SelectTuple selectTuple = new SelectTuple();

        if(dayTime) {
            selectTuple.selects.add("daytime");
        }else {
            selectTuple.noSelects.add("daytime");
        }

        if(night) {
            selectTuple.selects.add("night");
        }else {
            selectTuple.noSelects.add("night");
        }

        if(midnight) {
            selectTuple.selects.add("midnight");
        }else {
            selectTuple.noSelects.add("midnight");
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
