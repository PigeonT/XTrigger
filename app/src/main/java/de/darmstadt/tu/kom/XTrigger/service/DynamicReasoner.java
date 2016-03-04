package de.darmstadt.tu.kom.XTrigger.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import de.darmstadt.tu.kom.XTrigger.R;
import de.darmstadt.tu.kom.XTrigger.algo.DynamicAlgoDecisionTree;
import de.darmstadt.tu.kom.XTrigger.algo.DynamicAlgoKNN;
import de.darmstadt.tu.kom.XTrigger.algo.DynamicAlgoNB;
import de.darmstadt.tu.kom.XTrigger.broadcastreceiver.AlgorithmServiceReceiver;
import de.darmstadt.tu.kom.XTrigger.datajavadao.AlgoDao;
import de.darmstadt.tu.kom.XTrigger.datajavadao.TrainingDAO;
import de.darmstadt.tu.kom.XTrigger.datamodel.ContextDataModel;

public class DynamicReasoner extends IntentService implements IReasoner{

    private int algo = 0;
    private Handler postResultHandler;
    private ContextDataModel contextDataModel;
    private boolean executeResult = true;

    public DynamicReasoner() {
        super("DynamicReasoner");
    }

    public DynamicReasoner(String name) {
        super(name);
        algo = new AlgoDao().get();
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        postResultHandler = AlgorithmServiceReceiver.getstartAlgorithmServiceReceiverHandler();
        contextDataModel = (ContextDataModel) intent.getSerializableExtra("ContextDataModel");
        algo = new AlgoDao().get();

        switch (algo) {
            case R.id.algo_nb:
                if(new TrainingDAO().get()) {
                    executeResult = true;
                }else{
                    executeResult = new DynamicAlgoNB(contextDataModel).execute();
                }
                break;
            case R.id.algo_knn:
                if(new TrainingDAO().get()) {
                    executeResult = true;
                }else{
                    executeResult = new DynamicAlgoKNN(contextDataModel).execute();
                }                break;
            case R.id.algo_decisiontree:
                if(new TrainingDAO().get()) {
                    executeResult = true;
                }else{
                    executeResult = new DynamicAlgoDecisionTree(contextDataModel).execute();
                }                break;
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
