package de.darmstadt.tu.kom.XTrigger.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.algo.StaticAlgoDecisionTree;
import de.darmstadt.tu.kom.XTrigger.algo.StaticAlgoKNN;
import de.darmstadt.tu.kom.XTrigger.algo.StaticAlgoNB;
import de.darmstadt.tu.kom.XTrigger.broadcastreceiver.AlgorithmServiceReceiver;
import de.darmstadt.tu.kom.XTrigger.datajavadao.AlgoDao;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;


public class StaticReasoner extends IntentService implements IReasoner{

    private int algo = 0;
    private Handler postResultHandler;
    private ContextDataModel contextDataModel;
    private boolean executeResult = false;


    public StaticReasoner() {
        super("StaticReasoner");
    }

    public StaticReasoner(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        postResultHandler = AlgorithmServiceReceiver.getstartAlgorithmServiceReceiverHandler();
        contextDataModel = (ContextDataModel) intent.getSerializableExtra("ContextDataModel");
        algo = new AlgoDao().get();

        boolean shouldTrigger;

        switch (algo) {
            case R.id.algo_decisiontree:
                //decision tree
                executeResult = new StaticAlgoDecisionTree(contextDataModel).execute();
                break;
            case R.id.algo_knn:
                //k-nearest neighbors
                executeResult = new StaticAlgoKNN(contextDataModel).execute();
                break;
            case R.id.algo_nb:
                //naive bayes
                executeResult = new StaticAlgoNB(contextDataModel).execute();
                break;
        }

        if (executeResult) {
            postResult(postResultHandler, "success");
        } else {
            postResult(postResultHandler, "failed");
        }
    }

    private void postResult(Handler postResultHandler, String result) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        bundle.putSerializable("contextModel", contextDataModel);
        message.setData(bundle);
        postResultHandler.sendMessage(message);
    }

}
