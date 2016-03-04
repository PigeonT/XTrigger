package de.darmstadt.tu.kom.XTrigger.algo;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.darmstadt.tu.kom.XTrigger.datajavadao.PlaceDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TemperatureDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TimeDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeatherDAO;
import de.darmstadt.tu.kom.XTrigger.datajavadao.WeekdayDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import de.darmstadt.tu.kom.XTrigger.datamodel.Place;
import de.darmstadt.tu.kom.XTrigger.datamodel.Temperature;
import de.darmstadt.tu.kom.XTrigger.datamodel.Time;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weather;
import de.darmstadt.tu.kom.XTrigger.datamodel.Weekday;
import de.darmstadt.tu.kom.XTrigger.helper.Constants;
import de.darmstadt.tu.kom.XTrigger.helper.Helper;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public final class StaticAlgoKNN extends StaticAlgo {

    private boolean shouldTrigger;

    public StaticAlgoKNN(ContextDataModel contextDataModel) {
        super(contextDataModel);
    }

    @Override
    protected boolean algorithm() {
        double dist[];
        shouldTrigger = checkGPSDistance();
        if (!shouldTrigger) return false;
        else {
            //KNN algorithm
            initWekaCore();
            initDenseInstance();
            try {
                initClassfier();
                dist = testSample();
                // if yes probability > no probability, then shouldTrigger = true
                // otherwise shouldTrigger = false
                Log.d("Prob: ", String.format("Yes : %f%%, No : %f%%", dist[0] * 100, dist[1] * 100));
//                    Toast.makeText(Constants.CONTEXT, String.format("Yes : %f%%, No : %f%%", dist[0] * 100, dist[1] * 100), Toast.LENGTH_SHORT).show();
                return dist[0] >= dist[1];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }


    @Override
    protected void initClassfier() throws Exception {
        this.classifier = new IBk();
        classifier.buildClassifier(instanes);
    }

}
