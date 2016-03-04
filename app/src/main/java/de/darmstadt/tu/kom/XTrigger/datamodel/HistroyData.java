package de.darmstadt.tu.kom.XTrigger.datamodel;

import android.support.annotation.Nullable;

import java.util.Calendar;

public final class HistroyData {

    private int id;
    private String time;
    private String weekday;
    private String weather;
    private String temperature;
    //positionGPSTuple format is like "lat,lon" e.g.: (49.88,8.64)
    private String positionGPSTuple;
    private int positionIdentity;
    private String chosen;
    private long createDate;

    public HistroyData() {
        super();
    }

    public HistroyData(String time, String weekday, String weather, String temperature, int positionIdentity, long createDate) {
        super();
        this.time = time;
        this.weekday = weekday;
        this.weather = weather;
        this.temperature = temperature;
        this.positionIdentity = positionIdentity;
        this.createDate = createDate;
    }

    @Nullable
    @Override
    public String toString() {

        return  String.valueOf(this.id) + '\n' +
                this.time + '\n' +
                this.weekday + '\n' +
                this.weather + '\n' +
                this.temperature + '\n' +
                this.positionGPSTuple + '\n' +
                String.valueOf(this.positionIdentity) + '\n' +
                this.chosen + '\n' +
                String.valueOf(this.createDate) + '\n';
    }

    @Override
    public boolean equals(Object _obj) {
        try {
            if (_obj instanceof HistroyData) {
                HistroyData obj = (HistroyData) _obj;

                boolean result;

                result = time.equals(obj.time)
                        && weekday.equals(obj.weekday)
                        && weather.equals(obj.weather)
                        && temperature.equals(obj.temperature)
                        && positionIdentity == obj.positionIdentity;

                return   result;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 31 + time.hashCode();
        result = result * 31 + weekday.hashCode();
        result = result * 31 + weather.hashCode();
        result = result * 31 + temperature.hashCode();
        result = result * 31 + positionIdentity;
        result += chosen.hashCode();

        return result;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return this.id;
    }

    public HistroyData setTime(String time) {
        this.time = time;
        return this;
    }

    public HistroyData setId(int id) {
        this.id = id;
        return this;
    }

    public String getWeekday() {
        return weekday;
    }

    public HistroyData setWeekday(String weekday) {
        this.weekday = weekday;
        return this;
    }

    public String getWeather() {
        return weather;
    }

    public HistroyData setWeather(String weather) {
        this.weather = weather;
        return this;
    }

    public String getTemperature() {
        return temperature;
    }

    public HistroyData setTemperature(String temperature) {
        this.temperature = temperature;
        return this;
    }

    public String getPositionGPSTuple() {
        return positionGPSTuple;
    }

    public HistroyData setPositionGPSTuple(String positionGPSTuple) {
        this.positionGPSTuple = positionGPSTuple;
        return this;
    }

    public int getPositionIdentity() {
        return positionIdentity;
    }

    public HistroyData setPositionIdentity(int positionIdentity) {
        this.positionIdentity = positionIdentity;
        return this;
    }

    public long getCreateDate() {
        return createDate;
    }

    public HistroyData setCreateDate(long createDate) {
        this.createDate = createDate;
        return this;
    }

    public String getChosen() {
        return chosen;
    }

    public HistroyData setChosen(String chosen) {
        this.chosen = chosen;
        return this;
    }
}
