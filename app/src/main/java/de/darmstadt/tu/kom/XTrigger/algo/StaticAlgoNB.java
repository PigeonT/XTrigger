package de.darmstadt.tu.kom.XTrigger.algo;

import android.util.Log;

import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;
import weka.classifiers.bayes.NaiveBayes;

public final class StaticAlgoNB extends StaticAlgo {

    private boolean shouldTrigger;

    public StaticAlgoNB(ContextDataModel contextDataModel) {
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

                return dist[0] >= dist[1];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    @Override
    protected void initClassfier() throws Exception {
        this.classifier = new NaiveBayes();
        classifier.buildClassifier(instanes);

    }
}