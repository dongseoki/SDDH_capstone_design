package cs.smu.ac.sddh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import cs.smu.ac.sddh.Services.UnCatchTaskService;

public class SplashActivity extends AppCompatActivity {
    ImageView splashImageView;
    TextView splashTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        SharedPreferences sf = getSharedPreferences("settings",MODE_PRIVATE);
        int theme_mode = sf.getInt("theme", -1);
        AppCompatDelegate.setDefaultNightMode(theme_mode);

        // if (SQLite의 테마 설정값이 시스템을 따름)이면
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        // else if (SQLite의 테마 설정값이 라이트 테마)이면
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // else if (SQLite의 테마 설정값이 다크 테마)이면
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImageView=findViewById(R.id.splash_img);
        splashTextView=findViewById(R.id.splash_txt);
        splashAnimation();
        startService(new Intent(this, UnCatchTaskService.class));
    }
    @UiThread
    private void splashAnimation(){
        Animation textAnim= AnimationUtils.loadAnimation(this,R.anim.anim_splash_textview);
        splashTextView.startAnimation(textAnim);
        Animation imgAnim=AnimationUtils.loadAnimation(this,R.anim.anim_splash_imageview);
        splashImageView.startAnimation(imgAnim);
        imgAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                overridePendingTransition(R.anim.anim_splash_out_top,R.anim.anim_splash_in_down);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
