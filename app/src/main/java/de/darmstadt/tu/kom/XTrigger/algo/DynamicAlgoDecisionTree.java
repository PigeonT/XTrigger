package de.darmstadt.tu.kom.XTrigger.algo;

import android.util.Log;

import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;

public final class DynamicAlgoDecisionTree extends DynamicAlgo {

    public DynamicAlgoDecisionTree(ContextDataModel contextDataModel) {
        super(contextDataModel);
    }

    @Override
    protected void initClassfier() throws Exception {
        this.classifier = new J48();
        classifier.buildClassifier(instanes);
    }
}
