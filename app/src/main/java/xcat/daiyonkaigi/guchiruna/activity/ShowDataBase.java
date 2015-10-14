package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.atilika.kuromoji.Token;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.MyOpenHelper;
import xcat.daiyonkaigi.guchiruna.tokenize.StringToToken;

/**
 * データベースの実体を表示させるためのクラスです。
 * show_database.xmlと紐付いたActivityです。
 *
 */
public class ShowDataBase extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_database);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);

        MyOpenHelper helper = new MyOpenHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        // queryメソッドの実行例
        Cursor c = db.query("article", new String[]{"article"}, null,
                null, null, null, null);

        boolean mov = c.moveToFirst();
        ArrayList<String> guchi = new ArrayList<String>();
        while (mov){
            //愚痴の保存
            guchi.add( c.getString(0) );
            mov = c.moveToNext();
        }
        /* ネガポジ機能テスト用　*/
        Cursor cu = db.query("token", new String[]{"*"} ,null,
                null, null, null, null);
        boolean mov2 = cu.moveToFirst();
        int totalpozi = 0;
        int totalnega = 0;
        int count = 0;
            while (mov2){
                TextView textViewGuchi = new TextView(this);
                TextView textViewPozi = new TextView(this);
                TextView textViewNega = new TextView(this);
                TextView textViewDate = new TextView(this);
                String pozi = Integer.toString( cu.getInt(1) );
                String nega = Integer.toString( cu.getInt(2) );
                String hiduke = Integer.toString(cu.getInt(3));
                textViewGuchi.setText("愚痴　　　　　：" + guchi.get(count));
                textViewPozi.setText( "ポジティブ度　：" + pozi);
                textViewNega.setText( "ネガティブ度　：" + nega);
                textViewDate.setText( "日付　　　　　：" + hiduke);
                layout.addView(textViewGuchi);
                layout.addView(textViewPozi);
                layout.addView(textViewNega);
                layout.addView(textViewDate);
                // DB内の全ポジティブ/ネガティブ度の合計値取得
                // TODO:日付による抽出条件
                totalpozi = totalpozi + cu.getInt(1);
                totalnega = totalnega + cu.getInt(2);
                count++;
                mov2 = cu.moveToNext();
            }
        TextView textViewPozitotal = new TextView(this);
        TextView textViewNegatotal = new TextView(this);

        textViewPozitotal.setText("【合計値】\n" + "ポジティブ　：　" + Integer.toString(totalpozi));
        textViewNegatotal.setText("ネガティブ　：　" + Integer.toString(totalnega));
        layout.addView(textViewPozitotal);
        layout.addView(textViewNegatotal);

        c.close();
        db.close();
    }
}

