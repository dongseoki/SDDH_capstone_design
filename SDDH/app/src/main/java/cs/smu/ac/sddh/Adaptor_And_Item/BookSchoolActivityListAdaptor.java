package cs.smu.ac.sddh.Adaptor_And_Item;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs.smu.ac.sddh.R;

import static android.content.Context.MODE_PRIVATE;

//책 상세정보 하단에서 학교이미지 클릭시 화면
public class BookSchoolActivityListAdaptor extends BaseAdapter {
    ArrayList<BookSchoolActivityListItem> infos=new ArrayList<>();
    LayoutInflater mLayoutInflater = null;
    Context context;

    public BookSchoolActivityListAdaptor(Context context){
        this.context=context;
        mLayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return infos.size(); }

    @Override
    public Object getItem(int position) { return infos.get(position); }
    public void setItem(ArrayList<BookSchoolActivityListItem> array){ infos = array; }
    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=mLayoutInflater.inflate(R.layout.activity_school_list,null);
        TextView bookLocation = view.findViewById(R.id.book_location);
        TextView bookInfo = view.findViewById(R.id.book_info);
        TextView bookCN = view.findViewById(R.id.book_cn);
        TextView borrowAble = view.findViewById(R.id.borrow_school_able);
        TextView returnDate = view.findViewById(R.id.return_date);
        TextView bookReserve = view.findViewById(R.id.book_reserve);
        ////라벨 ex)반납예정일: 글자
        TextView labelBookLocation=view.findViewById(R.id.label_book_location);
        TextView labelBookInfo=view.findViewById(R.id.label_book_info);
        TextView labelBookCn=view.findViewById(R.id.label_book_cn);
        TextView labelBorrowAble=view.findViewById(R.id.label_borrow_school_able);
        TextView labelReturnDate=view.findViewById(R.id.label_return_date);
        TextView labelBookReserve=view.findViewById(R.id.label_book_reserve);
        //텍스트 컬러 설정
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings",MODE_PRIVATE);
        int themeMode=sharedPreferences.getInt("theme",-1);// -1,1,2
        if(themeMode==1){
            //라벨 글자색 설정
            labelBookLocation.setTextColor(Color.BLACK);
            labelBookInfo.setTextColor(Color.BLACK);
            labelBookCn.setTextColor(Color.BLACK);
            labelBorrowAble.setTextColor(Color.BLACK);
            labelReturnDate.setTextColor(Color.BLACK);
            labelBookReserve.setTextColor(Color.BLACK);
            ///
            bookLocation.setTextColor(Color.BLACK);
            bookInfo.setTextColor(Color.BLACK);
            bookCN.setTextColor(Color.BLACK);
            returnDate.setTextColor(Color.BLACK);
            bookReserve.setTextColor(Color.BLACK);
        }else if(themeMode==2){
            //라벨 글자색 설정
            labelBookLocation.setTextColor(Color.WHITE);
            labelBookInfo.setTextColor(Color.WHITE);
            labelBookCn.setTextColor(Color.WHITE);
            labelBorrowAble.setTextColor(Color.WHITE);
            labelReturnDate.setTextColor(Color.WHITE);
            labelBookReserve.setTextColor(Color.WHITE);
            ///
            bookLocation.setTextColor(Color.WHITE);
            bookInfo.setTextColor(Color.WHITE);
            bookCN.setTextColor(Color.WHITE);
            returnDate.setTextColor(Color.WHITE);
            bookReserve.setTextColor(Color.WHITE);
        }
        ////
        bookLocation.setText(infos.get(position).getBookLocation());
        bookInfo.setText(infos.get(position).getBookInfo());
        bookCN.setText(infos.get(position).getBookCN());
        bookReserve.setText(infos.get(position).getBookReserve());
        switch(Integer.parseInt(infos.get(position).getBorrowAble())) {
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
            default:
                borrowAble.setText("오류 : " + infos.get(position).getBorrowAble());
                borrowAble.setTextColor(Color.RED);
                break;
        }
        returnDate.setText(infos.get(position).getReturnDate());

        return view;
    }





}
