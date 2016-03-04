package de.darmstadt.tu.kom.XTrigger.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.AlgoDao;
import de.darmstadt.tu.kom.XTrigger.datajavadao.FeedbackDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.ModiDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.PlaceDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TemperatureDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TimeDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TrainingDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeatherDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeekdayDAO;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public class HomeFragment extends Fragment{
    private AlarmManager alarmManger;
    private PendingIntent pendingIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup currentView = (ViewGroup) inflater.inflate(
                R.layout.fragment_home, container, false);

        Button startServiceButton = (Button) currentView.findViewById(R.id.start_service);
        Button stopServiceButton = (Button) currentView.findViewById(R.id.stop_service);

//        Constants.SHARED_PREFERENCES.edit().clear().apply();

        pendingIntent = Constants.START_ALGORITHM_PENDING_INTENT;
        alarmManger = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        startServiceButton.setOnClickListener(new StartServiceListener());
        stopServiceButton.setOnClickListener(new StopServiceListener());

        Toast.makeText(getActivity(), "Bitte drücken Sie nochmal auf \"Starten\", wenn Sie gerade ihr Smartphone neu gestartet haben.",  Toast.LENGTH_LONG).show();

        return currentView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }



    private class StartServiceListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            //check all configuration right written
            if(new ModiDAO().get() == 0 || new AlgoDao().get()  == 0) {
                Toast.makeText(getActivity(), "Stellen Sie bitte die gewünschten Modi und Algorithmen ein!",  Toast.LENGTH_SHORT).show();
                return;
            }

            if(new ModiDAO().get() == R.id.modi_static) {
                if(someVariablerIsNull()) {
                    Toast.makeText(getActivity(), "Stellen Sie bitte jede Variable ein!",  Toast.LENGTH_SHORT).show();
                    return;
                }

            }


            if(pendingIntent != null) {
                Log.i("alarmmanager", "alarmmanager not null, cancel all alarmmanagers firstly");
                alarmManger.cancel(pendingIntent);
            }
            Toast.makeText(getActivity(), "XTrigger starten",  Toast.LENGTH_SHORT).show();

            Log.d("start", "start alarm");
            LocationManager  locationManager = (LocationManager) Constants.CONTEXT.getSystemService(Context.LOCATION_SERVICE);

            boolean GPSisEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!GPSisEnabled || !networkIsEnabled){
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, Constants.REQUEST_CODE_FOR_ENABLE_LOCATION_SERVICE);

            }else {
//                Intent i = new Intent(getActivity(), AlgorithmServiceReceiver.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("algorithm", "DecisionTreeService");
//                i.putExtras(bundle);
//                pendingIntent = PendingIntent.getBroadcast(getActivity(), Constants.PENDING_INTENT_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);

                if(new TrainingDAO().get()) {
                    alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.TRIGGER_INTERVAL_TRAINING, pendingIntent);
                }else{

                    alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.TRIGGER_INTERVAL, pendingIntent);
                }
               // alarmManger.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

            }

        }

        private boolean someVariablerIsNull() {

            if(new TimeDAO().get() == null) return true;
            if(new WeekdayDAO().get() == null) return true;
            if(new TemperatureDAO().get() == null) return true;
            if(new WeatherDAO().get() == null) return true;
            if(new PlaceDAO().get() == null) return true;
            if(new FeedbackDAO().get() == null) return true;

            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.REQUEST_CODE_FOR_ENABLE_LOCATION_SERVICE) {
            //Log.d("GPS", String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
            //Log.d("Network", String.valueOf(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));

//            Intent i = new Intent(getActivity(), AlgorithmServiceReceiver.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("algorithm", "DecisionTreeService");
//            i.putExtras(bundle);
//            pendingIntent = PendingIntent.getBroadcast(getActivity(), Constants.PENDING_INTENT_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManger.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
            if(new TrainingDAO().get()) {
                alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.TRIGGER_INTERVAL_TRAINING, pendingIntent);
            }else{

                alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.TRIGGER_INTERVAL, pendingIntent);
            }


        }
    }

    private class StopServiceListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d("stop", "stop alarm");
            Toast.makeText(getActivity(), "XTrigger stoppen",  Toast.LENGTH_SHORT).show();

            if(pendingIntent != null) {
                Log.i("alarmmanager", "not null");
                alarmManger.cancel(pendingIntent);
            }
        }
    }

}
