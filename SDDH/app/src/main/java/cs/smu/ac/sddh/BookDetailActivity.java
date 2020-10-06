package cs.smu.ac.sddh;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import cs.smu.ac.sddh.Adaptor_And_Item.BookSearchDetailAdapter;
import cs.smu.ac.sddh.Adaptor_And_Item.BookSearchDetailSchoolInfo;
import cs.smu.ac.sddh.Adaptor_And_Item.LibraryLS;
import cs.smu.ac.sddh.Enum.ESchoolID;
import cs.smu.ac.sddh.SQLite.FavoriteDBHelper;
import cs.smu.ac.sddh.Services.BookSearchFromNaver;
import cs.smu.ac.sddh.Services.DistanceManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookDetailActivity extends AppCompatActivity {
    String ISBN;
    ArrayList<LibraryLS> libraryLoanList;
    Handler handler = new Handler();
    ImageView fav = null;
    int fav_flag = 0;
    ListView schoolListView;
    BookSearchDetailAdapter bookSearchDetailAdapter;
    ArrayList<BookSearchDetailSchoolInfo> arrayList = new ArrayList<>();
    double lat, lon;
    double[] dist;
    LocationManager lm;
    int[] indexList = new int[12];
    boolean[] isEnd = new boolean[12];
    int univCount;
	
    ArrayList<String> univNameList;
    ArrayList<Integer> resTimeList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);




        fav = (ImageView) findViewById(R.id.book_like);

        final Intent intent = getIntent();
        setResult(RESULT_OK, intent);
        final BookSearchFromNaver.Book book = (BookSearchFromNaver.Book) intent.getExtras().get("book");


        /// 즐겨찾기 버튼 관련 셋팅
        //pseudo/   sqlite database의 favorite 엔티티에서 book.ISBN을 검색한다.
        final FavoriteDBHelper dbHelper = new FavoriteDBHelper(getApplicationContext());
        Cursor cursor = dbHelper.getAllData();

        //dbHelper.deleteAllData();

        boolean flags;
        boolean searchISBN = false;

        if(cursor == null){ // cursor 가 null일 경우 데이터베이스 내 TABLE이 존재하지 않는 상태
            dbHelper.onCreate(dbHelper.getWritableDatabase());
        }
        else{
            flags = cursor.moveToFirst();
            while(flags){
                String val = cursor.getString(8); //8번 == ISBN
                if(val.equals(book.ISBN)){
                    searchISBN = true;
                    break;
                }
                flags = cursor.moveToNext();
            }
        }

        if(searchISBN){
            fav.setImageResource(R.drawable.like_selected);
            fav_flag = 1;
        }
        else{
            fav.setImageResource(R.drawable.like);
            fav_flag = 0;
        }
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav_flag == 0) { // 즐겨찾기에 없는 상태
                    //pseudo/   sqlite database의 favorite 엔티티에 book.ISBN을 추가한다.
                    boolean res = dbHelper.insertData(book);
                    fav_flag = 1;
                    fav.setImageResource(R.drawable.like_selected);
                }
                else {
                    //pseudo/   sqlite database의 favorite 엔티티에서 book.ISBN을 삭제한다.
                    String[] delData = {book.ISBN};
                    int res = dbHelper.deleteData(delData);
                    fav_flag = 0;
                    fav.setImageResource(R.drawable.like);
                }
            }
        });
        /// 여기까지

        //Content Setting
        final ImageView image = (ImageView)findViewById(R.id.activity_detail_image);
        TextView title = (TextView)findViewById(R.id.activity_detail_title);
        TextView author = (TextView)findViewById(R.id.activity_detail_author);
        TextView publisher = (TextView)findViewById(R.id.activity_detail_publisher);
        TextView pubdate = (TextView)findViewById(R.id.activity_detail_pubdate);
        TextView isbn = (TextView)findViewById(R.id.activity_detail_isbn);
        final TextView link = (TextView)findViewById(R.id.activity_detail_link);


        Glide.with(getApplicationContext()).load(book.image).into(image);
        title.setText(book.title);
        author.setText(book.author);
        publisher.setText(book.publisher);
        pubdate.setText(book.pubdate);
        isbn.setText(book.ISBN);
        link.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent internetIntent = new Intent(Intent.ACTION_VIEW);
                internetIntent.setData(Uri.parse(book.link));
                startActivity(internetIntent);
            }
        });


        libraryLoanList = new ArrayList<LibraryLS>();
        for(int i=0;i<12;i++){
            libraryLoanList.add(new LibraryLS(""+i));
        }
        // libraryLoanList 초기화.

        univNameList = new ArrayList<String>(
                Arrays.asList("광운", "국민", "대진", "덕성", "동덕", "명지", "삼육", "상명", "서경", "서울", "성신", "한성"));
        // nameList 초기화.

        resTimeList = new ArrayList<>();
        for (int i=0;i<12;i++){
            resTimeList.add(-1);
            //System.out.println(resTimeList.get(i));
        }
        // 응답시간 리스트 초기화




        //ListView
        ///상세정보 하단 부분 ListView 이미지 확인용 코드(이후 진짜 데이터로 수정 바람)
        //arrayList.add 할때 BookSearchDetailAdaptor.java 에서 borrowAble 확인하기
        schoolListView=findViewById(R.id.search_detail_about_school);
        bookSearchDetailAdapter=new BookSearchDetailAdapter(getApplicationContext());

        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.sangmyung, R.drawable.sangmyung,-1,0,0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.dongduk, R.drawable.dongduck, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.gwangwoon, R.drawable.kwangwoon, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.kookmin, R.drawable.kookmin, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.daejin, R.drawable.daejin, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.duksung, R.drawable.duksung_symbol, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.myongji, R.drawable.myungi, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.sahmyook, R.drawable.samyook, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.seokyeong, R.drawable.seokyung, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.seoulwoman, R.drawable.seoul_woman, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.sungshin, R.drawable.sungshin, -1, 0, 0, 3));
        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.hansung, R.drawable.hansung, -1, 0, 0, 3));


//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.kookmin, R.drawable.univ_kookmin, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.daejin, R.drawable.univ_daejin, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.seoulwoman, R.drawable.univ_swu, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.gwangwoon, R.drawable.univ_kwangwoon, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.duksung, R.drawable.univ_duksung, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.dongduk, R.drawable.univ_dongduk, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.myongji, R.drawable.univ_myungi, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.sangmyung, R.drawable.sangmyung,10,0,0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.sahmyook, R.drawable.univ_syu, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.seokyeong, R.drawable.univ_sku, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.sungshin, R.drawable.univ_sungshin, 10, 0, 0, 3));
//        arrayList.add(new BookSearchDetailSchoolInfo(ESchoolID.hansung, R.drawable.univ_hansung, 10, 0, 0, 3));
        //arrayList.add(new BookSearchDetailSchoolInfo(R.drawable.sangmyung,10,libraryLoanList.get(0).loanStatusList.size(),libraryLoanList.get(0).loanStatusList.size()));

        for(int i=0; i<12; ++i){
            ESchoolID eSchoolID = arrayList.get(i).getSchoolID();
            int val = eSchoolID.convertInt(arrayList.get(i).getSchoolID());
            indexList[val] = i;
        }

        bookSearchDetailAdapter.setItem(arrayList);
        schoolListView.setAdapter(bookSearchDetailAdapter);
        ///

        schoolListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

                ESchoolID schoolID = arrayList.get(position).getSchoolID();
                int schoolNum = schoolID.convertInt(schoolID);
                if(isEnd[schoolNum] == true){
                    //libraryLoanList
                    for(int i=0; i<libraryLoanList.size(); ++i){

                        if(libraryLoanList.get(i).sid.equals(Integer.toString(schoolNum))){
                            Intent intent = new Intent(getApplicationContext(), BookSchoolActivity.class);
                            intent.putExtra("loanStatus", libraryLoanList.get(i).loanStatusList);
                            intent.putExtra("schoolSID", libraryLoanList.get(i).sid);
                            intent.putExtra("TITLE",book.title);
                            startActivity(intent);
                            break;
                        }
                    }

                }
            }
        });

        // ex.
        //String ISBN = "9788988474839";
        //String booktitle = "데이터 분석 전문가 가이드";
        String ISBN = book.ISBN;
        String booktitle = book.title;

//        RequestThread thread = new RequestThread(ISBN, booktitle);
//        thread.start();
        univCount = 0;

        ArrayList<RequestThread> threadArrayList = new ArrayList<>();
        for(int i=0;i<12;i++){
            threadArrayList.add(new RequestThread(ISBN, booktitle, i));
        }
        for(int i=0;i<12;i++){
            threadArrayList.get(i).start();
        }

        /// GPS -
        /// 권한 확인
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 위치관리자 객체를 얻어온다


        // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                    0, // 통지사이의 최소 시간간격 (miliSecond)
                    0, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);

            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                    0, // 통지사이의 최소 시간간격 (miliSecond)
                    0, // 통지사이의 최소 변경거리 (m)
                    mLocationListener);
        }
        catch(SecurityException ex){}

        /// - GPS




        // 이 부분에서 thread.join 이런거 이용해서, thread가 끝나길 기다린 다음 다음 과정을 거치면 될 듯 하다.
        // thread 의 결과는 전역변수인 libraryLoanList에 담긴다.

    }
    class RequestThread extends  Thread {
        String paramUrl = "";
        int sid = -1;
        public RequestThread(String isbn, String booktitle)  {
            String encodedString ="";
            try{
                encodedString = URLEncoder.encode(booktitle, "UTF-8");
            }
            catch (Exception e){
                e.printStackTrace();
            }

            paramUrl +="?isbn=";
            paramUrl += isbn;
            paramUrl +="&title=";
            paramUrl += encodedString;
            //paramUrl += "&searchFlag=";
            //paramUrl += searchFlag;
        }
        public RequestThread(String isbn, String booktitle, int sid)  {
            String encodedString ="";
            try{
                encodedString = URLEncoder.encode(booktitle, "UTF-8");
            }
            catch (Exception e){
                e.printStackTrace();
            }

            paramUrl +="?isbn=";
            paramUrl += isbn;
            paramUrl +="&title=";
            paramUrl += encodedString;
            paramUrl +="&sid=";
            paramUrl += sid;
            this.sid = sid;
            //paramUrl += "&searchFlag=";
            //paramUrl += searchFlag;
        }

        public void run(){

            try {
                String urlStr = "https://----------------------/";
                long reqTime = System.currentTimeMillis();


                OkHttpClient client = new OkHttpClient.Builder()
                        .callTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(urlStr+paramUrl)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();
                Log.d("크롤링", "run: " + result);

                long resTime = System.currentTimeMillis();

                resTimeList.set(sid,(int)(resTime - reqTime)/1000);

                processResponse(result, sid);
                Log.d("크롤링 로그", "성공");
            } catch (Exception e){
                e.printStackTrace();
                Log.d("크롤링 로그", "실패");
            }

        }
    }



    public void processResponse(String response, int sid){
        Gson gson = new Gson();
        //출처: https://jekalmin.tistory.com/entry/Gson을-이용한-json을-객체에-담기 [jekalmin의 블로그]
        if (sid == -1){
            libraryLoanList = gson.fromJson(response, new TypeToken<ArrayList<LibraryLS>>(){}.getType());
        }
        else{
            libraryLoanList.set(sid, gson.fromJson(response, LibraryLS.class));
        }

//        if(libraryLoanList != null){
////            int countLoanStatus = libraryLoanList.size();
////
////            //println("size : " + countLoanStatus);
////            Log.d("크롤링 로그", "도서관 개수"+countLoanStatus);
////            int bookCount = 0;
////            for(int i=0;i <countLoanStatus;i++){
////
////                Log.d("크롤링 로그",""+i + "번 도서관 책 개수 : "+ libraryLoanList.get(i).loanStatusList.size());
////                bookCount += libraryLoanList.get(i).loanStatusList.size();
////                for (int j=0; j<libraryLoanList.get(i).loanStatusList.size();j++){
////                    Log.d("크롤링 로그", ""+ j + "번째 책 정보.");
////                    Log.d("크롤링 로그", "title: "+ libraryLoanList.get(i).loanStatusList.get(j).title);
////                    Log.d("크롤링 로그", "RN: "+ libraryLoanList.get(i).loanStatusList.get(j).RN);
////                    Log.d("크롤링 로그", "CN: "+ libraryLoanList.get(i).loanStatusList.get(j).CN);
////                    Log.d("크롤링 로그", "POS: "+libraryLoanList.get(i).loanStatusList.get(j).POS);
////                    Log.d("크롤링 로그","STATE: "+ libraryLoanList.get(i).loanStatusList.get(j).STATE);
////                    Log.d("크롤링 로그", "RDD: "+libraryLoanList.get(i).loanStatusList.get(j).RDD);
////                    Log.d("크롤링 로그", "BN: "+libraryLoanList.get(i).loanStatusList.get(j).BN);
////                    Log.d("크롤링 로그", "errorMessage: "+libraryLoanList.get(i).loanStatusList.get(j).errorMessage);
////                }
////
////            }
////            println("검색된 대출 개수 : "+ bookCount);
////        }
        changeSchoolListView(sid);
    }
    public void changeSchoolListView(final int sid){
        handler.post(new Runnable() {
            @Override
            public void run() {
                schoolListView=findViewById(R.id.search_detail_about_school);
                bookSearchDetailAdapter=new BookSearchDetailAdapter(getApplicationContext());

                int i = sid;
                int j = indexList[i];

                arrayList.get(j).setSearchCount(libraryLoanList.get(i).loanStatusList.size());
                if(arrayList.get(j).getSearchCount()> 0)
                    isEnd[i] = true;
                int tmpCnt = 0;
                for(int k=0; k<libraryLoanList.get(i).loanStatusList.size(); ++k){
                    if(libraryLoanList.get(i).loanStatusList.get(k).getSTATE() == 1)
                        ++tmpCnt;
                }
                arrayList.get(j).setBorrowCount(tmpCnt);
                if(libraryLoanList.get(i).loanStatusList.size() == 0)
                    arrayList.get(j).setBorrowAble(-2);   //도서없음
                else if(tmpCnt > 0)
                    arrayList.get(j).setBorrowAble(1);    //대출가능
                else
                    arrayList.get(j).setBorrowAble(-1);

                //arrayList.add(new BookSearchDetailSchoolInfo(0, R.drawable.sangmyung,10,0,0, 0));
                //arrayList.add(new BookSearchDetailSchoolInfo(R.drawable.sangmyung,10,libraryLoanList.get(0).loanStatusList.size(),libraryLoanList.get(0).loanStatusList.size()));
                bookSearchDetailAdapter.setItem(arrayList);
                //bookSearchDetailAdapter.setItem(arrayList.get(j-1), sid);
                schoolListView.setAdapter(bookSearchDetailAdapter);
                univCount++;
//                if(univCount >=12){
//                    String endMessage = "서동도협 도서 검색 완료!\n" +
//                            "학교별 검색 시간.\n";
//                    int sumTime = 0;
//                    for (int idx=0;idx<12;idx++){
//                        endMessage += univNameList.get(idx) + " : "+ resTimeList.get(idx) + "(초)\n"  ;
//                        sumTime += resTimeList.get(idx);
//                    }
//                    endMessage += "평균 검색 시간 : " +(sumTime / resTimeList.size()) + "(초)\n";
//
//                    Toast.makeText(getApplicationContext(), endMessage, Toast.LENGTH_LONG).show();
//                }
                // 대학교별 응답 시간 확인용 테스트 코드
            }
        });
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);

            DistanceManager DM = new DistanceManager(location.getLatitude(), location.getLongitude());
            dist = DM.distances();

            for (int i = 0; i < 12; i++) {
                arrayList.get(indexList[i]).setDistance((int)dist[i]);
            }
            refreshSchoolList();
            bookSearchDetailAdapter.setItem(arrayList);
            schoolListView.setAdapter(bookSearchDetailAdapter);
            lm.removeUpdates(mLocationListener);
        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    public void refreshSchoolList(){
        Collections.sort(arrayList);
        for(int i=0; i<12; ++i){
            ESchoolID eSchoolID = arrayList.get(i).getSchoolID();
            int val = eSchoolID.convertInt(arrayList.get(i).getSchoolID());
            indexList[val] = i;
        }
    }
}
