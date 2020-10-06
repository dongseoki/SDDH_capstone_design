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

import cs.smu.ac.sddh.Services.BookSearchFromNaver;
import cs.smu.ac.sddh.R;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{
    //ArrayList<FavoriteInfo> favoriteInfos=new ArrayList<>();
    private ArrayList<BookSearchFromNaver.Book> mData = null;
    private Context context;

    FavoriteAdapter(){}
    public FavoriteAdapter(ArrayList<BookSearchFromNaver.Book> books){ mData = books; }
    public interface OnItemClickListener{
        void onItemClick(View v, int position, BookSearchFromNaver.Book item);
    }

    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView book;
        protected TextView author;
        protected TextView bookTitle;
        protected TextView publishingCompany;
        protected TextView publishingDate;
        public ViewHolder(View view){
            super(view);
            this.book=view.findViewById(R.id.favorite_book_image);
            this.author=view.findViewById(R.id.favorite_author);
            this.bookTitle=view.findViewById(R.id.favorite_book_title);
            this.publishingCompany=view.findViewById(R.id.favorite_publishing_company);
            this.publishingDate=view.findViewById(R.id.favorite_publishing_day);


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
    public void addItem(BookSearchFromNaver.Book book){
        mData.add(book);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.favorite_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookSearchFromNaver.Book item = mData.get(position);

        Glide.with(context).load(item.image).into(holder.book);
        //FavoriteInfo favoriteInfo=favoriteInfos.get(position);
        holder.author.setText(item.author);
        holder.bookTitle.setText(item.title);
        holder.publishingCompany.setText(item.publisher);
        holder.publishingDate.setText(item.pubdate);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
