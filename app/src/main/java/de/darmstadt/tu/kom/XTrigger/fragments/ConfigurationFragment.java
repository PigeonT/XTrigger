package de.darmstadt.tu.kom.XTrigger.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;
import de.darmstadt.tu.kom.XTrigger.helper.Helper;


public class ConfigurationFragment extends ListFragment {

    private ListArrayAdapter mListArrayAdapter;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListArrayAdapter = new ListArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Constants.SETTING_ITEMS_LABEL);

        setListAdapter(mListArrayAdapter);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup currentView = (ViewGroup) inflater.inflate(R.layout.fragment_configuration, container, false);

        return currentView;

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        DialogFragment dialogFragment;

        Log.d("onitemselected", String.valueOf(position));

        switch(position) {
            case 0 :
                dialogFragment = Helper.DialogFactory(Constants.SETTING_CATEGORY.TIME);
                dialogFragment.show(getFragmentManager(), "timeDialog");
                break;
            case 1 :
                dialogFragment = Helper.DialogFactory(Constants.SETTING_CATEGORY.WEEKDAY);
                dialogFragment.show(getFragmentManager(), "weekdayDialog");
                break;
            case 2 :
                dialogFragment = Helper.DialogFactory(Constants.SETTING_CATEGORY.WEATHER);
                dialogFragment.show(getFragmentManager(), "weatherDialog");
                break;
            case 3 :
                dialogFragment = Helper.DialogFactory(Constants.SETTING_CATEGORY.TEMPERATURE);
                dialogFragment.show(getFragmentManager(), "temperatureDialog");
                break;
            case 4 :
                dialogFragment = Helper.DialogFactory(Constants.SETTING_CATEGORY.PLACE);
                dialogFragment.show(getFragmentManager(), "placeDialog");
                break;
            case 5:
                dialogFragment = Helper.DialogFactory(Constants.SETTING_CATEGORY.FEEDBACKSETTING);
                dialogFragment.show(getFragmentManager(), "feedbackSettingDialog");

                break;
            default:
                break;
        }

    }


    private class ListArrayAdapter extends ArrayAdapter<String> {

        public ListArrayAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

                View contentView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_row_item_for_static_parameter_setting, null);
                TextView label = (TextView) contentView.findViewById(R.id.label);
                label.setText(Constants.SETTING_ITEMS_LABEL[position]);
                return contentView;
        }
    }

}
