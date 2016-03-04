package de.darmstadt.tu.kom.XTrigger.broadcastreceiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Calendar;
import java.util.UUID;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.config.Config;
import de.darmstadt.tu.kom.XTrigger.datajavadao.FeedbackDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.HistroyDataDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.ModiDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TrainingDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import de.darmstadt.tu.kom.XTrigger.datamodel.Feedback;
import de.darmstadt.tu.kom.XTrigger.datamodel.HistroyData;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public class NotificationServiceReceiver extends BroadcastReceiver {
    private ContextDataModel contextDataModel;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Config.getContext() == null) {
            Config.setContext(context);
        }

        String action = intent.getAction();
        Feedback feedback = new FeedbackDAO().get();
        boolean btnSwitch = new TrainingDAO().get();

        if (action.equals("cancel")) {
            //cancel
        } else {
            contextDataModel = (ContextDataModel) intent.getBundleExtra("contextModelBundle").get("contextModel");
            if (!btnSwitch) {
                notTrainingModel(action, feedback);
            } else {
                trainingModel(action, feedback);
            }
        }
        NotificationManager notification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification.cancel(Constants.NOTIFICATION_ID);

    }

    private void trainingModel(String action, Feedback feedback) {
        switch (action) {
            case "yes":
                trainingModelForYes(feedback);
                break;
            case "no":
                trainingModelForNo(feedback);
                break;
        }
    }

    private void notTrainingModel(String action, Feedback feedback) {
        switch (action) {
            case "yes":
                notTrainingModelForYes(feedback);
                break;
            case "no":
                notTrainingModelForNo(feedback);
                break;
        }

    }

    private void notTrainingModelForNo(Feedback feedback) {
        if (new ModiDAO().get() == R.id.modi_static) {
            Feedback.Interval noInterval;
            if (feedback != null && (noInterval = feedback.getInterval("No")) != null) {

                if (noInterval.getDefaultValue()) {
                    // if user choose yes, then trigger at least in 2 hours
                    Constants.SHARED_PREFERENCES.edit().putLong("notificationNo", System.currentTimeMillis() + 7200000L).apply();
                } else {
                    int delayMinutes = noInterval.getInterval();

                    Calendar delayTime = Calendar.getInstance();
                    delayTime.add(Calendar.MINUTE, delayMinutes);

                    Constants.SHARED_PREFERENCES.edit().putLong("notificationYes", delayTime.getTimeInMillis()).apply();
                }
            } else {
                Constants.SHARED_PREFERENCES.edit().putLong("notificationNo", System.currentTimeMillis() + 7200000L).apply();
            }
        } else if (new ModiDAO().get() == R.id.modi_dynamic) {
            trainingModelForNo(feedback);
        }
    }

    private void notTrainingModelForYes(Feedback feedback) {
        if (new ModiDAO().get() == R.id.modi_static) {

            Feedback.Interval yesInterval;
            if (feedback != null && (yesInterval = feedback.getInterval("Yes")) != null) {
                if (yesInterval.getDefaultValue()) {
                    // if user choose ye for default , then trigger at least tomorrow at 00:00
                    Calendar midNightOfToday = Calendar.getInstance();
                    midNightOfToday.set(Calendar.HOUR_OF_DAY, 24);
                    midNightOfToday.set(Calendar.MINUTE, 0);
                    midNightOfToday.set(Calendar.SECOND, 0);
                    midNightOfToday.set(Calendar.MILLISECOND, 0);

                    Constants.SHARED_PREFERENCES.edit().putLong("notificationYes", midNightOfToday.getTimeInMillis()).apply();
                } else {
                    int delayMinutes = yesInterval.getInterval();

                    Calendar delayTime = Calendar.getInstance();
                    delayTime.add(Calendar.MINUTE, delayMinutes);

                    Constants.SHARED_PREFERENCES.edit().putLong("notificationYes", delayTime.getTimeInMillis()).apply();
                }
            } else {
                Calendar midNightOfToday = Calendar.getInstance();
                midNightOfToday.set(Calendar.HOUR_OF_DAY, 24);
                midNightOfToday.set(Calendar.MINUTE, 0);
                midNightOfToday.set(Calendar.SECOND, 0);
                midNightOfToday.set(Calendar.MILLISECOND, 0);

                Constants.SHARED_PREFERENCES.edit().putLong("notificationYes", midNightOfToday.getTimeInMillis()).apply();
            }
        } else if (new ModiDAO().get() == R.id.modi_dynamic) {
            trainingModelForYes(feedback);
        }
    }

    private void trainingModelForYes(Feedback feedback) {

        HistroyData h = new HistroyData();

        String positionGPSTuple = String.valueOf(this.contextDataModel.getLatitude()) + "," + String.valueOf(this.contextDataModel.getLongitude());
        String time = getTime();
        String weekday = getWeekday();
        String weather = getWeather();
        String temperature = getTemperature();

        h.setTime(time)
                .setWeekday(weekday)
                .setWeather(weather)
                .setTemperature(temperature)
                .setPositionGPSTuple(positionGPSTuple)
                .setPositionIdentity(UUID.randomUUID().hashCode())
                .setChosen("yes")
                .setCreateDate(contextDataModel.getTime());


        new HistroyDataDAO().create(h);

    }

    private void trainingModelForNo(Feedback feedback) {
        HistroyData h = new HistroyData();

        String positionGPSTuple = String.valueOf(this.contextDataModel.getLatitude()) + "," + String.valueOf(this.contextDataModel.getLongitude());
        String time = getTime();
        String weekday = getWeekday();
        String weather = getWeather();
        String temperature = getTemperature();

        h.setTime(time)
                .setWeekday(weekday)
                .setWeather(weather)
                .setTemperature(temperature)
                .setPositionGPSTuple(positionGPSTuple)
                .setPositionIdentity(UUID.randomUUID().hashCode())
                .setChosen("no")
                .setCreateDate(contextDataModel.getTime());


        new HistroyDataDAO().create(h);
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
}
