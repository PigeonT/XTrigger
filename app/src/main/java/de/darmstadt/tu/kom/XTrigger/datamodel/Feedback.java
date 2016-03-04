package de.darmstadt.tu.kom.XTrigger.datamodel;


import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Feedback {

    private final Map<String , Interval> intervalList;

    public Feedback() {
        super();
        intervalList = new HashMap<>();
    }

    public void addToIntervalHashmap(Interval interval) {
        intervalList.put(interval.label, interval);
    }

    public Map<String, Interval> getIntervalHashmap() {
        return this.intervalList;
    }

    public Interval getInterval(String label) {
        return this.intervalList.get(label);
    }


    public final static class Interval {
        private String label;
        private boolean defaultValue;
        private int interval;

        public void setInterval(int interval) {
            this.interval = interval;
        }

        public Interval(String label, boolean defaultValue, int interval) {
            this.label = label;
            this.defaultValue = defaultValue;
            this.interval = interval;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public boolean getDefaultValue() {
            return this.defaultValue;
        }

        public void setDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean isDefaultValue() {
            return defaultValue;
        }

        public int getInterval() {
            return interval;
        }
    }
}
