package cs.smu.ac.sddh.SQLite;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;


/*
 * SQLiteHelper 사용법
 * 1.
 * 2. 데이터를 담을 구조체 정의
 * 3. CREATE TABLE 과 같은 SQL문으로 DB 생성 (SQL_CREATE_ENTRIES 참조)
 * 4. db.execSQL(create table 구문 넣기) // SQL 실행
 * 5. insertdata로 데이터 넣기
 *
 * 잘 모르겠다 싶으면 그냥 상균이한테 말하기!!! 제가 할게요 구현해야하는게 있다면
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    public final class Entry implements BaseColumns{
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    public static final String DATABASE_NAME = "SDDH.db";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Entry.TABLE_NAME + " (" + Entry._ID + " INTEGER PRIMARY KEY, "+
            Entry.COLUMN_NAME_TITLE + " TEXT," +
            Entry.COLUMN_NAME_SUBTITLE+ " TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Entry.TABLE_NAME;


    public SQLiteHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertData(String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Entry.COLUMN_NAME_TITLE, value);
        long result = db.insert(Entry.TABLE_NAME, null, contentValues);
        if(result == -1)    return false;
        else    return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + Entry.TABLE_NAME, null);
        }catch(Exception e){
        }
        return res;
    }

    public boolean updateData(String id, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Entry.COLUMN_NAME_TITLE, value);
        db.update(Entry.TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public int deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Entry.TABLE_NAME, "ID = ?", new String[] {id});
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }
}
