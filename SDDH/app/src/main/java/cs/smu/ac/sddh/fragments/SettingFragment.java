package cs.smu.ac.sddh.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cs.smu.ac.sddh.ChoiceSchoolActivity;
import cs.smu.ac.sddh.NotifyActivity;
import cs.smu.ac.sddh.R;
import cs.smu.ac.sddh.ThemeActivity;
import cs.smu.ac.sddh.TutorialActivity;
import cs.smu.ac.sddh.VersionActivity;


public class SettingFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        context = container.getContext();

        TextView version = view.findViewById(R.id.version_info);
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VersionActivity.class);
                startActivity(intent);
            }
        });

        TextView noti =  view.findViewById(R.id.notify);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotifyActivity.class);
                startActivity(intent);
            }
        });



/*
        TextView cache =  view.findViewById(R.id.remove_cache);
        cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "cache", Toast.LENGTH_LONG).show();
            }
        });

        TextView profile =  view.findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "profile", Toast.LENGTH_LONG).show();
            }
        });
*/
        TextView univ =  view.findViewById(R.id.school_info);
        univ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChoiceSchoolActivity.class);
                startActivity(intent);
            }
        });

        TextView theme =  view.findViewById(R.id.select_theme);
        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ThemeActivity.class);
                startActivity(intent);
            }
        });

        TextView tutorial =  view.findViewById(R.id.reshow_tutorial);
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TutorialActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

}

