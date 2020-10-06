package cs.smu.ac.sddh.Adaptor_And_Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cs.smu.ac.sddh.Enum.ESchoolID;
import cs.smu.ac.sddh.R;

import static android.content.Context.MODE_PRIVATE;

//학교별 대출건수(책 상세정보 하단에 보일 정보)
public class BookSearchDetailAdapter extends BaseAdapter {
    ArrayList<BookSearchDetailSchoolInfo> infos=new ArrayList<>();
    LayoutInflater mLayoutInflater = null;
    Context context;
    public BookSearchDetailAdapter(Context context){
        this.context=context;
        mLayoutInflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void setItem(ArrayList<BookSearchDetailSchoolInfo> arrayList){
        this.infos=arrayList;
    }
    public void setItem(BookSearchDetailSchoolInfo bookSearchDetailSchoolInfo, int sid){
        this.infos.set(sid, bookSearchDetailSchoolInfo);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=mLayoutInflater.inflate(R.layout.activity_detail_list,null);
        ImageView schoolView=view.findViewById(R.id.school_image);
        TextView schoolName = view.findViewById(R.id.school_name);
        TextView distanceView=view.findViewById(R.id.distance);
        TextView borrowAbleCountView=view.findViewById(R.id.borrow_count);
        TextView searchCountView=view.findViewById(R.id.search_count);
        TextView borrowAble = view.findViewById(R.id.borrow_able);
        //컬러 설정
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",MODE_PRIVATE);
        int themeMode=sharedPreferences.getInt("theme",-1);// -1,1,2
        if(themeMode==1){
            schoolName.setTextColor(Color.BLACK);
            distanceView.setTextColor(Color.BLACK);
            searchCountView.setTextColor(Color.BLACK);
            borrowAbleCountView.setTextColor(Color.BLACK);
        }else if(themeMode==2){
            schoolName.setTextColor(Color.WHITE);
            distanceView.setTextColor(Color.WHITE);
            searchCountView.setTextColor(Color.WHITE);
            borrowAbleCountView.setTextColor(Color.WHITE);
        }
        ///
        //setting view

        ESchoolID eSchoolID = infos.get(position).schoolID;
        switch(eSchoolID){
            case sangmyung: //상명대학교
                schoolName.setText("상명대학교");
                break;
            case daejin: //대진대학교
                schoolName.setText("대진대학교");

                break;
            case dongduk: //동덕여자대학교
                schoolName.setText("동덕여자대학교");
                break;
            case duksung: //덕성여자대학교
                schoolName.setText("덕성여자대학교");
                break;
            case hansung:   //한성대학교
                schoolName.setText("한성대학교");
                break;
            case kookmin:   //국민대학교
                schoolName.setText("국민대학교");
                break;
            case gwangwoon: //광운대학교
                schoolName.setText("광운대학교");
                break;
            case myongji:   //명지대학교
                schoolName.setText("명지대학교");
                break;
            case seokyeong:  //서경대학교
                schoolName.setText("서경대학교");
                break;
            case seoulwoman:    //서울여자대학교
                schoolName.setText("서울여자대학교");
                break;
            case sungshin:  //성신여자대학교
                schoolName.setText("성신여자대학교");
                break;
            case sahmyook:    //삼육대학교
                schoolName.setText("삼육대학교");
                break;
        }

        schoolView.setImageResource(infos.get(position).schoolImage);
        String distance="거리 :"+infos.get(position).distance + "km";
        String borrowCount="대출가능건수: "+infos.get(position).borrowCount;
        String searchCount="검색건수: "+infos.get(position).searchCount;
        // STATE 상태 설명. -1 : 오류, 0 : 대출중 , 1 : 이용가능 , 2: 지정도서. -2 : 도서없음
        switch(infos.get(position).borrowAble){
            case 0:
                borrowAble.setText("대출중");
                borrowAble.setTextColor(Color.RED);
                break;
            case 1:
                borrowAble.setText("대출가능");
                borrowAble.setTextColor(Color.GREEN);
                break;
            case 2:
                borrowAble.setText("지정도서");
                borrowAble.setTextColor(Color.RED);
                break;
            case 3:
                borrowAble.setText("검색중");
                borrowAble.setTextColor(Color.rgb(223, 213, 0));
                break;
            case -1:
                borrowAble.setText("대출 불가");
                borrowAble.setTextColor(Color.RED);
                break;
            case -2:
                borrowAble.setText("도서 없음");
                borrowAble.setTextColor(Color.RED);
                break;
            default :
                borrowAble.setText("오류 : " + Integer.toString(infos.get(position).borrowAble));
                borrowAble.setTextColor(Color.RED);
                break;
        }
        distanceView.setText(distance);
        borrowAbleCountView.setText(borrowCount);
        searchCountView.setText(searchCount);
        return view;
    }
}
