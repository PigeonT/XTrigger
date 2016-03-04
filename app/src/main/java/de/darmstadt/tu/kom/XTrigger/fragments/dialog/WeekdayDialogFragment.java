package de.darmstadt.tu.kom.XTrigger.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeekdayDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weekday;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public class WeekdayDialogFragment extends android.support.v4.app.DialogFragment {
    private AlertDialog.Builder alertDialog;
    private View currentView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity());

        int layoutId = getActivity().getResources().getIdentifier("dialog_weekday", "layout", getActivity().getPackageName());

        currentView = getActivity().getLayoutInflater().inflate(layoutId,null);

        init(currentView);

        alertDialog.setView(currentView);
        alertDialog.setTitle("Wochentag");

        addPlaceEventListener();

        return alertDialog.create();
    }

    private void init(View currentView) {
        CheckBox monday = (CheckBox) currentView.findViewById(R.id.monday);
        CheckBox tuesday = (CheckBox) currentView.findViewById(R.id.tuesday);
        CheckBox wednesday = (CheckBox) currentView.findViewById(R.id.wednesday);
        CheckBox thursday = (CheckBox) currentView.findViewById(R.id.thursday);
        CheckBox friday = (CheckBox) currentView.findViewById(R.id.friday);
        CheckBox saturday = (CheckBox) currentView.findViewById(R.id.saturday);
        CheckBox sunday = (CheckBox) currentView.findViewById(R.id.sunday);
        Weekday weekday = new WeekdayDAO().get();
        if(weekday != null) {
            if(weekday.isMonday()) monday.setChecked(true);
            if(weekday.isTuesday()) tuesday.setChecked(true);
            if(weekday.isWednesday()) wednesday.setChecked(true);
            if(weekday.isThursday()) thursday.setChecked(true);
            if(weekday.isFriday()) friday.setChecked(true);
            if(weekday.isSaturday()) saturday.setChecked(true);
            if(weekday.isSunday()) sunday.setChecked(true);
        }
    }


    private void addPlaceEventListener() {
        alertDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Weekday weekday = new Weekday();
                boolean monday = ((CheckBox) currentView.findViewById(R.id.monday)).isChecked();
                boolean tuesday = ((CheckBox) currentView.findViewById(R.id.tuesday)).isChecked();
                boolean wednesday = ((CheckBox) currentView.findViewById(R.id.wednesday)).isChecked();
                boolean thursday = ((CheckBox) currentView.findViewById(R.id.thursday)).isChecked();
                boolean friday = ((CheckBox) currentView.findViewById(R.id.friday)).isChecked();
                boolean saturday = ((CheckBox) currentView.findViewById(R.id.saturday)).isChecked();
                boolean sunday = ((CheckBox) currentView.findViewById(R.id.sunday)).isChecked();

                weekday.setIsSunday(sunday).setIsSaturday(saturday).setIsFriday(friday).setIsThursday(thursday).setIsWednesday(wednesday).setIsTuesday(tuesday).setIsMonday(monday);

                new WeekdayDAO().create(weekday);

            }
        });
        alertDialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

}
