package de.darmstadt.tu.kom.XTrigger.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.datajavadao.AlgoDao;
import de.darmstadt.tu.kom.XTrigger.datajavadao.HistroyDataDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.ModiDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TimeDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TrainingDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.Time;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        TextView textviewModi = (TextView) findViewById(R.id.modi_textview);
        TextView textViewTraining = (TextView) findViewById(R.id.training_textview);
        TextView textViewAlgo = (TextView) findViewById(R.id.algo_textview);
        TextView textViewRecordsNumber = (TextView) findViewById(R.id.records_textview);

        ListView listViewForStaticModi = (ListView) findViewById(R.id.static_parameter_listview);

//        Button backBtn = (Button) findViewById(R.id.btn_back);

        if (new ModiDAO().get() == R.id.modi_static) {
            textviewModi.setText("statisch");
        } else {
            textviewModi.setText("dynamisch");
        }

        switch (new AlgoDao().get()) {
            case R.id.algo_knn:
                textViewAlgo.setText("K-Nearest Neighbor");
                break;
            case R.id.algo_nb:
                textViewAlgo.setText("Naive Bayes");
                break;
            case R.id.algo_decisiontree:
                textViewAlgo.setText("Entscheidungsbaum");
                break;
            default:
                textViewAlgo.setText("false");
        }

        if (new TrainingDAO().get()) {
            textViewTraining.setText("Training Phase");
        } else {
            textViewTraining.setText("Not Training Phase");
        }

        if (new ModiDAO().get() != R.id.modi_static) {

            listViewForStaticModi.setVisibility(View.GONE);

        } else {

//            ArrayAdapter<String> listAdapter = new StaticParameterListAdapter(this, android.R.layout.simple_list_item_1, Constants.SETTING_ITEMS_LABEL);
//            String[] rest = new String[] {"1", "2", "3", "4", "5", "6"};
//            ArrayAdapter<String> a = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rest);
//
////            SimpleAdapter simpleAdapter = new SimpleAdapter(this, );

//            listViewForStaticModi.setAdapter(a);
        }

        textViewRecordsNumber.setText(String.valueOf(new HistroyDataDAO().getCount()));



//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                StatusActivity.this.finish();
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
