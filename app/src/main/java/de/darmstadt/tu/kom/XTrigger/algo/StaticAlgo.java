package de.darmstadt.tu.kom.XTrigger.algo;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.darmstadt.tu.kom.XTrigger.datajavadao.PlaceDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TemperatureDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TimeDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeatherDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeekdayDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import de.darmstadt.tu.kom.XTrigger.datamodel.Place;
import de.darmstadt.tu.kom.XTrigger.datamodel.Temperature;
import de.darmstadt.tu.kom.XTrigger.datamodel.Time;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weather;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weekday;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;
import de.darmstadt.tu.kom.XTrigger.helper.Helper;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public abstract class StaticAlgo implements IAlgorithm {

    protected Classifier classifier;
    protected List<Attribute> attributeList;
    protected List<Attribute> allAttributes;
    protected Instances instanes;
    protected ContextDataModel contextDataModel;

    protected abstract boolean algorithm();

    protected StaticAlgo(ContextDataModel contextDataModel) {
        super();
        attributeList = new ArrayList<>();
        this.contextDataModel = contextDataModel;
    }

    @Override
    public boolean execute() {
        if (contextDataModel != null) {
            Long yes, no;

            if ((yes = Constants.SHARED_PREFERENCES.getLong("notificationYes", -1)) != -1) {
                if (System.currentTimeMillis() < yes) {
                    return false;
                } else {
                    Constants.SHARED_PREFERENCES.edit().remove("notificationYes").apply();
                }
            }

            if ((no = Constants.SHARED_PREFERENCES.getLong("notificationNo", -1)) != -1) {
                if (System.currentTimeMillis() < no) {
                    return false;
                } else {
                    Constants.SHARED_PREFERENCES.edit().remove("notificationNo").apply();
                }
            }

//                Log.d("shouldTrigger", String.valueOf(shouldTrigger));
            return algorithm();

        }
        return false;
    }


    protected void initWekaCore() {
        attributeList.add(new Attribute("time", Arrays.asList("daytime", "night", "midnight")));
        attributeList.add(new Attribute("weekday", Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")));
        attributeList.add(new Attribute("weather", Arrays.asList("clear", "rainny", "foggy")));
        attributeList.add(new Attribute("temperature", Arrays.asList("icy", "mild", "warm", "hot")));

        //classes
        attributeList.add(new Attribute("Class", Arrays.asList("yes", "no")));

        allAttributes = new ArrayList<>(Arrays.asList(attributeList.get(0), attributeList.get(1), attributeList.get(2), attributeList.get(3), attributeList.get(4)));
        instanes = new Instances("TrainingSample", (ArrayList<Attribute>) allAttributes, 0);
        instanes.setClass(attributeList.get(4));
    }

    @Nullable
    protected String getWeather() {
        int weather = contextDataModel.getWeatherCategoryID();

        if (weather == 951 || weather == 952 || weather == 953 || weather == 954 || weather == 955) {
            return "clear";
        } else {
            //see openweathermap api group
            // http://openweathermap.org/weather-conditions
            int _weather = Character.getNumericValue((String.valueOf(weather).charAt(0)));
            switch (_weather) {
                //clear or cloud, both group id is 8
                case 8:
                    return "clear";
                //rainy
                case 7:
                case 3:
                    return "foggy";
                //snowy
                case 5:
                case 6:
                    return "rainny";
                default:
                    return null;
            }
        }
    }

    @Nullable
    protected String getTemperature() {
        int temp = contextDataModel.getTemp();

        if (temp < 0) {
            return "icy";
        } else if (temp >= 0 && temp < 15) {
            return "mild";
        } else if (temp >= 15 && temp < 25) {
            return "warm";
        } else if (temp >= 25) {
            return "hot";

        } else {
            return null;
        }
    }

    @Nullable
    protected String getWeekday() {
        switch (contextDataModel.getDayOfWeek()) {
            case 1:
                return "sunday";
            case 2:
                return "monday";
            case 3:
                return "tuesday";
            case 4:
                return "wednesday";
            case 5:
                return "thursday";
            case 6:
                return "friday";
            case 7:
                return "saturday";
            default:
                return null;

        }

    }

    @Nullable
    protected String getTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(contextDataModel.getTime());

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (currentHour >= 6 && currentHour < 17) {
            return "daytime";
        } else if (currentHour >= 17 && currentHour < 21) {
            return "night";
        } else if (currentHour >= 21 && currentHour < 24) {
            return "midnight";
        } else if (currentHour >= 0 && currentHour < 6) {
            return "midnight";
        } else {
            return null;
        }
    }

    protected boolean checkGPSDistance() {

        double currentLat = contextDataModel.getLatitude();
        double currentLon = contextDataModel.getLongitude();

        Place place = new PlaceDAO().get();

        if (place.isAllowedEverywhere()) return true;

        if (place.isTriggerInTheseStreets()) {

            if (place.isTriggerSomewhereElse()) return true;
            else {
                List<Place.Address> addressList = place.getAddressList();

                for (Place.Address address : addressList) {
                    double distance = Helper.distance(currentLat, address.getLatitude(), currentLon, address.getLongitude(), 0, 0);
                    //distance smaller than 100 meters
                    if (distance < 100) {
                        return true;
                    }
                }
            }

        } else {
            if (!place.isTriggerSomewhereElse()) return false;
            else {
                List<Place.Address> addressList = place.getAddressList();
                boolean _trigger = true;
                for (Place.Address address : addressList) {
                    double distance = Helper.distance(currentLat, address.getLatitude(), currentLon, address.getLongitude(), 0, 0);
                    //distance smaller than 100 meters
                    Log.d("distance", String.valueOf(distance));
                    if (distance < 100) {

                        _trigger = false;
                        break;
                    }
                }
                return _trigger;
            }

        }

        return false;
    }

    protected abstract void initClassfier() throws Exception;

    protected double[] testSample() throws Exception {
        String time;
        String weekday;
        String weather;
        String temperature;

        Instance testSample = new DenseInstance(allAttributes.size());
        testSample.setDataset(instanes);

        time = getTime();
        weekday = getWeekday();
        weather = getWeather();
        temperature = getTemperature();

        testSample.setValue(attributeList.get(0), time);
        testSample.setValue(attributeList.get(1), weekday);
        testSample.setValue(attributeList.get(2), weather);
        testSample.setValue(attributeList.get(3), temperature);

        return classifier.distributionForInstance(testSample);
    };

    protected void initDenseInstance() {
        Time _time = new TimeDAO().get();
        Weekday _weekday = new WeekdayDAO().get();
        Weather _weather = new WeatherDAO().get();
        Temperature _temperature = new TemperatureDAO().get();

        //add yes class
        for (String time : _time.getSelectTuple().getSelects()) {
            for (String weekday : _weekday.getSelectTuple().getSelects()) {
                for (String weather : _weather.getSelectTuple().getSelects()) {
                    for (String temperature : _temperature.getSelectTuple().getSelects()) {

                        Instance instance = new DenseInstance(allAttributes.size());
                        instance.setDataset(instanes);

                        instance.setValue(attributeList.get(0), time);
                        instance.setValue(attributeList.get(1), weekday);
                        instance.setValue(attributeList.get(2), weather);
                        instance.setValue(attributeList.get(3), temperature);
                        instance.setValue(attributeList.get(4), "yes");

                        instanes.add(instance);
                    }
                }
            }
        }

        //add no class
        for (String time : _time.getSelectTuple().getNoSelects()) {
            for (String weekday : _weekday.getSelectTuple().getNoSelects()) {
                for (String weather : _weather.getSelectTuple().getNoSelects()) {
                    for (String temperature : _temperature.getSelectTuple().getNoSelects()) {
                        Instance instance = new DenseInstance(allAttributes.size());
                        instance.setDataset(instanes);

                        instance.setValue(attributeList.get(0), time);
                        instance.setValue(attributeList.get(1), weekday);
                        instance.setValue(attributeList.get(2), weather);
                        instance.setValue(attributeList.get(3), temperature);
                        instance.setValue(attributeList.get(4), "no");

                        instanes.add(instance);
                    }
                }
            }
        }

    }

}
