package cs.smu.ac.sddh.Adaptor_And_Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cs.smu.ac.sddh.R;
import cs.smu.ac.sddh.Services.BookSearchFromNaver;

public class BookSearchAdapter extends RecyclerView.Adapter<BookSearchAdapter.ViewHolder> {

    private ArrayList<BookSearchFromNaver.Book> mData = null;
    private Context context;

    public BookSearchAdapter(ArrayList<BookSearchFromNaver.Book> books){
        mData = books;
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int position, BookSearchFromNaver.Book item);
    }

    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title;
        TextView author;
        TextView publish;
        TextView publish_day;

        ViewHolder(View itemView){
            super(itemView);
            icon = itemView.findViewById(R.id.search_book_icon);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            publish = itemView.findViewById(R.id.book_publish);
            publish_day = itemView.findViewById(R.id.book_publish_day);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //Item Click Event
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        //Item Selected
                        BookSearchFromNaver.Book item = mData.get(position);
                        mListener.onItemClick(v, position, item);
                    }
                }
            });

        }
    }

    @NonNull
    @Override   //아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_search_item, parent, false);
        BookSearchAdapter.ViewHolder vh = new BookSearchAdapter.ViewHolder(view);
        return vh;
    }

    @Override   //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookSearchFromNaver.Book item = mData.get(position);

        Glide.with(context).load(item.image).into(holder.icon);
        holder.title.setText(item.title);
        holder.author.setText(item.author);
        holder.publish.setText(item.publisher);
        holder.publish_day.setText(item.pubdate);
    }

    @Override   //전체 데이터 갯수 리턴
    public int getItemCount() {
        return mData.size();
    }
}
