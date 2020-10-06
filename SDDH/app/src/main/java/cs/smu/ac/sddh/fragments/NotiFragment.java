package cs.smu.ac.sddh.fragments;

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

import cs.smu.ac.sddh.Adaptor_And_Item.NotificationAdapter;
import cs.smu.ac.sddh.Adaptor_And_Item.NotificationInfo;
import cs.smu.ac.sddh.R;
import cs.smu.ac.sddh.SQLite.AlarmDBHelper;

public class NotiFragment extends Fragment {

    ArrayList<NotificationInfo> notiArray;
    RecyclerView notiRecyclerview;
    NotificationAdapter notiAdapter;
    AlarmDBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view=(ViewGroup)inflater.inflate(R.layout.fragment_notification,container,false);

        dbHelper = new AlarmDBHelper(getActivity().getApplicationContext());
        RecentDataRefresh();
        notiRecyclerview=view.findViewById(R.id.notification_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        notiRecyclerview.setLayoutManager(linearLayoutManager);
        notiRecyclerview.setNestedScrollingEnabled(true);
        notiAdapter=new NotificationAdapter(notiArray);
        //favoriteAdapter.addItem(); -->창현,상균이 작성 부탁!!!!!(추후 작성)
        notiRecyclerview.setAdapter(notiAdapter);
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
        notiArray = new ArrayList<NotificationInfo>(cursor.getCount());
        boolean flags = true;
        if(cursor.getCount() == 0)  flags = false;
        while(flags){
            // request code, title, bannap
            NotificationInfo value = new NotificationInfo(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            notiArray.add(value);
            flags = cursor.moveToPrevious();
        }
    }
}