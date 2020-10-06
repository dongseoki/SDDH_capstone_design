package cs.smu.ac.sddh.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import cs.smu.ac.sddh.Adaptor_And_Item.LoanStatus;

public class AlarmDBHelper extends SQLiteOpenHelper {
    public class AlarmEntry implements BaseColumns {
        public static final String TABLE_NAME = "alarmentry";
        public static final String RequestCode = "requestcode";
        public static final String BookTitle = "booktitle";
        public static final String SchoolName = "schoolname";
        public static final String ReturnDate = "returndate";
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + AlarmEntry.TABLE_NAME + " (" + AlarmEntry._ID + " INTEGER PRIMARY KEY, "+
            AlarmEntry.RequestCode + " TEXT," +
            AlarmEntry.BookTitle + " TEXT," +
            AlarmEntry.SchoolName + " TEXT," +
            AlarmEntry.ReturnDate + " TEXT)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;

    public AlarmDBHelper(@Nullable Context context){
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

    public boolean insertData(LoanStatus loanStatus, int requestCode, String schoolName, String bookTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(FAVEntry._ID, 0);
        contentValues.put(AlarmEntry.RequestCode, Integer.toString(requestCode));
        contentValues.put(AlarmEntry.BookTitle, bookTitle);
        contentValues.put(AlarmEntry.SchoolName, schoolName);
        contentValues.put(AlarmEntry.ReturnDate, loanStatus.RDD);
        long result = db.insert(AlarmEntry.TABLE_NAME, null, contentValues);
        if(result == -1)    return false;
        else    return true;
    }

    public boolean insertData(int requestCode, String schoolName, String bookTitle, String duedate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(FAVEntry._ID, 0);
        contentValues.put(AlarmEntry.RequestCode, Integer.toString(requestCode));
        contentValues.put(AlarmEntry.BookTitle, bookTitle);
        contentValues.put(AlarmEntry.SchoolName, schoolName);
        contentValues.put(AlarmEntry.ReturnDate, duedate);
        long result = db.insert(AlarmEntry.TABLE_NAME, null, contentValues);
        if(result == -1)    return false;
        else    return true;
    }

    /*
     * 사용법 : BookDetailActivity.java 참조
     * Cursor cursor = dbHelper.getAllData();
     * boolean flags;
     * if(cursor == null){ // cursor 가 null일 경우 데이터베이스 내 TABLE이 존재하지 않는 상태
            dbHelper.onCreate(dbHelper.getWritableDatabase());
        }
        else{
            flags = cursor.moveToFirst();
            while(flags){
                String val = cursor.getString(8); //8번 == ISBN
                if(val.equals(book.ISBN)){
                    searchISBN = true;
                    break;
                }
                flags = cursor.moveToNext();
            }
        }
     */
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        try {
            res = db.rawQuery("select * from " + AlarmEntry.TABLE_NAME, null);
        }catch(Exception e){
        }
        return res;
    }

    //Requets Code 를 키로 사용하여 삭제
    public int deleteData(String[] requestCode){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(AlarmEntry.TABLE_NAME, AlarmEntry.RequestCode +" LIKE ?", requestCode);
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }
}
