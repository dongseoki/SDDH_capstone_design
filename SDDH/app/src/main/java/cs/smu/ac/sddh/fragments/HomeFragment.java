package cs.smu.ac.sddh.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cs.smu.ac.sddh.Adaptor_And_Item.HomeFragmentAdapter;
import cs.smu.ac.sddh.Adaptor_And_Item.HomeInfo;
import cs.smu.ac.sddh.R;

public class HomeFragment extends Fragment {
    GridView homeView;
    HomeFragmentAdapter adapter;
    int[] images={R.drawable.daejin,R.drawable.dongduck,R.drawable.duksung_symbol,R.drawable.hansung,
    R.drawable.kookmin,R.drawable.kwangwoon,R.drawable.myungi,R.drawable.sangmyung,
    R.drawable.seokyung,R.drawable.seoul_woman,R.drawable.sungshin,R.drawable.samyook};
    String[] name={"대진대학교","동덕여자대학교","덕성여자대학교","한성대학교",
    "국민대학교","광운대학교","명지대학교","상명대학교",
    "서경대학교","서울여자대학교","성신여자대학교","삼육대학교"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        homeView=view.findViewById(R.id.home_view);
        adapter=new HomeFragmentAdapter();
        addData();
        homeView.setAdapter(adapter);
        homeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //추가적인 기능은 좀 더 생각해봐야할 듯
            //현재는 각 학교 홈페이지 접속가능
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeInfo homeInfo=(HomeInfo)adapter.getItem(position);
                switch (homeInfo.getUnivName()){
                    case "상명대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.smu.ac.kr/ko/index.do"));
                        startActivity(intent);
                        break;
                    }
                    case "명지대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.mju.ac.kr/sites/mjukr/intro/intro.html"));
                        startActivity(intent);
                        break;
                    }
                    case "대진대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.daejin.ac.kr/index_imsi.html"));
                        startActivity(intent);
                        break;
                    }
                    case "동덕여자대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.dongduk.ac.kr/kor/main.do"));
                        startActivity(intent);
                        break;
                    }
                    case "덕성여자대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.duksung.ac.kr/main.do?isMaster=N&isLogined=N&viewPrefix=%2FWEB-INF%2Fjsp" +
                                "%2Fcms&urlRootPath=&siteResourcePath=%2Fsite%2Fduksung"));
                        startActivity(intent);
                        break;
                    }
                    case "삼육대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.syu.ac.kr/eclass-introduction-home/?Go="));
                        startActivity(intent);
                        break;
                    }
                    case "성신여자대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.sungshin.ac.kr/sites/main_kor/main.jsp"));
                        startActivity(intent);
                        break;
                    }
                    case "서울여자대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.swu.ac.kr/index.do"));
                        startActivity(intent);
                        break;
                    }
                    case "국민대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.kookmin.ac.kr/home.php"));
                        startActivity(intent);
                        break;
                    }
                    case "서경대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.skuniv.ac.kr/"));
                        startActivity(intent);
                        break;
                    }
                    case "광운대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.kw.ac.kr/ko/"));
                        startActivity(intent);
                        break;
                    }
                    case "한성대학교":{
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.hansung.ac.kr/web/www/home"));
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        return view;
    }
    public void addData(){
        for (int i=0;i<12;i++){
            adapter.addItem(new HomeInfo(images[i],name[i]));
        }
    }
}
