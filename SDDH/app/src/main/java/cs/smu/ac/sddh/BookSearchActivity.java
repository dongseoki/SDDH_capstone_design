package cs.smu.ac.sddh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import cs.smu.ac.sddh.Adaptor_And_Item.BookSearchAdapter;
import cs.smu.ac.sddh.Services.BookSearchFromNaver;

public class BookSearchActivity extends AppCompatActivity {
    RecyclerView recyclerView = null;
    BookSearchAdapter adapter = null;
    ArrayList<BookSearchFromNaver.Book> books = null;
    Intent nextActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        books = intent.getParcelableExtra("Books");
        books = (ArrayList<BookSearchFromNaver.Book>)intent.getExtras().get("Books");

        recyclerView = findViewById(R.id.search_result_recyclerview);
        adapter = new BookSearchAdapter(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new BookSearchAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View v, int position, BookSearchFromNaver.Book item) {
                nextActivity = new Intent(getApplicationContext(), BookDetailActivity.class);
                nextActivity.putExtra("book", item);
                startActivity(nextActivity);
            }
        });

    }

    private void addItem(BookSearchFromNaver.Book book){
    }

}

