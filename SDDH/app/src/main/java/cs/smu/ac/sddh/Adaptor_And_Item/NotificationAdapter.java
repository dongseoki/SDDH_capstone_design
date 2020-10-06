package cs.smu.ac.sddh.Adaptor_And_Item;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import cs.smu.ac.sddh.Services.AlarmUtil;
import cs.smu.ac.sddh.R;
import cs.smu.ac.sddh.SQLite.AlarmDBHelper;

import static android.content.Context.MODE_PRIVATE;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
    ArrayList<NotificationInfo> NotiInfos= null;
    AlarmDBHelper dbHelper;


    public NotificationAdapter (ArrayList<NotificationInfo> notiInfo){ this.NotiInfos = notiInfo; }
    public class ViewHolder extends RecyclerView.ViewHolder{

        //protected ImageView book;
        protected TextView title;
        protected TextView bannap;
        protected TextView UnivTextview;
        protected Button deleteButton;
        protected int requestCode;
        protected String O_title;
        protected String O_Univ;
        protected String O_dday;

        public ViewHolder(View view){
            super(view);
            //this.book=view.findViewById(R.id.favorite_book);
            this.title=view.findViewById(R.id.notification_book_title);
            this.bannap=view.findViewById(R.id.notification_return_date);
            this.UnivTextview=view.findViewById(R.id.notification_Univ_name);
            this.deleteButton = (Button) view.findViewById(R.id.remove_btn) ;

            deleteButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmUtil alarmUtil = new AlarmUtil();
                    alarmUtil.releaseAlarm(view.getContext() , requestCode);
                    //Toast.makeText(view.getContext(), "알림 취소", Toast.LENGTH_SHORT).show();
                    Toast.makeText(view.getContext(), Integer.toString(requestCode), Toast.LENGTH_SHORT).show();
                    dbHelper = new AlarmDBHelper(view.getContext());

                    if (deleteButton.getText().equals("삭제 취소")){
                        dbHelper = new AlarmDBHelper(view.getContext());
                        dbHelper.insertData(requestCode, O_Univ, O_title, O_dday);

                        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("Notification", MODE_PRIVATE);

                        int[] DDAY = RDD_to_Int(O_dday.toString());
                        int N_Day = sharedPreferences.getInt("N_Day", 1);
                        int N_Hour = sharedPreferences.getInt("N_Hour", 7);
                        int N_Minute = sharedPreferences.getInt("N_Minute", 0);
                        //알림등록 버튼 리스너
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(DDAY[0], DDAY[1]-1, DDAY[2], N_Hour, N_Minute, 00);
                        calendar.add(Calendar.DATE, -1*N_Day);
                        alarmUtil.setAlarm(view.getContext(), calendar, requestCode, O_title, O_Univ, O_dday);
                        deleteButton.setText("삭제");
                    } else {
                        dbHelper.deleteData(new String[]{Integer.toString(requestCode)});
                        deleteButton.setText("삭제 취소");
                    }
                }
            }) ;


        }
    }
    public void addItem(NotificationInfo notificationInfo){
        NotiInfos.add(notificationInfo);
    }

    public void deleteItem(NotificationInfo notificationInfo){
        NotiInfos.remove(1);
    }
    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.notification_list, parent, false);
        return new NotificationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationInfo notiInfo  =NotiInfos.get(position);
        //holder.book.setImageResource(notiInfo.getBookImage());
        holder.title.setText("책 제목 : "+notiInfo.getTitle());
        holder.bannap.setText("학교 : " +notiInfo.getUniv());
        holder.requestCode = notiInfo.getRequestCode();
        holder.UnivTextview.setText("반납 예정일 : "+notiInfo.getdate());
        holder.requestCode = notiInfo.getRequestCode();
        holder.O_dday = notiInfo.getdate();
        holder.O_title = notiInfo.getTitle();
        holder.O_Univ = notiInfo.getUniv();
    }

    @Override
    public int getItemCount() {
        return NotiInfos.size();
    }

    private int[] RDD_to_Int(String RDD) {
        int[] ret = new int[3];
        String[] array = RDD.split("-");
        ret[0] = Integer.parseInt(array[0]);
        ret[1] = Integer.parseInt(array[1]);
        ret[2] = Integer.parseInt(array[2]);

        return ret;
    }
}