package cs.smu.ac.sddh.Services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import cs.smu.ac.sddh.R;
import cs.smu.ac.sddh.SQLite.AlarmDBHelper;

import static android.content.Context.MODE_PRIVATE;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        String Title = intent.getExtras().getString("TITLE");
        String Due_date = intent.getExtras().getString("DUE_DATE");
        String UNIV = intent.getExtras().getString("SCHOOL_NAME");
        int ID = intent.getExtras().getInt("ID");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "BookDueDate")
                .setSmallIcon(R.drawable.library)
                .setContentTitle("\""+Title+"\" 반납 예정")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("도서 \""+Title+"\" 이/가 "+Due_date+"에 "+UNIV+"에서 반납 예정입니다."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences("Notification", MODE_PRIVATE);
        boolean n_switch = sharedPreferences.getBoolean("N_Switch", true);
        if (n_switch == true) {
            notificationManager.notify(ID, builder.build());
        }
        AlarmDBHelper dbHelper;
        dbHelper = new AlarmDBHelper(context);
        dbHelper.deleteData(new String[]{Integer.toString(ID)});

    }
}