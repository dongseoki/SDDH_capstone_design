package cs.smu.ac.sddh;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import cs.smu.ac.sddh.Adaptor_And_Item.BookSchoolActivityListAdaptor;
import cs.smu.ac.sddh.Adaptor_And_Item.BookSchoolActivityListItem;
import cs.smu.ac.sddh.Adaptor_And_Item.LoanStatus;
import cs.smu.ac.sddh.SQLite.AlarmDBHelper;
import cs.smu.ac.sddh.Services.AlarmUtil;

//도서관련 학교 상세 화면
public class BookSchoolActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences2;
    ArrayList<LoanStatus> loanStatuses;
    String schoolSID;
    String title;
    ListView bookSchoolList;
    BookSchoolActivityListAdaptor adaptor;
    ArrayList<BookSchoolActivityListItem> listItems = new ArrayList<>();
    ImageView schoolImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);
        schoolImageView=findViewById(R.id.school_detail_image);//학교로고 이미지
        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        schoolSID = intent.getStringExtra("schoolSID");
        //        0	광운대학교
//        1	국민대학교
//        2	대진대학교
//        3	덕성여자대학교
//        4	동덕여자대학교
//        5	명지대학교
//        6	삼육대학교
//        7	상명대학교
//        8	서경대학교
//        9	서울여자대학교
//        10 성신여자대학교
//        11 한성대학교
        final int schoolNumber=Integer.parseInt(schoolSID);
        //Toast.makeText(this, ""+schoolNumber, Toast.LENGTH_SHORT).show();
        // 학교 번호 확인용 테스트 코드.
        
        //상단 학교로고 이미지 띄우기
        if(schoolNumber==0){
            schoolImageView.setImageResource(R.drawable.kwangwoon);
        }else if(schoolNumber==1){
            schoolImageView.setImageResource(R.drawable.kookmin);
        }else if(schoolNumber==2){
            schoolImageView.setImageResource(R.drawable.daejin);
        }else if(schoolNumber==3){
            schoolImageView.setImageResource(R.drawable.duksung_symbol);
        }else if(schoolNumber==4){
            schoolImageView.setImageResource(R.drawable.dongduck);
        }else if(schoolNumber==5){
            schoolImageView.setImageResource(R.drawable.myungi);
        }else if(schoolNumber==6){
            schoolImageView.setImageResource(R.drawable.samyook);
        }else if(schoolNumber==7){
            schoolImageView.setImageResource(R.drawable.sangmyung);
        }else if(schoolNumber==8){
            schoolImageView.setImageResource(R.drawable.seokyung);
        }else if(schoolNumber==9){
            schoolImageView.setImageResource(R.drawable.seoul_woman);
        }else if(schoolNumber==10){
            schoolImageView.setImageResource(R.drawable.sungshin);
        }else if(schoolNumber==11){
            schoolImageView.setImageResource(R.drawable.hansung);
        }
        loanStatuses = intent.getParcelableExtra("loanStatus");
        loanStatuses = (ArrayList<LoanStatus>)intent.getExtras().get("loanStatus");
        //loanStatuses에 도서 정보 담겨있음


        //ListView 설정
        bookSchoolList = findViewById(R.id.search_school_listView);
        adaptor = new BookSchoolActivityListAdaptor(getApplicationContext());
        for(int i=0; i<loanStatuses.size(); ++i){
            listItems.add(new BookSchoolActivityListItem(loanStatuses.get(i).getPOS(), loanStatuses.get(i).getRN(), loanStatuses.get(i).getCN(), Integer.toString(loanStatuses.get(i).getSTATE()), loanStatuses.get(i).RDD, Integer.toString(loanStatuses.get(i).getBN())));
        }
        adaptor.setItem(listItems);
        bookSchoolList.setAdapter(adaptor);

        //지도 Redirection
        ImageButton map = findViewById(R.id.show_map);
        map.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String data = "geo:0,0?q=" + getSchoolName(schoolNumber);
                    Intent mapIntent = new Intent();
                    mapIntent.setAction(Intent.ACTION_VIEW);
                    mapIntent.setData(Uri.parse(data));
                    startActivity(mapIntent);
                }
                catch(Exception e){
                    Toast.makeText(BookSchoolActivity.this, "지도 어플리케이션 업데이트가 필요합니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Button Listener 등록
        Button setAlarmButton = findViewById(R.id.add_notification_button);
        Button requestBookButton = findViewById(R.id.request_book_button);

        setAlarmButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //알림등록 버튼 리스너
                SharedPreferences sharedPreferences = getSharedPreferences("Notification", MODE_PRIVATE);
                int N_Count = sharedPreferences.getInt("N_Count", 0);   //최초의 초기화는 Main Activity 에서 사용
                int r_code = sharedPreferences.getInt("Request_Code", -1);
                if (getMinDueDate().equals("")) {
                    Toast.makeText(BookSchoolActivity.this, "   대출 가능한 책이 있습니다.\n알림을 등록할 필요가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    sharedPreferences = getSharedPreferences("Notification", MODE_PRIVATE);

                    int N_Day = sharedPreferences.getInt("N_Day", 1);
                    Boolean N_Switch = sharedPreferences.getBoolean("N_Switch", true);
                    int N_Hour = sharedPreferences.getInt("N_Hour", 7);
                    int N_Minute = sharedPreferences.getInt("N_Minute", 0);
                    //알림등록 버튼 리스너
                    Calendar calendar = Calendar.getInstance();
                    String[] array = getMinDueDate().split("-");
                    calendar.set(Integer.parseInt(array[0]), Integer.parseInt(array[1])-1, Integer.parseInt(array[2]), N_Hour, N_Minute, 00);
                    calendar.add(Calendar.DATE, -1*N_Day);
                    //calendar.set(Integer.parseInt(2020,05,06,07,00,00);

                    //TODO
                    AlarmDBHelper dbHelper = new AlarmDBHelper(getApplicationContext());
                    //dbHelper.insertData(LoanStatus loanStatus, int requestCode, String schoolName, String bookTitle);

                    Cursor cursor = dbHelper.getAllData();

                    if(cursor == null){ // cursor 가 null일 경우 데이터베이스 내 TABLE이 존재하지 않는 상태
                        dbHelper.onCreate(dbHelper.getWritableDatabase());
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Request_Code", ++r_code);
                    editor.commit();

                    boolean res = dbHelper.insertData(loanStatuses.get(getMinLoanStatus()), r_code, getSchoolName(schoolNumber),title);




                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String channelID = "BookDueDate";
                        CharSequence channelName = "반납 예정일 알림";
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
                        channel.setDescription("도서 대출 예정일을 기준으로 사용자가 지정한 일수만큼 일찍 알람을 보내줍니다.");
                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                    }

                    AlarmUtil alarmutil = new AlarmUtil();
                    alarmutil.setAlarm(BookSchoolActivity.this, calendar, r_code, title, getSchoolName(schoolNumber), getMinDueDate());

////////////////////////////tesst code
/*
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(2020, 5, 13, 23,15,00);
                alarmutil.setAlarm(BookSchoolActivity.this, calendar1, r_code, title, getSchoolName(schoolNumber), getMinDueDate());
*/
                }

            }
        });
        requestBookButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //도서 신청 버튼 리스너
                sharedPreferences2 = getSharedPreferences("Univ", MODE_PRIVATE);
                int univ = sharedPreferences2.getInt("univ", -1);
                Intent intent;
                switch(univ) {
                    case -1:
                        intent = new Intent(getApplicationContext(),ChoiceSchoolActivity.class);
                        startActivity(intent);//액티비티 띄우기
                        break;
                    case 0:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://kupis.kw.ac.kr/local/html/libraryOtherLib"));
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.kookmin.ac.kr/#/mylibrary/olv/result"));
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://library.daejin.ac.kr/perusal_guide.mir"));
                        startActivity(intent);
                        break;
                    case 3:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://discover.duksung.ac.kr/#/service/other-college/olv"));
                        startActivity(intent);
                        break;
                    case 4:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://library.dongduk.ac.kr/#/service/olv"));
                        startActivity(intent);
                        break;
                    case 5:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.mju.ac.kr/guide/Static.ax?page=Sub907."));
                        startActivity(intent);
                        break;
                    case 6:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.syu.ac.kr/otherlib/write"));
                        startActivity(intent);
                        break;
                    case 7:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.smu.ac.kr/Common?html=/Users/Smul/Docs/service_annexuse_info.cshtml"));
                        startActivity(intent);
                        break;
                    case 8:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://library.skuniv.ac.kr/guide/Static.ax?page=Interlib"));
                        startActivity(intent);
                        break;
                    case 9:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.swu.ac.kr/otherlib/write"));
                        startActivity(intent);
                        break;
                    case 10:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://lib.sungshin.ac.kr/htmlmanager/service/43"));
                        startActivity(intent);
                        break;
                    case 11:
                        intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://hsel.hansung.ac.kr/perusal_reading_room_guide.mir"));
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    String getSchoolName(int sid){
        String ret = "";
        switch(sid){
            case 0: ret = "광운대학교"; break;
            case 1: ret = "국민대학교"; break;
            case 2: ret = "대진대학교"; break;
            case 3: ret = "덕성대학교"; break;
            case 4: ret = "동덕여자대학교"; break;
            case 5: ret = "명지대학교"; break;
            case 6: ret = "삼육대학교"; break;
            case 7: ret = "상명대학교"; break;
            case 8: ret = "서경대학교"; break;
            case 9: ret = "서울여자대학교"; break;
            case 10: ret = "성신여자대학교"; break;
            case 11: ret = "한성대학교"; break;
        }
        return ret;
    }

    String getMinDueDate(){
        int minimum = 99999999;
        int index = 0;
        for(int i=0; i<loanStatuses.size(); ++i){
            if (minimum > RDD_to_Int(loanStatuses.get(i).RDD))
                minimum = RDD_to_Int(loanStatuses.get(i).RDD);
                index = i;
        }

        if (minimum == -1) return "";
        else return loanStatuses.get(index).RDD;
    }

    int getMinLoanStatus(){
        int minimum = 99999999;
        int index = 0;
        for(int i=0; i<loanStatuses.size(); ++i){
            if (minimum > RDD_to_Int(loanStatuses.get(i).RDD))
                minimum = RDD_to_Int(loanStatuses.get(i).RDD);
            index = i;
        }

        if (minimum == -1) return -1;
        else return index;
    }

    int RDD_to_Int(String RDD) {
        if (RDD.equals("")) return -1;
        int ret = 0;

        String[] array = RDD.split("-");
        String tmp;
        tmp = array[0] + array[1] + array[2];
        ret = Integer.parseInt(tmp);
        return ret;
    }

}
