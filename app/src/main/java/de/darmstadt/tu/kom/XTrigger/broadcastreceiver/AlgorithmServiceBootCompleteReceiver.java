package de.darmstadt.tu.kom.XTrigger.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.darmstadt.tu.kom.XTrigger.activity.MainActivity;
import de.darmstadt.tu.kom.XTrigger.activity.StatusActivity;
import de.darmstadt.tu.kom.XTrigger.config.Config;


public class AlgorithmServiceBootCompleteReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Config.getContext() == null) {
            Config.setContext(context);
        }

        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Log.i("start receiver", "start bootcomplete receiver");

        context.startActivity(i);
    }
}
