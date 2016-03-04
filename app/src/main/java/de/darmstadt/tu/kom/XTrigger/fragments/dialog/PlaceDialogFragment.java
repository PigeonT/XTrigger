package de.darmstadt.tu.kom.XTrigger.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.IDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.PlaceDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.Place;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;


public class PlaceDialogFragment extends DialogFragment {
    private AlertDialog.Builder alertDialog;
    private View currentView;
    private Handler getPositionHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        alertDialog = new AlertDialog.Builder(getActivity());

        int layoutId = getActivity().getResources().getIdentifier("dialog_place", "layout", getActivity().getPackageName());

        currentView = getActivity().getLayoutInflater().inflate(layoutId, null);

        CheckBox cb = (CheckBox) currentView.findViewById(R.id.on_the_way);

        init(currentView);

        alertDialog.setView(currentView);
        alertDialog.setTitle("Ort");

        addPlaceEventListener();


        return alertDialog.create();
    }

    private void init(View currentView) {
        Place place = new PlaceDAO().get();

        EditText firstEditText = (EditText) currentView.findViewById(R.id.first_place);
        EditText secondEditText = (EditText) currentView.findViewById(R.id.second_place);
        firstEditText.setSingleLine(true);
        secondEditText.setSingleLine(true);
        CheckBox cbOnTheWay = (CheckBox) currentView.findViewById(R.id.on_the_way);
        CheckBox cbTriggerSomewhereElse = (CheckBox) currentView.findViewById(R.id.trigger_in_other_places);
        CheckBox cbTriggerInTheseStreets = (CheckBox) currentView.findViewById(R.id.trigger_in_wroten_addresses);

        if (place != null) {

            if(place.isTriggerSomewhereElse()) {
                cbTriggerSomewhereElse.setChecked(true);
            }

            if(place.isTriggerInTheseStreets()) {
                cbTriggerInTheseStreets.setChecked(true);
            }

            if (place.isAllowedEverywhere()) {
                cbOnTheWay.setChecked(true);
                return;
            }

            Place.Address address;
            if (place.getAddressList().size() >= 1 && (address = place.getAddressList().get(0)) != null) {
                firstEditText.setText(address.getName());
            }
            if (place.getAddressList().size() >= 2 && (address = place.getAddressList().get(1)) != null) {
                secondEditText.setText(address.getName());
            }
        }else {
            cbTriggerSomewhereElse = (CheckBox) currentView.findViewById(R.id.trigger_in_other_places);
            cbTriggerInTheseStreets = (CheckBox) currentView.findViewById(R.id.trigger_in_wroten_addresses);
            cbTriggerInTheseStreets.setChecked(true);
            cbTriggerSomewhereElse.setChecked(false);
        }



    }


    private void addPlaceEventListener() {
        alertDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String firstAddress = ((EditText) currentView.findViewById(R.id.first_place)).getText().toString().split("\n")[0].trim();
                String secondAddress = ((EditText) currentView.findViewById(R.id.second_place)).getText().toString().split("\n")[0].trim();
                boolean isAllowedEverywhere = ((CheckBox) currentView.findViewById(R.id.on_the_way)).isChecked();
                boolean isTriggerSomewhereElse = ((CheckBox) currentView.findViewById(R.id.trigger_in_other_places)).isChecked();
                boolean isTriggerInTheseStreets = ((CheckBox) currentView.findViewById(R.id.trigger_in_wroten_addresses)).isChecked();

                final List<String> addressList = new ArrayList<>();

                if (!firstAddress.equals("")) {

                    addressList.add(firstAddress);
                }
                if (!secondAddress.equals("")) {
                    addressList.add(secondAddress);
                }

                HandlerThread handlerThread = new HandlerThread("ThreadForRetrievGPS");
                handlerThread.start();

                //fetch gps position from addresslist
                getPositionHandler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
                    Place place = new Place();

                    @Override
                    public boolean handleMessage(Message msg) {


                        //regardless of address, ""egal ort" checkbox is selected"
                        if (msg.what == -1) {
                            place.setAllowedEverywhere(true);
                            place.setTriggerInTheseStreets(false);
                            place.setTriggerSomewhereElse(false);
                            //insert and update is the same in codes, so it not matters, use insert() or update().
                            //update()
                            update();
                            return false;
                        }

                        if (msg.what == -2) {
                            delete();
                            return false;
                        }
                        Log.d("from addressList", addressList.toString());
                        List<String> _addressList = msg.getData().getStringArrayList("addressList");


                        if (_addressList != null) {
                            for (String address : _addressList) {
                                try {
                                    URL url = new URL(Constants.GOOGLE_MAP_URL + URLEncoder.encode(address, "UTF-8") + Constants.SENSOR_FALSE);
                                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                                    StringBuilder jsonResult = new StringBuilder();

                                    String line;
                                    while ((line = in.readLine()) != null) {
                                        jsonResult.append(line);
                                    }
                                    String json = jsonResult.toString();

                                    JSONObject jsonObject = new JSONObject(json);
                                    if (jsonObject.getString("status").equals("OK")) {

                                        Log.d("Lat" + address, jsonObject.getString("results"));

                                        JSONObject currentGPSPosition = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                                        Place.Address _address = new Place.Address();
                                        _address.setName(address);
                                        _address.setLatitude(Double.parseDouble(currentGPSPosition.getString("lat")));
                                        _address.setLongitude(Double.parseDouble(currentGPSPosition.getString("lng")));
                                        place.addtoAddressList(_address);
                                        place.setAllowedEverywhere(false);
                                        place.setTriggerInTheseStreets(msg.getData().getBoolean("isTriggerInTheseStreets"));
                                        place.setTriggerSomewhereElse(msg.getData().getBoolean("isTriggerSomewhereElse"));

                                        //insert and update is the same in codes, so it not matters, use insert() or update().
                                        //update()
                                        update();
                                        Log.d("current thread: ", String.valueOf(Looper.myLooper() == Looper.getMainLooper()));
                                        Log.d("lat and lng", currentGPSPosition.getString("lat"));
                                    }

                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                    Log.e("MalformedURLException", e.getMessage());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.e("IOException", e.getMessage());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        return false;
                    }


                    private void delete() {
                        IDAO placeDao = new PlaceDAO();
                        ((PlaceDAO) placeDao).delete(place);
                    }

                    private void insert() {
                        IDAO placeDao = new PlaceDAO();
                        ((PlaceDAO) placeDao).create(place);
                    }

                    private void update() {
                        IDAO placeDao = new PlaceDAO();
                        ((PlaceDAO) placeDao).update(place);
                    }
                });

                Bundle bundle = new Bundle();
                bundle.putBoolean("isTriggerSomewhereElse", isTriggerSomewhereElse);
                bundle.putBoolean("isTriggerInTheseStreets", isTriggerInTheseStreets);
                if (addressList.size() != 0 && !isAllowedEverywhere) {

                    bundle.putStringArrayList("addressList", (ArrayList<String>) addressList);
                    Message mMessage = new Message();
                    mMessage.setData(bundle);
                    getPositionHandler.sendMessage(mMessage);
                } else if (isAllowedEverywhere) {
                    //-1 means user select "egal ort checkbox"
                    getPositionHandler.sendEmptyMessage(-1);
                } else {
                    //-2 means delete this place
                    getPositionHandler.sendEmptyMessage(-2);
                }

            }
        });

        alertDialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cancel all settings
            }
        });
    }

}
