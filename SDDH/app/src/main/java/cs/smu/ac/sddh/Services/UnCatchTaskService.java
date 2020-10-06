package cs.smu.ac.sddh.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class UnCatchTaskService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        SharedPreferences homef = getSharedPreferences("frag",MODE_PRIVATE);
        SharedPreferences.Editor editor = homef.edit();
        editor.putInt("flag", 0);
        editor.commit();

        stopSelf();
    }
}
