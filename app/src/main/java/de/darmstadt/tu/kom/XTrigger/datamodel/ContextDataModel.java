package de.darmstadt.tu.kom.XTrigger.datamodel;

import java.io.Serializable;

public final class ContextDataModel implements Serializable{


    private float smartphoneSpeed;
    private double longitude;
    private double latitude;
    private long time;
    private int dayOfWeek;
    private int temp;
    private int weatherCategoryID;
    private int windSpeed;

    public ContextDataModel() {
        super();
    }

    public float getSmartphoneSpeed() {
        return smartphoneSpeed;
    }

    public ContextDataModel setSmartphoneSpeed(float speed) {
        smartphoneSpeed = speed;
        return this;
    }


    public double getLongitude() {
        return longitude;
    }

    public ContextDataModel setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public ContextDataModel setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public long getTime() {
        return time;
    }

    public ContextDataModel setTime(long time) {
        this.time = time;
        return this;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public ContextDataModel setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public int getTemp() {
        return temp;
    }

    public ContextDataModel setTemp(int temp) {
        this.temp = temp;
        return this;
    }

    public int getWeatherCategoryID() {
        return weatherCategoryID;
    }

    public ContextDataModel setWeatherCategoryID(int weatherCategoryID) {
        this.weatherCategoryID = weatherCategoryID;
        return this;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public ContextDataModel setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
        return this;
    }

}
