package cs.smu.ac.sddh.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cs.smu.ac.sddh.BookSearchActivity;
import cs.smu.ac.sddh.Services.BookSearchFromNaver;
import cs.smu.ac.sddh.NetWorks.NetWorkStatus;
import cs.smu.ac.sddh.R;
import cs.smu.ac.sddh.Adaptor_And_Item.RecentSearchAdapter;
import cs.smu.ac.sddh.SQLite.SQLiteHelper;

public class SearchFragment extends Fragment {
    RecyclerView recentSearchRecyclerView = null;
    RecentSearchAdapter recentSearchAdapter = null;
    SQLiteHelper dbHelper;
    EditText searchEditText;
    ArrayList<String> recentSearchData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchEditText = getActivity().findViewById(R.id.search_text);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup view=(ViewGroup)inflater.inflate(R.layout.fragment_search,container,false);
        int Status= NetWorkStatus.getConnectivityStatus(getContext());
        if(Status==NetWorkStatus.NOT_CONNECTED){
            Toast.makeText(getContext(), "인터넷이 연결되지 않았습니다!!! 네트워크를 ON 하세요!", Toast.LENGTH_SHORT).show();
        }
        dbHelper = new SQLiteHelper(getActivity().getApplicationContext());
        RecentDataRefresh();
        //recentSearchData = getArguments().getStringArrayList("recentSearchData");         //기존 MainActivity로 부터 데이터를 받아왔으나 더이상 불필요
        recentSearchRecyclerView = (RecyclerView) view.findViewById(R.id.recent_search_recyclerview);
        recentSearchAdapter = new RecentSearchAdapter(recentSearchData);
        recentSearchRecyclerView.setAdapter(recentSearchAdapter);
        recentSearchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recentSearchAdapter.setOnItemClickListener(new RecentSearchAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position, String item) {
                //item 으로 해당 value 가져와짐
                searchEditText.setText(item);

                String searchValue = searchEditText.getText().toString();
                final BookSearchFromNaver naver = new BookSearchFromNaver(searchValue);
                new Thread(){
                    public void run(){
                        try{
                            naver.connect();
                            getActivity().runOnUiThread(new Runnable(){
                                public void run(){
                                    ArrayList<BookSearchFromNaver.Book> book = naver.getBookInstance();

                                    Intent intent = new Intent(view.getContext(), BookSearchActivity.class);
                                    intent.putExtra("Books", book);
                                    startActivity(intent);
                                }
                            });
                        }
                        catch(Exception e){

                        }
                    }
                }.start();


            }
        });


        Button btn = view.findViewById(R.id.Btn_DeleteRecentSearchData);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteAllData();
                refreshFragment();
            }
        });

        return view;
    }

    public void RecentDataRefresh(){
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

    public void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
