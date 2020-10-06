package cs.smu.ac.sddh.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import cs.smu.ac.sddh.Services.BookSearchFromNaver;

public class FavoriteDBHelper extends SQLiteOpenHelper {
    public class FAVEntry implements BaseColumns{
        public static final String TABLE_NAME = "FAVentry";
        public static final String Title = "title";
        public static final String Link = "link";
        public static final String Image = "image";
        public static final String Author = "author";
        public static final String Price = "price";
        public static final String Discount = "discount";
        public static final String Publisher = "publisher";
        public static final String ISBN = "isbn";
        public static final String Description = "description";
        public static final String Pubdate = "pubdate";
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FAVEntry.TABLE_NAME + " (" + FAVEntry._ID + " INTEGER PRIMARY KEY, "+
            FAVEntry.Title + " TEXT," +
            FAVEntry.Link+ " TEXT, " +
            FAVEntry.Image+ " TEXT, " +
            FAVEntry.Author+ " TEXT, " +
            FAVEntry.Price+ " INTEGER, " +
            FAVEntry.Discount+ " INTEGER, " +
            FAVEntry.Publisher+ " TEXT, " +
            FAVEntry.ISBN+ " TEXT, " +
            FAVEntry.Description+ " TEXT, " +
            FAVEntry.Pubdate+ " TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FAVEntry.TABLE_NAME;

    public FavoriteDBHelper(@Nullable Context context){
        super(context, "SDDH.db", null, 1);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertData(BookSearchFromNaver.Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(FAVEntry._ID, 0);
        contentValues.put(FAVEntry.Title, book.title);
        contentValues.put(FAVEntry.Link, book.link);
        contentValues.put(FAVEntry.Image, book.image);
        contentValues.put(FAVEntry.Author, book.author);
        contentValues.put(FAVEntry.Price, book.price);
        contentValues.put(FAVEntry.Discount, book.discount);
        contentValues.put(FAVEntry.Publisher, book.publisher);
        contentValues.put(FAVEntry.ISBN, book.ISBN);
        contentValues.put(FAVEntry.Description, book.description);
        contentValues.put(FAVEntry.Pubdate, book.pubdate);
        long result = db.insert(FAVEntry.TABLE_NAME, null, contentValues);
        if(result == -1)    return false;
        else    return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + FAVEntry.TABLE_NAME, null);
        }catch(Exception e){
        }
        return res;
    }


    public int deleteData(String[] ISBN){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FAVEntry.TABLE_NAME, FAVEntry.ISBN +" LIKE ?", ISBN);
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

}
