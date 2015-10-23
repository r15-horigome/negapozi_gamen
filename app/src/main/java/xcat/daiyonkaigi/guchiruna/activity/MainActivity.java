package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.Calendar;
import java.util.Random;

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.MyOpenHelper;


public class MainActivity extends Activity {

    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout)findViewById(R.id.relativelayout1);

        //DBの初期化処理
        MyOpenHelper helper = new MyOpenHelper(this);
        final SQLiteDatabase db = helper.getWritableDatabase();

        //テキスト及び関連する情報の内容を取得
        final EditText editText = (EditText) findViewById(R.id.editText);



        //ボタン押下時の登録処理
        Button confirmButton = (Button)this.findViewById(R.id.button);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                String article = editText.getText().toString();
                // テストデータ初期化用
                db.delete( "negapozi", null, null );
                //db.delete( "article", null, null );
                //記事を格納
                ContentValues articleInsertValues = new ContentValues();
                articleInsertValues.put("article", article);
                //TODO 日付を登録
                long id = db.insert("article", "null", articleInsertValues);

                /*
                 * ネガポジ機能テスト用
                 * ネガポジ用データをDBに登録
                 */
                // 日付の取得　年月日
                Calendar cal = Calendar.getInstance();
                String yearStr = "" + cal.get(Calendar.YEAR);
                String monthStr = "" + cal.get(Calendar.MONTH);
                String dayStr = "" + cal.get(Calendar.DATE);
                int year = Integer.parseInt(yearStr);
                int month = Integer.parseInt(monthStr);
                int day = Integer.parseInt(dayStr);

                /*
                 *  テストデータ作成用
                 *  乱数でネガポジ度数を出して、DBに登録してます。
                 *  奥村さんの開発が完了したらマージする。
                 *
                 * */
                int max = 0;
                Random rnd;
                int kekka = 0;
                long ret;
                Log.e("test","テストデータ作成開始");
                try {
                    while (max < 31) {
                        Log.e("test",max+"回目");
                        //乱数の取得
                        //-3～3の乱数を取得する
                        rnd = new Random();
                        kekka = rnd.nextInt(7) - 3;
                        if (kekka > 0) {
                            Log.e("test",kekka+"POZI乱数");
                            //DBに保存 ポジティブ度数、年月日
                            ContentValues values = new ContentValues();
                            values.put("Pozi", kekka);
                            values.put("Year", year);
                            values.put("Month", month);
                            values.put("Day", max+1);
                            ret = db.insert("negapozi", null, values);
                            Log.e("test",ret+"行目作成");
                        } else {
                            Log.e("test",kekka+"NEGA乱数");
                            //DBに保存 ネガティブ度数、年月日
                            ContentValues values = new ContentValues();
                            values.put("Nega", kekka * -1);
                            values.put("Year", year);
                            values.put("Month", month);
                            values.put("Day", max+1);
                            ret = db.insert("negapozi", null, values);
                            Log.e("test",ret+"行目作成");
                        }
                        max++;
                        day++;
                    }
                }finally {
                    db.close();
                }
                //次画面での表示処理
                Intent dbIntent = new Intent(MainActivity.this, NegapoziActivity.class);
                startActivity(dbIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
