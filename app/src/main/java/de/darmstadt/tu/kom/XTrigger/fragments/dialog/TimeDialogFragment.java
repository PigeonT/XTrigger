package de.darmstadt.tu.kom.XTrigger.fragments.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.CheckBox;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TimeDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.Time;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public class TimeDialogFragment extends DialogFragment {

private AlertDialog.Builder alertDialog;
private View currentView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity());

        int layoutId = getActivity().getResources().getIdentifier("dialog_time", "layout", getActivity().getPackageName());

        currentView = getActivity().getLayoutInflater().inflate(layoutId,null);

        init(currentView);

        alertDialog.setView(currentView);
        alertDialog.setTitle("Zeit");

        addPlaceEventListener();

        return alertDialog.create();
    }

    private void init(View currentView) {
        Time time = new TimeDAO().get();

        CheckBox dayTime = (CheckBox) currentView.findViewById(R.id.daytime);
        CheckBox night = (CheckBox) currentView.findViewById(R.id.night);
        CheckBox midnight = (CheckBox) currentView.findViewById(R.id.midnight);

        if(time != null) {
            if(time.isDayTime()) {
                dayTime.setChecked(true);
            }
            if(time.isMidnight()) {
                midnight.setChecked(true);
            }
            if(time.isNight()) {
                night.setChecked(true);
            }
        }


    }


    private void addPlaceEventListener() {
        alertDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Time time = new Time();

                boolean dayTime = ((CheckBox) currentView.findViewById(R.id.daytime)).isChecked();
                boolean night = ((CheckBox) currentView.findViewById(R.id.night)).isChecked();
                boolean midnight = ((CheckBox) currentView.findViewById(R.id.midnight)).isChecked();

                time.setDayTime(dayTime);
                time.setNight(night);
                time.setMidnight(midnight);

                new TimeDAO().create(time);

            }
        });
        alertDialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}