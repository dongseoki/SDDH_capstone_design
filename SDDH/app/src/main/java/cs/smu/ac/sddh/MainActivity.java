package cs.smu.ac.sddh;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import cs.smu.ac.sddh.Adaptor_And_Item.RecentSearchAdapter;
import cs.smu.ac.sddh.NetWorks.NetWorkStatus;
import cs.smu.ac.sddh.SQLite.SQLiteHelper;
import cs.smu.ac.sddh.Services.BookSearchFromNaver;
import cs.smu.ac.sddh.fragments.SearchFragment;
import cs.smu.ac.sddh.fragments.HomeFragment;
import cs.smu.ac.sddh.fragments.AboutFragment;
import cs.smu.ac.sddh.fragments.FavoriteFragment;
import cs.smu.ac.sddh.fragments.NotiFragment;
import cs.smu.ac.sddh.fragments.SettingFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    SearchFragment searchFragment;
    RecyclerView recentSearchRecyclerView;
    RecentSearchAdapter recentSearchAdapter;
    ArrayList<BookSearchFromNaver.Book> book;
    ArrayList<String> recentSearchData;
    SQLiteHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
////네트워크 체크코드 작성
        int Staus= NetWorkStatus.getConnectivityStatus(getApplicationContext());
        if(Staus==NetWorkStatus.NOT_CONNECTED){
            Toast.makeText(this, "인터넷이 연결되지 않았습니다!!!", Toast.LENGTH_SHORT).show();
            //안드로이드 기기내 설정 페이지로 이동
            Intent intent=new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        }
        ///
        InitializeSharedPreference();
        SharedPreferences sf = getSharedPreferences("settings",MODE_PRIVATE);
        int theme_mode = sf.getInt("theme", -1);
        AppCompatDelegate.setDefaultNightMode(theme_mode);
        permissionCheck();
        // if (SQLite의 테마 설정값이 시스템을 따름)이면
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        // else if (SQLite의 테마 설정값이 라이트 테마)이면
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // else if (SQLite의 테마 설정값이 다크 테마)이면
        // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // 툴바 설정
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //뒤로가기 버튼 활성화
        getSupportActionBar().setDisplayShowTitleEnabled(false);    //타이틀제거
       // toolbar.setTitle("도서검색");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);   //뒤로가기 버튼을 사용자 설정 아이콘으로 변경
        //DrawerLayout 설정
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Search 부분 Fragment 설정
        final HomeFragment homeFragment = new HomeFragment();
        searchFragment =new SearchFragment();
        homefrag();
        final EditText searchEditText = (EditText) findViewById(R.id.search_text);
        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener(){   //상단 검색 텍스트 클릭 시
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                //
                if(hasFocus){
                    //Focus On => SearchFragment 로 적용 및 키보드 입력 활성화
                    RecentDataRefresh();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("recentSearchData", recentSearchData);
                    searchFragment.setArguments(bundle);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_frame, searchFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();



                }
                else{
                    //Focus Off => MainFragment 로 적용 및 키보드 입력 비활성화
                }
            }
        });
        searchEditText.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                final TextView test = (TextView)findViewById(R.id.search_ex);
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == 0){  //엔터 입력시
                    String searchValue = searchEditText.getText().toString();
                    if (searchEditText.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(),"키워드를 입력해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final BookSearchFromNaver naver = new BookSearchFromNaver(searchValue);
                        RecentDataRefresh();
                        int idx = recentSearchData.indexOf(searchValue);
                        if(idx != -1){}
                        else
                            dbHelper.insertData(searchValue);
                        searchFragment.refreshFragment();
                        new Thread() {
                            public void run() {
                                try {
                                    naver.connect();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            book = naver.getBookInstance();
                                            Intent intent = new Intent(getApplicationContext(), BookSearchActivity.class);
                                            intent.putExtra("Books", book);
                                            startActivity(intent);
                                        }
                                    });
                                } catch (Exception e) {

                                }
                            }
                        }.start();
                        return true;
                    }
                }
                else if(keyCode == KeyEvent.KEYCODE_SEARCH && event.getAction() == 0){
                    String searchValue = searchEditText.getText().toString();
                    if (searchEditText.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(),"키워드를 입력해주세요",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        final BookSearchFromNaver naver = new BookSearchFromNaver(searchValue);
                        RecentDataRefresh();
                        int idx = recentSearchData.indexOf(searchValue);
                        if(idx != -1){
                        }else
                            dbHelper.insertData(searchValue);
                        searchFragment.refreshFragment();
                        new Thread() {
                            public void run() {
                                try {
                                    naver.connect();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            book = naver.getBookInstance();
                                            searchEditText.setText(null);

                                            if (searchEditText.getText().toString().equals("")) {
                                                Toast.makeText(getApplicationContext(), "키워드를 입력해주세요", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Intent intent = new Intent(getApplicationContext(), BookSearchActivity.class);
                                                intent.putExtra("Books", book);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                                } catch (Exception e) {

                                }
                            }
                        }.start();
                        return true;
                    }
                }
                return false;
            }
        });

        // 튜토리얼 삽입 ~
        //        최초 실행 여부를 판단 ->>>
        SharedPreferences pref = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst", false);
        if(checkFirst==false){
            // 앱 최초 실행시 하고 싶은 작업
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("checkFirst",true);
            editor.commit();

            Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
            //finish();
        }else{
            // 최초 실행이 아닐때 진행할 작업
            
        }
        // ~ 튜토리얼 삽입

        //최근검색 기록 갱신
        dbHelper = new SQLiteHelper(MainActivity.this); //DB 초기설정
        RecentDataRefresh();



    }


    private long time= 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            EditText searchText = (EditText)findViewById(R.id.search_text);
            searchText.setText(null);
            searchText.clearFocus();

            // 두번 눌러서 종료하기 ~
            if(System.currentTimeMillis()-time>=2000){
                time=System.currentTimeMillis();
                Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
            }else if(System.currentTimeMillis()-time<2000) {

                SharedPreferences homef = getSharedPreferences("frag",MODE_PRIVATE);
                SharedPreferences.Editor editor = homef.edit();
                editor.putInt("flag", 0);
                editor.commit();
                finish();
            }
            // ~ 두번 눌러서 종료하기
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.menu_barcode_search:
                // User chose the "Settings" item, show the app settings UI...
                scanner();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment cur = null;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment deletedFrag = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        fragmentTransaction.remove(deletedFrag);

        EditText searchText = (EditText)findViewById(R.id.search_text);
        searchText.clearFocus();
        getSupportFragmentManager().popBackStackImmediate();


        int id = item.getItemId();
        if(id==R.id.nav_home){
            HomeFragment homeFragment=new HomeFragment();
            fragmentTransaction.replace(R.id.main_frame,homeFragment).commit();
        }
        else if (id == R.id.nav_favorite) {
            // Handle the camera action
            FavoriteFragment favoriteFragment=new FavoriteFragment();
            fragmentTransaction.replace(R.id.main_frame,favoriteFragment).commit();
        } else if (id == R.id.nav_notification) {
            NotiFragment notiFragment = new NotiFragment();
            fragmentTransaction.replace(R.id.main_frame, notiFragment).commit();
        } /*else if (id == R.id.nav_account) {
            AccountFragment accountFragment=new AccountFragment();
            fragmentTransaction.replace(R.id.main_frame,accountFragment).commit();
        } */else if (id == R.id.nav_setting) {
            SettingFragment settingfragment = new SettingFragment();
            fragmentTransaction.replace(R.id.main_frame, settingfragment).commit();
        } else if (id == R.id.nav_help) {
            AboutFragment aboutFragment = new AboutFragment();
            fragmentTransaction.replace(R.id.main_frame, aboutFragment).commit();
        } else if (id == R.id.nav_tutorial) {
            Intent intent=new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void scanner() {
        Intent intent = new Intent(MainActivity.this, BarcodeScannerActivity.class);
        startActivity(intent);
    }

    public void RecentDataRefresh(){
        final EditText searchEditText = (EditText)findViewById(R.id.search_text);


        //SQLite Database Code
        Cursor cursor = dbHelper.getAllData();
        cursor.moveToLast();

        recentSearchData = new ArrayList<String>(cursor.getCount());
        boolean flags = true;
        if(cursor.getCount() == 0)  flags = false;
        while(flags){
            String value = cursor.getString(1); //최근기록 데이터 역순으로 가져와서 value 에 저장됨
            recentSearchData.add(value);
            flags = cursor.moveToPrevious();
        }
    }

    public void InitializeSharedPreference(){
        //Notification SP Initializing
        SharedPreferences sharedPreferences = getSharedPreferences("Notification", MODE_PRIVATE);
        int N_Day = sharedPreferences.getInt("N_Day", 1);
        boolean N_Switch = sharedPreferences.getBoolean("N_Switch", true);
        int N_Hour = sharedPreferences.getInt("N_Hour", 7);
        int N_Minute = sharedPreferences.getInt("N_Minute", 0);
        int N_Count = sharedPreferences.getInt("N_Count", 0);
        SharedPreferences.Editor notificationEditor = sharedPreferences.edit();
        notificationEditor.putInt("N_Hour", N_Hour);
        notificationEditor.putInt("N_Minute", N_Minute);
        notificationEditor.putInt("N_Day", N_Day);
        notificationEditor.putBoolean("N_Switch", N_Switch);
        notificationEditor.putInt("N_Count", N_Count);
        notificationEditor.commit();
    }

    void permissionCheck() {
        //ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.CAMERA}, 100);
    }

    void homefrag() {
        SharedPreferences homef = getSharedPreferences("frag",MODE_PRIVATE);
        int flag = homef.getInt("flag", 0);

        if (flag == 1) return;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment=new HomeFragment();
        fragmentTransaction.replace(R.id.main_frame,homeFragment).commit();

        SharedPreferences.Editor editor = homef.edit();
        editor.putInt("flag", 1);
        editor.commit();
    }
}
