package de.darmstadt.tu.kom.XTrigger.datamodel;

import java.util.ArrayList;
import java.util.List;

public final class Weekday {
    private boolean isMonday;
    private boolean isTuesday;
    private boolean isThursday;
    private boolean isWednesday;
    private boolean isFriday;
    private boolean isSaturday;
    private boolean isSunday;

    public boolean isMonday() {
        return isMonday;
    }

    public Weekday setIsMonday(boolean isMonday) {
        this.isMonday = isMonday;
        return this;
    }

    public boolean isTuesday() {
        return isTuesday;
    }

    public Weekday setIsTuesday(boolean isTuesday) {
        this.isTuesday = isTuesday;
        return this;
    }

    public boolean isThursday() {
        return isThursday;
    }

    public Weekday setIsThursday(boolean isThursday) {
        this.isThursday = isThursday;
        return this;
    }

    public boolean isWednesday() {
        return isWednesday;
    }

    public Weekday setIsWednesday(boolean isWednesday) {
        this.isWednesday = isWednesday;
        return this;
    }

    public boolean isFriday() {
        return isFriday;
    }

    public Weekday setIsFriday(boolean isFriday) {
        this.isFriday = isFriday;
        return this;
    }

    public boolean isSaturday() {
        return isSaturday;
    }

    public Weekday setIsSaturday(boolean isSaturday) {
        this.isSaturday = isSaturday;
        return this;
    }

    public boolean isSunday() {
        return isSunday;
    }

    public Weekday setIsSunday(boolean isSunday) {
        this.isSunday = isSunday;
        return this;
    }

    public SelectTuple getSelectTuple() {
        SelectTuple selectTuple = new SelectTuple();

        if (isMonday) {
            selectTuple.selects.add("monday");
        } else {
            selectTuple.noSelects.add("monday");
        }

        if (isTuesday) {
            selectTuple.selects.add("tuesday");
        } else {
            selectTuple.noSelects.add("tuesday");
        }

        if (isWednesday) {
            selectTuple.selects.add("wednesday");
        } else {
            selectTuple.noSelects.add("wednesday");
        }

        if (isThursday) {
            selectTuple.selects.add("thursday");
        } else {
            selectTuple.noSelects.add("thursday");
        }

        if (isFriday) {
            selectTuple.selects.add("friday");
        } else {
            selectTuple.noSelects.add("friday");
        }

        if (isSaturday) {
            selectTuple.selects.add("saturday");
        } else {
            selectTuple.noSelects.add("saturday");
        }

        if (isSunday) {
            selectTuple.selects.add("sunday");
        } else {
            selectTuple.noSelects.add("sunday");
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
