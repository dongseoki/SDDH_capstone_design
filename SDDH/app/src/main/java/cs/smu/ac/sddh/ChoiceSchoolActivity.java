package cs.smu.ac.sddh;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChoiceSchoolActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private RadioButton btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11 ;
    private RadioGroup radioGroup;
    private int checked = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_choice_school);//학교선택 할 수 있는 액티비티

        sharedPreferences = getSharedPreferences("Univ", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        btn0 = (RadioButton) findViewById(R.id.select_kwangwoon);
        btn1 = (RadioButton) findViewById(R.id.select_kookmin);
        btn2 = (RadioButton) findViewById(R.id.select_daejin);
        btn3 = (RadioButton) findViewById(R.id.select_ducksung);
        btn4 = (RadioButton) findViewById(R.id.select_dongdeok);
        btn5 = (RadioButton) findViewById(R.id.select_myungi);
        btn6 = (RadioButton) findViewById(R.id.select_samyook);
        btn7 = (RadioButton) findViewById(R.id.select_sangmyung);
        btn8 = (RadioButton) findViewById(R.id.select_seokyung);
        btn9 = (RadioButton) findViewById(R.id.select_seoulwoman);
        btn10 = (RadioButton) findViewById(R.id.select_sungshin);
        btn11 = (RadioButton) findViewById(R.id.select_hansung);
        radioGroup = (RadioGroup) findViewById(R.id.univ_group);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == R.id.select_kwangwoon) {
                    checked = 0;
                } else if (checkedId == R.id.select_kookmin) {
                    checked = 1;
                } else if (checkedId == R.id.select_daejin) {
                    checked = 2;
                } else if (checkedId == R.id.select_ducksung) {
                    checked = 3;
                } else if (checkedId == R.id.select_dongdeok) {
                    checked = 4;
                } else if (checkedId == R.id.select_myungi) {
                    checked = 5;
                } else if (checkedId == R.id.select_samyook) {
                    checked = 6;
                } else if (checkedId == R.id.select_sangmyung) {
                    checked = 7;
                } else if (checkedId == R.id.select_seokyung) {
                    checked = 8;
                } else if (checkedId == R.id.select_seoulwoman) {
                    checked = 9;
                } else if (checkedId == R.id.select_sungshin) {
                    checked = 10;
                } else if (checkedId == R.id.select_hansung) {
                    checked = 11;
                }
            }
        });

        int univ = sharedPreferences.getInt("univ", -1);

        switch (univ) {
            case 0:
                btn0.setChecked(true);
                break;
            case 1:
                btn1.setChecked(true);
                break;
            case 2:
                btn2.setChecked(true);
                break;
            case 3:
                btn3.setChecked(true);
                break;
            case 4:
                btn4.setChecked(true);
                break;
            case 5:
                btn5.setChecked(true);
                break;
            case 6:
                btn6.setChecked(true);
                break;
            case 7:
                btn7.setChecked(true);
                break;
            case 8:
                btn8.setChecked(true);
                break;
            case 9:
                btn9.setChecked(true);
                break;
            case 10:
                btn10.setChecked(true);
                break;
            case 11:
                btn11.setChecked(true);
                break;
        }

        Button btn = findViewById(R.id.school_select_btn);
        SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
        int themeMode=sharedPreferences.getInt("theme",-1);// -1,1,2
        if(themeMode==2){//다크모드시 버튼 색깔 조정
            btn.setBackgroundColor(Color.DKGRAY);
            btn.setTextColor(Color.WHITE);
        }
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                editor.putInt("univ", checked);
                editor.commit();
                Toast.makeText(ChoiceSchoolActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
