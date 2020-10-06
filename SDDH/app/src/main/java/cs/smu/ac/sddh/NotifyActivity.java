package cs.smu.ac.sddh;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cs.smu.ac.sddh.R;

public class NotifyActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    int N_Day, N_Hour, N_Minute;    //N_Day는 정확히 몇일전에 할것인지의 값, Day2는 인덱스값
    boolean N_Switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        //SharedPreference 로부터 값을 받아와 현재값 설정
        sharedPreferences = getSharedPreferences("Notification", MODE_PRIVATE);
        N_Day = sharedPreferences.getInt("N_Day", 1);
        N_Switch = sharedPreferences.getBoolean("N_Switch", true);
        N_Hour = sharedPreferences.getInt("N_Hour", 7);
        N_Minute = sharedPreferences.getInt("N_Minute", 0);

        //Spinner
        final Spinner dSpinner = (Spinner)findViewById(R.id.day_before);
        SpinnerAdapter dAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                (String[])getResources().getStringArray(R.array.my_array));
        // 스피너에 어답터를 연결 시켜 준다.
        dSpinner.setAdapter(dAdapter);

        dSpinner.setSelection(N_Day);
        dSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0: //당일
                        N_Day = 0;
                        break;
                    case 1: //1일전
                        N_Day = 1;
                        break;
                    case 2: //2일전
                        N_Day = 2;
                        break;
                    case 3: //3일전
                        N_Day = 3;
                        break;
                    default :
                        N_Day = 1;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                N_Day = 1;
            }
        });


        //TimePicker
        final TimePicker timePicker = findViewById(R.id.timePicker);
        timePicker.setHour(N_Hour);
        timePicker.setMinute(N_Minute);

        //switch val setting
        Switch NSwitch = findViewById(R.id.alarm_switch);
        NSwitch.setChecked(N_Switch);
        NSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    N_Switch = true;
                }else{
                    N_Switch = false;
                }
            }
        });

        //Button
        Button btn = (Button)findViewById(R.id.setAlarmTime);
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //적용버튼 Listener

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("N_Hour", timePicker.getHour());
                editor.putInt("N_Minute", timePicker.getMinute());
                editor.putInt("N_Day", N_Day);
                editor.putBoolean("N_Switch", N_Switch);
                editor.commit();

                Toast.makeText(getApplicationContext(), "적용되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}