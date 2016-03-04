package de.darmstadt.tu.kom.XTrigger.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.FeedbackDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.Feedback;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public class FeedbackSettingDialogFragment extends DialogFragment {

    private AlertDialog.Builder alertDialog;
    private View currentView;
    private EditText jaEditText;
    private EditText neinEditText;
    private CheckBox jaDefault;
    private CheckBox neinDefault;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity());

        int layoutId = getActivity().getResources().getIdentifier("dialog_feedback_setting", "layout", getActivity().getPackageName());

        currentView = getActivity().getLayoutInflater().inflate(layoutId, null);

        jaEditText = (EditText) currentView.findViewById(R.id.ja_interval_set_minutes);
        neinEditText = (EditText) currentView.findViewById(R.id.nein_interval_set_minutes);
        jaDefault = (CheckBox) currentView.findViewById(R.id.ja_interval_set_default);
        neinDefault = (CheckBox) currentView.findViewById(R.id.nein_interval_set_default);

        init(currentView);


        alertDialog.setView(currentView);
        alertDialog.setTitle("Feedback Einstellung");

        alertDialog.setPositiveButton(getString(android.R.string.ok), null);

        alertDialog.setNegativeButton(getString(android.R.string.cancel), null);

        return alertDialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Feedback feedback = new Feedback();

                    if (!jaDefault.isChecked()) {
                        String jaInterval = jaEditText.getText().toString().split("\n")[0].trim().replace(" ", "");
                        if (jaInterval.matches("\\d+")) {
                            int nJaInterval = Integer.parseInt(jaInterval);

                            if ((nJaInterval > 0 && nJaInterval < 1440)) {
                                Feedback.Interval yesInterval = new Feedback.Interval("Yes", false, nJaInterval);
                                feedback.addToIntervalHashmap(yesInterval);

                            } else {
                                Toast.makeText(getActivity(), "Eingabe ist ungültig, die Nummer muss positiv und kleiner als 1440 (24 * 60) sein!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else {
                            Toast.makeText(getActivity(), "Eingabe ist ungültig, da die Eingabe nummerisch sein muss!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        //default ja checked, only use default values
                        Feedback.Interval yesInterval = new Feedback.Interval("Yes", true, 0);
                        feedback.addToIntervalHashmap(yesInterval);
                    }
                    if (!neinDefault.isChecked()) {
                        String neinInterval = neinEditText.getText().toString().split("\n")[0].trim().replace(" ", "");

                        if (neinInterval.matches("\\d+")) {
                            int nNeinInterval = Integer.parseInt(neinInterval);

                            if ((nNeinInterval > 0 && nNeinInterval < 1440)) {
                                //save this minuten, trigger for nein after this minutes
                                Feedback.Interval noInterval = new Feedback.Interval("No", false, nNeinInterval);
                                feedback.addToIntervalHashmap(noInterval);
                            } else {
                                Toast.makeText(getActivity(), "Eingabe ist ungültig, die Nummer muss positiv und kleiner als 1440 (24 * 60) sein!", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } else {
                            Toast.makeText(getActivity(), "Eingabe ist ungültig, die Eingabe nummerisch sein muss!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        //default nein checked, only use default values
                        Feedback.Interval noInterval = new Feedback.Interval("No", true, 0);
                        feedback.addToIntervalHashmap(noInterval);
                    }
                    new FeedbackDAO().update(feedback);
                    dismiss();

                }
            });
        }
    }

    private void init(View currentView) {

        jaEditText.setSingleLine(true);
        neinEditText.setSingleLine(true);

        Feedback fb = new FeedbackDAO().get();
        if (fb != null) {

            //set yes interval initial values
            Feedback.Interval yesInterval = fb.getInterval("Yes");
            jaDefault.setChecked(yesInterval.getDefaultValue());
            if (yesInterval.getInterval() != 0) {
                jaEditText.setText(String.valueOf(yesInterval.getInterval()));
            }

            //set no interval initial values
            Feedback.Interval noInterval = fb.getInterval("No");
            neinDefault.setChecked(noInterval.getDefaultValue());
            if (noInterval.getInterval() != 0) {
                neinEditText.setText(String.valueOf(noInterval.getInterval()));
            }
        }
    }
}
