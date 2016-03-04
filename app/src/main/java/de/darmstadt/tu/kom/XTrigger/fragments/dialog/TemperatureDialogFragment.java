package de.darmstadt.tu.kom.XTrigger.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TemperatureDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.Temperature;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public class TemperatureDialogFragment extends android.support.v4.app.DialogFragment {
    private AlertDialog.Builder alertDialog;
    private View currentView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity());

        int layoutId = getActivity().getResources().getIdentifier("dialog_temperature", "layout", getActivity().getPackageName());

        currentView = getActivity().getLayoutInflater().inflate(layoutId, null);


        init(currentView);
        alertDialog.setView(currentView);
        alertDialog.setTitle("Temperatur");

        addPlaceEventListener();

        return alertDialog.create();
    }

    private void init(View currentView) {
        Temperature temperature = new TemperatureDAO().get();
        CheckBox icy = (CheckBox) currentView.findViewById(R.id.icy);
        CheckBox mild = (CheckBox) currentView.findViewById(R.id.mild);
        CheckBox warm = (CheckBox) currentView.findViewById(R.id.warm);
        CheckBox hot = (CheckBox) currentView.findViewById(R.id.hot);


        if (temperature != null) {
            if (temperature.isHot()) {
                hot.setChecked(true);
            }
            if (temperature.isIcy()) {
                icy.setChecked(true);
            }
            if (temperature.isWarm()) {
                warm.setChecked(true);
            }
            if (temperature.isMild()) {
                mild.setChecked(true);
            }
        }
    }


    private void addPlaceEventListener() {
        alertDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Temperature temperature = new Temperature();
                boolean icy = ((CheckBox) currentView.findViewById(R.id.icy)).isChecked();
                boolean mild = ((CheckBox) currentView.findViewById(R.id.mild)).isChecked();
                boolean warm = ((CheckBox) currentView.findViewById(R.id.warm)).isChecked();
                boolean hot = ((CheckBox) currentView.findViewById(R.id.hot)).isChecked();

                temperature.setIcy(icy).setMild(mild).setWarm(warm).setHot(hot);

                new TemperatureDAO().create(temperature);
            }
        });
        alertDialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
