package xcat.daiyonkaigi.guchiruna.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteデータベースを生成するクラスです。
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    public MyOpenHelper(Context context) {
        super(context, "guchiruna2", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table article(" + "article text" + ");");

        //ネガポジ用のテーブル
        db.execSQL("create table negapozi(" +
                " id INTEGER PRIMARY KEY " +
                ",Pozi REAR " +
                ",Nega REAR " +
                ",Year INTEGER " +
                ",Month INTEGER " +
                ",Day INTEGER " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
