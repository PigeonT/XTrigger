package de.darmstadt.tu.kom.XTrigger.algo;

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import de.darmstadt.tu.kom.XTrigger.datajavadao.HistroyDataDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import de.darmstadt.tu.kom.XTrigger.datamodel.HistroyData;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public abstract class DynamicAlgo implements IAlgorithm {
    protected Classifier classifier;
    protected List<Attribute> attributeList;
    protected ArrayList<Attribute> allAttributes;
    protected Instances instanes;
    protected ContextDataModel contextDataModel;
    protected boolean shouldTrigger;

    protected DynamicAlgo(ContextDataModel contextDataModel) {
        super();
        this.contextDataModel = contextDataModel;
        attributeList = new ArrayList<>();
        shouldTrigger = false;
    }

    protected abstract void initClassfier() throws Exception;

    @Override
    public boolean execute() {
        if (contextDataModel != null) {
            return algorithm();
        } else {
            return false;
        }
    }

    protected boolean algorithm() {

        double dist[];

        initWekaCore();
        initDenseInstance();

        try {
            initClassfier();
            dist = testSample();
            // if yes probability > no probability, then shouldTrigger = true
            // otherwise shouldTrigger = false
            Log.d("Prob: ", String.format("Yes : %f%%, No : %f%%", dist[0] * 100, dist[1] * 100));
            return dist[0] >= dist[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void initWekaCore() {
        //nominal attributes
        attributeList.add(new Attribute("time", Arrays.asList("daytime", "night", "midnight")));
        attributeList.add(new Attribute("weekday", Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")));
        attributeList.add(new Attribute("weather", Arrays.asList("clear", "rainny", "foggy")));
        attributeList.add(new Attribute("temperature", Arrays.asList("icy", "mild", "warm", "hot")));
        //numeric attributes
        attributeList.add(new Attribute("positionIdentity"));
        //classes
        attributeList.add(new Attribute("Class", Arrays.asList("yes", "no")));

        allAttributes = new ArrayList<>(Arrays.asList(attributeList.get(0), attributeList.get(1), attributeList.get(2), attributeList.get(3), attributeList.get(4), attributeList.get(5)));
        instanes = new Instances("TrainingSample", allAttributes, 0);
        instanes.setClass(attributeList.get(5));
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

    protected double[] testSample() throws Exception {
        String time;
        String weekday;
        String weather;
        String temperature;
        int positionIdentity;

        Instance testSample = new DenseInstance(allAttributes.size());
        testSample.setDataset(instanes);

        time = getTime();
        weekday = getWeekday();
        weather = getWeather();
        temperature = getTemperature();
        positionIdentity = getPositionIdentity();

        testSample.setValue(attributeList.get(0), time);
        testSample.setValue(attributeList.get(1), weekday);
        testSample.setValue(attributeList.get(2), weather);
        testSample.setValue(attributeList.get(3), temperature);
        testSample.setValue(attributeList.get(4), positionIdentity);

        return classifier.distributionForInstance(testSample);
    }

    private int getPositionIdentity() {
        HistroyData _h = new HistroyData();
        String positionGPSTuple = String.valueOf(this.contextDataModel.getLatitude()) + "," + String.valueOf(this.contextDataModel.getLongitude());

        //I only want confirm this positionIdentity, so other fields for me not important
        _h.setTime("foo")
            .setWeekday("bar")
            .setWeather("dummy")
            .setTemperature("fooBar")
            .setPositionGPSTuple(positionGPSTuple)
            .setPositionIdentity(UUID.randomUUID().hashCode())
            .setChosen("barFoo")
            .setCreateDate(UUID.randomUUID().getMostSignificantBits());


        HistroyData h = new HistroyDataDAO().doCompareHistroyData(_h);

        return h.getPositionIdentity();
    }

    protected void initDenseInstance() {

        List<HistroyData> histroyDataList = new HistroyDataDAO().getAllHistroyData();

        for (HistroyData h : histroyDataList) {
            Instance instance = new DenseInstance(allAttributes.size());
            instance.setDataset(instanes);

            instance.setValue(attributeList.get(0), h.getTime());
            instance.setValue(attributeList.get(1), h.getWeekday());
            instance.setValue(attributeList.get(2), h.getWeather());
            instance.setValue(attributeList.get(3), h.getTemperature());
            instance.setValue(attributeList.get(4), h.getPositionIdentity());
            instance.setValue(attributeList.get(5), h.getChosen());

            instanes.add(instance);
        }
    }

}
