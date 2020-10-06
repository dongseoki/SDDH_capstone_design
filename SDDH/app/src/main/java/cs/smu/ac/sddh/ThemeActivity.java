package cs.smu.ac.sddh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import cs.smu.ac.sddh.R;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import androidx.annotation.NonNull;

public class ThemeActivity extends AppCompatActivity {
    RadioButton m_rb1, m_rb2, m_rb3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        final RadioGroup rg = (RadioGroup)findViewById(R.id.theme_group);
        Button b = (Button)findViewById(R.id.apply_theme);

        m_rb1 = (RadioButton) findViewById(R.id.r_system_mode);
        m_rb2 = (RadioButton) findViewById(R.id.r_light_mode);
        m_rb3 = (RadioButton) findViewById(R.id.r_dark_mode);

        SharedPreferences sf = getSharedPreferences("settings",MODE_PRIVATE);
        int theme_mode = sf.getInt("theme", -1);

        if (theme_mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            m_rb1.setChecked(true);
        }
        else if (theme_mode == AppCompatDelegate.MODE_NIGHT_NO) {
            m_rb2.setChecked(true);
        }
        else if (theme_mode == AppCompatDelegate.MODE_NIGHT_YES) {
            m_rb3.setChecked(true);
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("settings",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int theme_mode = -1;

                int id = rg.getCheckedRadioButtonId();
                //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
                RadioButton rb = (RadioButton) findViewById(id);
                if (m_rb1.isChecked()) {
                    theme_mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                }
                else if (m_rb2.isChecked()) {
                    theme_mode = AppCompatDelegate.MODE_NIGHT_NO;
                }
                else if (m_rb3.isChecked()) {
                    theme_mode = AppCompatDelegate.MODE_NIGHT_YES;
                }
                editor.putInt("theme", theme_mode); // key, value를 이용하여 저장하는 형태
                editor.commit();

                SharedPreferences homef = getSharedPreferences("frag",MODE_PRIVATE);
                SharedPreferences.Editor editor2 = homef.edit();
                editor2.putInt("flag", 0);
                editor2.commit();


                finishAffinity();
                Intent intent = new Intent(ThemeActivity.this,  MainActivity.class);
                startActivity(intent);
                System.exit(0);
            } // end onClick()
        });  // end Listener


    }

}
