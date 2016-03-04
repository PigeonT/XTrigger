package de.darmstadt.tu.kom.XTrigger.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.AlgoDao;
import de.darmstadt.tu.kom.XTrigger.datajavadao.HistroyDataDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.ModiDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TrainingDAO;


public class SettingFragment extends Fragment {
    private RadioGroup modiRadioGroup;
    private Switch btnSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        btnSwitch = (Switch) currentView.findViewById(R.id.switch_btn);
        modiRadioGroup = (RadioGroup) currentView.findViewById(R.id.group_modi);
        RadioGroup algoRadioGroup = (RadioGroup) currentView.findViewById(R.id.group_algo);
        Button resetBtn = (Button) currentView.findViewById(R.id.btn_reset);

        init(modiRadioGroup);
        init(algoRadioGroup);
        init(btnSwitch);

        //reset button
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                new HistroyDataDAO().removeAll();
                                Toast.makeText(currentView.getContext(), "Alle Records gelöscht", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(currentView.getContext())
                        .setTitle("Records löschen")
                        .setMessage("Sind Sie sicher, alle Records zu löschen?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener);

                builder.show();


            }
        });

        return currentView;
    }

    private void init(RadioGroup radioGroup) {
        int id = radioGroup.getId();
        switch (id) {
            case R.id.group_algo:
                int algo = new AlgoDao().get();
                if (algo != 0) {
                    radioGroup.check(algo);
                }
                radioGroup.setOnCheckedChangeListener(new AlgoCheckedChangeListener());
                break;
            case R.id.group_modi:
                int modi = new ModiDAO().get();
                if (modi != 0) {
                    radioGroup.check(modi);
                }
                radioGroup.setOnCheckedChangeListener(new ModiCheckedChangeListener());
                break;
        }
    }

    private void init(final Switch btnSwitch) {
        boolean btnTraining = new TrainingDAO().get();
        btnSwitch.setChecked(btnTraining);

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (modiRadioGroup.getCheckedRadioButtonId() != R.id.modi_dynamic) {
                        Toast.makeText(getActivity(), "Training darf nur in dynamischer Modi sein", Toast.LENGTH_LONG).show();
                        btnSwitch.setChecked(false);
                    } else {
                        new TrainingDAO().update(true);
                    }
                } else {
                    new TrainingDAO().update(false);
                }
            }
        });
    }

    private class AlgoCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            new AlgoDao().update(checkedId);
        }
    }

    private class ModiCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.modi_static) {
                btnSwitch.setChecked(false);
            }
            if (checkedId == R.id.modi_dynamic) {
                btnSwitch.setChecked(true);
            }
            new ModiDAO().update(checkedId);
        }
    }
}
