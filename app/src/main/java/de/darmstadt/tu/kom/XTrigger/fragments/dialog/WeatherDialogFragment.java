package de.darmstadt.tu.kom.XTrigger.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeatherDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weather;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public class WeatherDialogFragment extends android.support.v4.app.DialogFragment {
    private AlertDialog.Builder alertDialog;
    private View currentView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity());

        int layoutId = getActivity().getResources().getIdentifier("dialog_weather", "layout", getActivity().getPackageName());

        currentView = getActivity().getLayoutInflater().inflate(layoutId,null);

        init(currentView);

        alertDialog.setView(currentView);
        alertDialog.setTitle("Wetter");

        addPlaceEventListener();

        return alertDialog.create();
    }

    private void init(View currentView) {
        Weather temperature = new WeatherDAO().get();
        CheckBox clear = (CheckBox) currentView.findViewById(R.id.clear);
        CheckBox rainny = (CheckBox) currentView.findViewById(R.id.rainny);
        CheckBox foggy = (CheckBox) currentView.findViewById(R.id.foggy);

        if (temperature != null) {
            if (temperature.isClear()) {
                clear.setChecked(true);
            }
            if (temperature.isRainny()) {
                rainny.setChecked(true);
            }
            if (temperature.isFoggy()) {
                foggy.setChecked(true);
            }
        }
    }


    private void addPlaceEventListener() {
        alertDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Weather weather = new Weather();
                boolean clear = ((CheckBox) currentView.findViewById(R.id.clear)).isChecked();
                boolean foggy = ((CheckBox) currentView.findViewById(R.id.foggy)).isChecked();
                boolean rainny = ((CheckBox) currentView.findViewById(R.id.rainny)).isChecked();

                weather.setClear(clear).setFoggy(foggy).setRainny(rainny);

                new WeatherDAO().update(weather);
            }
        });
        alertDialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }


}
