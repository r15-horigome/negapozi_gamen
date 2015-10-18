package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.plot.PiePlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.geom.Font;
import org.atilika.kuromoji.Token;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import xcat.daiyonkaigi.guchiruna.R;
import xcat.daiyonkaigi.guchiruna.db.MyOpenHelper;
import xcat.daiyonkaigi.guchiruna.negapozi.ChartView;
import xcat.daiyonkaigi.guchiruna.negapozi.GraphView;
import xcat.daiyonkaigi.guchiruna.tokenize.StringToToken;

/**
 * データベースの実体を表示させるためのクラスです。
 * show_database.xmlと紐付いたActivityです。
 *
 */
public class ShowDataBase extends Activity {

    // 各月の日数
    public static final int[] MONTHDAYS = {31,29,31,30,31,30,31,31,30,31,30,31};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_database);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout3);

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
        /* ネガポジ度数、月ごとの合計値取得　*/
        Cursor cu = db.query("negapozi", new String[]{"*"} ,null,
                null, null, null, null);
        /* ネガポジ度数、指定された年月内の日ごと  */
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
                String year = Integer.toString(cu.getInt(3));
                String month = Integer.toString(cu.getInt(4));
                String day = Integer.toString(cu.getInt(5));
                textViewGuchi.setText("愚痴　　　　　：" + guchi.get(count));
                textViewPozi.setText( "ポジティブ度　：" + pozi);
                textViewNega.setText( "ネガティブ度　：" + nega);
                textViewDate.setText("日付　　　　　：" + year + "年" + month + "月" + day+ "日"+ "\n");
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
        //layout.addView(textViewPozitotal);
        //layout.addView(textViewNegatotal);

        /*  円グラフ */
        /*DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("ポジティブ",totalpozi);
        dataset.setValue("ネガティブ",totalnega);
        AFreeChart chart = ChartFactory.createPieChart("これまでの合計", dataset,
                true, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Typeface.NORMAL, 18));
        //plot.setCircular(false);            // false＝楕円
        //plot.setLabelLinksVisible(false);   // false＝link(Label-Pie)なし

        /* 折れ線グラフ */
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("XYSeries");
        series.add(1, 1);
        series.add(2, 2);
        series.add(3, 3);
        series.add(4, 2);
        series.add(5, 3);
        series.add(6, 4);
        series.add(7, 7);
        dataset.addSeries(series);

        AFreeChart chart = ChartFactory.createXYLineChart(
                "これまでの合計",
                "年月",
                "度数",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        ChartView graph2 = (ChartView) findViewById(R.id.graphview2);
        // グラフを生成
        graph2.setChart(chart);

        c.close();
        db.close();
    }
}

