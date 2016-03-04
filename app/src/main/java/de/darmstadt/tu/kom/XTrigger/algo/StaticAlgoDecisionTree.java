package de.darmstadt.tu.kom.XTrigger.algo;

import java.util.Calendar;

import de.darmstadt.tu.kom.XTrigger.datajavadao.TemperatureDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TimeDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeatherDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeekdayDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import de.darmstadt.tu.kom.XTrigger.datamodel.Temperature;
import de.darmstadt.tu.kom.XTrigger.datamodel.Time;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weather;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weekday;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public final class StaticAlgoDecisionTree extends StaticAlgo {

    private boolean shouldTrigger;

    public StaticAlgoDecisionTree(ContextDataModel contextDataModel) {
        super(contextDataModel);
    }

    @Override
    protected boolean algorithm() {

        shouldTrigger = checkTime()
                && checkWeekofDay()
                && checkWeather()
                && checkTemperature()
                && checkSmartphoneSpeed()
                && checkGPSDistance();

        return shouldTrigger;
    }

    private boolean checkSmartphoneSpeed() {
        //only when use is slient or very low speed meter/s
        float smartphoneSpeed = contextDataModel.getSmartphoneSpeed();

        return (smartphoneSpeed > -1) && (smartphoneSpeed < 15);
    }

    private boolean checkTemperature() {
        int temp = contextDataModel.getTemp();
        Temperature storeTemp = new TemperatureDAO().get();

        if (temp < 0) {
            return storeTemp != null && storeTemp.isIcy();
        } else if (temp >= 0 && temp < 15) {
            return storeTemp != null && storeTemp.isMild();
        } else if (temp >= 15 && temp < 25) {
            return storeTemp != null && storeTemp.isWarm();
        } else if (temp >= 25) {
            return storeTemp != null && storeTemp.isHot();

        } else {
            return false;
        }


    }

    private boolean checkWeather() {
        int weather = contextDataModel.getWeatherCategoryID();
        Weather storeWeather = new WeatherDAO().get();

//        ID	Meaning
//        951	calm
//        952	light breeze
//        953	gentle breeze
//        954	moderate breeze
//        955	fresh breeze

        if (weather == 951 || weather == 952 || weather == 953 || weather == 954 || weather == 955) {
            return storeWeather != null && storeWeather.isClear();
        } else {
            //see openweathermap api group
            // http://openweathermap.org/weather-conditions
            int _weather = Character.getNumericValue((String.valueOf(weather).charAt(0)));
            switch (_weather) {
                //clear or cloud, both group id is 8
                case 8:
                    return storeWeather != null && storeWeather.isClear();
                //rainy
                case 7:
                case 3:
                    return storeWeather != null && storeWeather.isFoggy();
                //snowy
                case 5:
                case 6:
                    return storeWeather != null && storeWeather.isRainny();
                default:
                    return false;
            }
        }
    }

    private boolean checkWeekofDay() {

        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(contextDataModel.getDayOfWeek());
        Weekday weekday = new WeekdayDAO().get();
        //int weekOfDay = calendar.get(Calendar.DAY_OF_WEEK);

        //sunday => 1
        //monday => 2
        //tuesday => 3
        //wednesday => 4
        //thursday => 5
        //friday => 6
        //saturday => 7
        switch (contextDataModel.getDayOfWeek()) {
            case 1:
                return weekday.isSunday();
            case 2:
                return weekday.isMonday();
            case 3:
                return weekday.isTuesday();
            case 4:
                return weekday.isWednesday();
            case 5:
                return weekday.isThursday();
            case 6:
                return weekday.isFriday();
            case 7:
                return weekday.isSaturday();
            default:
                return false;

        }
    }

    private boolean checkTime() {
        Time time = new TimeDAO().get();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(contextDataModel.getTime());

        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (currentHour >= 6 && currentHour < 17) {
            return time.isDayTime();
        } else if (currentHour >= 17 && currentHour < 21) {
            return time.isNight();
        } else if (currentHour >= 21 && currentHour < 24) {
            return time.isMidnight();
        } else if (currentHour >= 0 && currentHour < 6) {
            return time.isMidnight();
        } else {
            return false;
        }

    }

    @Override
    protected void initClassfier() throws Exception {

    }

}