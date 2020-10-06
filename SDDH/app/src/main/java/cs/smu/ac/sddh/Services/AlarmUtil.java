package cs.smu.ac.sddh.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmUtil {

    // 알람 추가 메소드
    public void setAlarm(Context context, Calendar cal, int requestCode, String Title, String SchoolName, String Due_date){

        Intent mAlarmIntent = new Intent(context, AlarmBroadcastReceiver.class);
        mAlarmIntent.putExtra("TITLE", Title);
        mAlarmIntent.putExtra("DUE_DATE", Due_date);
        mAlarmIntent.putExtra("ID", requestCode);
        mAlarmIntent.putExtra("SCHOOL_NAME", SchoolName);

        PendingIntent alarmSender = PendingIntent.getBroadcast(context, requestCode, mAlarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),alarmSender);
        Toast.makeText(context, "알람이 등록되었습니다.", Toast.LENGTH_LONG).show();

        // FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 는 취소하고 새롭게 만든다


    }

    //알람 해제 메소드
    public void releaseAlarm(Context context, int requestCode) {
        AlarmManager fiveToHourAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent fiveIntent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent fiveSender = PendingIntent.getBroadcast(context, requestCode, fiveIntent, 0);
        fiveToHourAlarmManager.cancel(fiveSender);
        Log.d("NotiTEST", "AlarmUtil Canel");
    }
}