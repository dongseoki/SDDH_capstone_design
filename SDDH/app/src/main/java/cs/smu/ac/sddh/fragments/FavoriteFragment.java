package cs.smu.ac.sddh.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cs.smu.ac.sddh.Adaptor_And_Item.FavoriteAdapter;
import cs.smu.ac.sddh.BookDetailActivity;
import cs.smu.ac.sddh.Services.BookSearchFromNaver;
import cs.smu.ac.sddh.R;
import cs.smu.ac.sddh.SQLite.FavoriteDBHelper;

public class FavoriteFragment extends Fragment {
    RecyclerView favoriteRecyclerview;
    FavoriteAdapter favoriteAdapter;
    ArrayList<BookSearchFromNaver.Book> bookArray;
    FavoriteDBHelper dbHelper;

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_favorite,container,false);
        //DB
        dbHelper = new FavoriteDBHelper(getActivity().getApplicationContext());
        RecentDataRefresh();

        favoriteRecyclerview=view.findViewById(R.id.favorite_recyclerview);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        favoriteRecyclerview.setLayoutManager(linearLayoutManager);
        favoriteRecyclerview.setNestedScrollingEnabled(true);
        favoriteAdapter=new FavoriteAdapter(bookArray);
        //favoriteAdapter.addItem(); -->창현,상균이 작성 부탁!!!!!(추후 작성)
        favoriteRecyclerview.setAdapter(favoriteAdapter);

        favoriteAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position, BookSearchFromNaver.Book item) {
                //Item Click Listener
                Intent nextActivity = new Intent(getActivity().getApplicationContext(), BookDetailActivity.class);
                nextActivity.putExtra("book", item);
                startActivity(nextActivity);
            }
        });

        return view;
    }

    public void RecentDataRefresh(){
        //SQLite Database Code
        Cursor cursor = dbHelper.getAllData();
        if(cursor == null){
            dbHelper.onCreate(dbHelper.getWritableDatabase());
            cursor = dbHelper.getAllData();
        }
        cursor.moveToLast();
        bookArray = new ArrayList<BookSearchFromNaver.Book>(cursor.getCount());
        boolean flags = true;
        if(cursor.getCount() == 0)  flags = false;
        while(flags){
            BookSearchFromNaver.Book value = new BookSearchFromNaver.Book(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getInt(5), cursor.getInt(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10)); //최근기록 데이터 역순으로 가져와서 value 에 저장됨
            bookArray.add(value);
            flags = cursor.moveToPrevious();
        }
    }
}
