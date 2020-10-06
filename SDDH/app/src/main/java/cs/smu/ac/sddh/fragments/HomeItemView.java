package cs.smu.ac.sddh.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cs.smu.ac.sddh.R;

public class HomeItemView extends LinearLayout {
    ImageView univImage;
    TextView univName;

    public HomeItemView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.fragment_home_list, this, true);
        univImage = findViewById(R.id.univ_image);
        univName = findViewById(R.id.univ_name);
    }

    public void setUnivImage(int image) {
        univImage.setImageResource(image);
    }
    public void setUnivName(String name){
        univName.setText(name);
    }
}
