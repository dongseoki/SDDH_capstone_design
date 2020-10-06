package cs.smu.ac.sddh.Adaptor_And_Item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cs.smu.ac.sddh.R;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
    private ArrayList<String> mData = null; //String 대신 클래스 쓰면 사용자 정의 클래스 가능
    private Context context;

    public RecentSearchAdapter(ArrayList<String> values){
        mData = values;
    }
    public interface OnItemClickListener{
        void onItemClick(View v, int position, String item);
    }

    private RecentSearchAdapter.OnItemClickListener mListener = null;
    public void setOnItemClickListener(RecentSearchAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        /*
        ImageView icon;
        TextView title;
        TextView author;
        TextView publish;
        TextView publish_day;
        */
        TextView text;

        ViewHolder(View itemView){
            super(itemView);
            /*
            icon = itemView.findViewById(R.id.search_book_icon);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            publish = itemView.findViewById(R.id.book_publish);
            publish_day = itemView.findViewById(R.id.book_publish_day);
            */
            text = itemView.findViewById(R.id.RecentSearchTextView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //Item Click Event
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        //Item Selected
                        //BookSearchFromNaver.Book item = mData.get(position);
                        //mListener.onItemClick(v, position, item);
                        String value = mData.get(position);
                        mListener.onItemClick(v, position, value);
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

        View view = inflater.inflate(R.layout.latest_seachlist_item, parent, false);
        RecentSearchAdapter.ViewHolder vh = new RecentSearchAdapter.ViewHolder(view);
        return vh;
    }

    @Override   //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    public void onBindViewHolder(@NonNull RecentSearchAdapter.ViewHolder holder, int position) {
        //BookSearchFromNaver.Book item = mData.get(position);
        String item = mData.get(position);
        holder.text.setText(item);

        //Glide.with(context).load(item.image).into(holder.icon);
        //holder.title.setText(item.title);
        //holder.author.setText(item.author);
        //holder.publish.setText(item.publisher);
        //holder.publish_day.setText(item.pubdate);
    }

    @Override   //전체 데이터 갯수 리턴
    public int getItemCount() {
        return mData.size();
    }
}
