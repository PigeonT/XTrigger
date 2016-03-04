package de.darmstadt.tu.kom.XTrigger.algo;

import android.util.Log;

import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import weka.classifiers.lazy.IBk;

public final class DynamicAlgoKNN extends DynamicAlgo {

    public DynamicAlgoKNN(ContextDataModel contextDataModel) {
        super(contextDataModel);
    }

    @Override
    protected void initClassfier() throws Exception {
        this.classifier = new IBk();
        classifier.buildClassifier(instanes);
    }
}
