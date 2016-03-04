package de.darmstadt.tu.kom.XTrigger.algo;

import android.util.Log;

import java.util.Calendar;
import java.util.UUID;

import de.darmstadt.tu.kom.XTrigger.datajavadao.HistroyDataDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import de.darmstadt.tu.kom.XTrigger.datamodel.HistroyData;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;

public final class DynamicAlgoNB extends DynamicAlgo {

    public DynamicAlgoNB(ContextDataModel contextDataModel) {
        super(contextDataModel);
    }

    @Override
    protected void initClassfier() throws Exception {
        this.classifier = new NaiveBayes();
        classifier.buildClassifier(instanes);
    }
}
