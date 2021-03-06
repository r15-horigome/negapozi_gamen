package xcat.daiyonkaigi.guchiruna.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.LegendItem;
import org.afree.chart.axis.Axis;
import org.afree.chart.axis.NumberTickUnit;
import org.afree.chart.axis.TickUnit;
import org.afree.chart.axis.TickUnits;
import org.afree.chart.axis.ValueAxis;
import org.afree.chart.plot.PiePlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.plot.XYPlot;
import org.afree.chart.renderer.xy.XYItemRenderer;
import org.afree.chart.title.LegendTitle;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.xy.XYDataset;
import org.afree.data.xy.XYSeries;
import org.afree.data.xy.XYSeriesCollection;
import org.afree.graphics.PaintType;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Font;
import org.atilika.kuromoji.Token;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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
public class ShowDataBase extends Activity implements View.OnClickListener {

    // 各月の日数
    public static final int[] MONTHDAYS = {31,29,31,30,31,30,31,31,30,31,30,31};
    // グラフ表示の基準となる日付
    private int YEAR;
    private int MONTH;
    private int DAY;
    /* view関係 */
    private int totalpozi[];
    private int totalnega[];
    private Button mirai;
    private Button all;
    private Button kako;
    private Button rank;
    private Button negapozi;
    private Button form;
    private Button en;
    private ChartView graph2;
    /* グラフが円なのか折れ線なのかのフラグ 1:折れ線 2:円 */
    int graphflag;

    /* DB関係 */
    private SQLiteDatabase db;
    private  MyOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_database);

        //LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);

        /*  viewのID取得 */
        mirai = (Button) findViewById(R.id.mirai);
        all = (Button) findViewById(R.id.all);
        kako = (Button) findViewById(R.id.kako);
        form = (Button) findViewById(R.id.form);
        negapozi = (Button) findViewById(R.id.negapozi);
        rank = (Button) findViewById(R.id.rank);
        en = (Button) findViewById(R.id.en);
        mirai.setOnClickListener(this);
        all.setOnClickListener(this);
        kako.setOnClickListener(this);
        rank.setOnClickListener(this);
        negapozi.setOnClickListener(this);
        form.setOnClickListener(this);
        en.setOnClickListener(this);
        /* DBの接続セット */
        helper = new MyOpenHelper(this);
        db = helper.getReadableDatabase();

        // 日付の取得　年月日
        Calendar cal = Calendar.getInstance();
        String yearStr = "" + cal.get(Calendar.YEAR);
        String monthStr = "" + cal.get(Calendar.MONTH);
        String dayStr = "" + cal.get(Calendar.DATE);
        this.YEAR = Integer.parseInt(yearStr);
        this.MONTH = Integer.parseInt(monthStr);
        this.DAY = Integer.parseInt(dayStr);
        //日付の月が-1で取得される。原因不明。
        Log.e("NEGAPOZI--START--",yearStr + monthStr + dayStr);

        // グラフ描画
        graph2 = (ChartView)findViewById(R.id.graphview2);
        totalpozi = new int[32];
        totalnega = new int[32];
        this.createGraph();
        graphflag = 1;
        /*
         *  テストデータ表示用
         *  TODO:リリース時削除
         * */

        /*String sql3 = "SELECT distinct Day,SUM(Pozi),SUM(Nega) FROM negapozi WHERE Year = "+ this.YEAR + " AND Month = "+ this.MONTH + " GROUP BY Day";
        Cursor cu3 = db.rawQuery(sql3, null);
        boolean mov3 = cu3.moveToFirst();
        int count = 0;
        while (mov3){
            if(20<count) {
                TextView textViewday = new TextView(this);
                TextView textViewpozi = new TextView(this);
                TextView textViewnega = new TextView(this);
                String days = Integer.toString(cu3.getInt(0));
                Log.e("test",days);
                String pozi = Integer.toString(cu3.getInt(1));
                Log.e("test",pozi);
                String nega = Integer.toString(cu3.getInt(2));
                Log.e("test",nega);
                textViewpozi.setText("ポジティブ度　：" + pozi);
                textViewnega.setText("ネガティブ度　：" + nega);
                textViewday.setText(days + "日" + "");
                layout.addView(textViewpozi);
                layout.addView(textViewnega);
                layout.addView(textViewday);
            }
            count++;
            mov3 = cu3.moveToNext();
        }*/

        /*
         * ネガポジ度数、ALL用取得
         * TODO:過去何ヶ月分取るか
         */
        //Cursor cu = db.query("negapozi", new String[]{"*"} ,null,
        //        null, null, null, null);
       // 最大描写領域を設定

        /*  円グラフ */
        /*
*/

    }

    /*  各ボタンクリック時の処理 */
    public void onClick(View v){
        // 次月
        if (v == mirai){
            MONTH = MONTH +1;
            if (MONTH == 13){
                MONTH = 1;
                YEAR = YEAR+1;
            }
            Log.e("test",""+MONTH);
            this.createGraph();
        // 前月
        }else if (v == kako){
            MONTH = MONTH -1;
            if (MONTH == 0){
                MONTH =12;
                YEAR = YEAR-1;
            }
            Log.e("test",""+MONTH);
            this.createGraph();
            /*  ランキング画面に遷移  */
        }else if (v == rank){
            /*  ネガポジ画面に遷移  */
        }else if (v == negapozi){
            /* 入力画面に遷移 */
        }else if(v == form){
            /* 円グラフ表示 */
        }else if(v == en){
            // 今が折れ線なら円表示
            if(graphflag == 1){
                this.createPieChart();
                graphflag = 2;
                // それ以外は折れ線表示
            }else{
                this.createGraph();
                graphflag = 1;
            }
        }
    }

    private void createGraph(){

        /* 指定月の日数ごとのネガポジ度数、日にちの取得 */
        String sqlformonth = "SELECT distinct Day,SUM(Pozi),SUM(Nega) FROM negapozi WHERE Year = "+ this.YEAR + " AND Month = "+ this.MONTH + " GROUP BY Day ORDER BY day ASC";
        Cursor cu = db.rawQuery(sqlformonth, null);
        boolean mov2 = cu.moveToFirst();

        /* グラフ表示用配列の初期化 */
        int count = 0;
        while (count < 32 ){
            totalpozi[count] = 0;
            totalnega[count] = 0;
            count++;
        }
        /* 配列の要素 = 日にち にネガポジ度数を設定　※ネガポジ度数の無い日は0 */
        int countday = 0;
        while (mov2){
            countday = cu.getInt(0)-1 ;
            totalpozi[countday] = cu.getInt(1);
            totalnega[countday] = cu.getInt(2);
            mov2 = cu.moveToNext();
        }
        /* X軸、Y軸生成 */
        int graphcount = 0;
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries seriespozi = new XYSeries("ポジティブ");
        XYSeries seriesnega = new XYSeries("ネガティブ");
        int pozisum = 0;
        int negasum = 0;
        /* 0からスタート */
        seriespozi.add(0.0,0.0);
        seriesnega.add(0.0,0.0);
        /* 折れ線グラフ描画 */
        while (graphcount < MONTHDAYS[this.MONTH-1]){
            pozisum = pozisum + totalpozi[graphcount];
            negasum = negasum + totalnega[graphcount];
            seriespozi.add(graphcount+1,pozisum);
            seriesnega.add(graphcount+1,negasum);
            graphcount++;
        }
        dataset.addSeries(seriespozi);
        dataset.addSeries(seriesnega);
        /* グラフインスタンスの生成 */
        AFreeChart chart = ChartFactory.createXYLineChart(
                this.YEAR+"年"+this.MONTH + "月",
                "日",
                "度数",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );
        //*** フォントの設定 *****
        Font xyTitleFont = new Font(Typeface.SANS_SERIF, Typeface.BOLD, 22);
        Font xyTitleFontLabel = new Font(Typeface.SANS_SERIF, Typeface.BOLD, 30);
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));//背景の色
        chart.setBorderPaintType(new SolidColor(Color.BLACK));//枠線の色
        XYPlot plot = (XYPlot) chart.getPlot();
        //*** グラフ領域の背景とY軸を黒にする *****
        plot.setBackgroundPaintType(new SolidColor(Color.BLACK));
        plot.setDomainGridlinePaintType(new SolidColor(Color.BLACK));
        // *** Y軸の目盛間隔とフォントの変更
        ValueAxis yAxis = plot.getRangeAxis();
        TickUnits ty = new TickUnits();
        TickUnit uniY = new NumberTickUnit(10);
        ty.add(uniY);
        yAxis.setStandardTickUnits(ty);
        yAxis.setTickLabelFont(xyTitleFont);
        yAxis.setRange(0, 50);
        yAxis.setLabelFont(xyTitleFontLabel);
        // *** x軸の目盛間隔とフォントの変更
        ValueAxis xAxis = plot.getDomainAxis();
        TickUnits tx = new TickUnits();
        TickUnit uniX = new NumberTickUnit(10);
        tx.add(uniX);
        xAxis.setStandardTickUnits(tx);
        xAxis.setTickLabelFont(xyTitleFont);
        xAxis.setRange(0,31);
        xAxis.setLabelFont(xyTitleFontLabel);

        //*** 各線の太さ *****
        XYItemRenderer renderer = plot.getRenderer();
        float aLine = 3f;
        renderer.setSeriesStroke(0, aLine);
        renderer.setSeriesStroke(1, aLine);
        //*** 凡例作成 ***
        LegendTitle legend = new LegendTitle(chart.getPlot());
        legend.setItemFont(xyTitleFont);
        chart.addLegend(legend);

        // グラフの描画
        graph2.setChart(chart);
        graph2.invalidate();
    }

    private void createPieChart(){

        /*  ネガポジ度数、月合計値取得  */
        int count = 0;
        int sumpozi = 0;
        int sumnega = 0;
        while(count<32){
            sumpozi = totalpozi[count] + sumpozi;
            sumnega = totalnega[count] + sumnega;
            count++;
        }
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("ポジティブ",sumpozi);
        dataset.setValue("ネガティブ",sumnega);
        AFreeChart chart = ChartFactory.createPieChart(this.YEAR+"年"+this.MONTH + "月", dataset,
                true, false, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));//背景の色
        chart.setBorderPaintType(new SolidColor(Color.BLACK));//枠線の色
        //*** フォントの設定 *****
        Font TitleFont = new Font(Typeface.SANS_SERIF, Typeface.BOLD, 22);
        Font TitleFontLabel = new Font(Typeface.SANS_SERIF, Typeface.BOLD, 18);
        //*** ラベルの設定 *****
        plot.setBackgroundPaintType(new SolidColor(Color.BLACK));
        plot.setLabelFont(TitleFontLabel);
        //plot.setCircular(false);            // false＝楕円
        //plot.setLabelLinksVisible(false);   // false＝link(Label-Pie)なし
        //*** 凡例設定 ***
        LegendTitle legend = new LegendTitle(chart.getPlot());
        legend.setItemFont(TitleFont);
        chart.addLegend(legend);

        // グラフの描画
        graph2.setChart(chart);
        graph2.invalidate();

    }

    /* DB切断 */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}

