package xcat.daiyonkaigi.guchiruna.negapozi;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import org.afree.chart.AFreeChart;
import org.afree.graphics.geom.RectShape;

/**
 * Created by s1157252 on 2015/10/17.
 */
public class GraphView  extends ChartView{
    private AFreeChart chart;

    //*************************************************************************
    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //  RectShape chartArea = new RectShape(0.0, 0.0, 200.0, 200.0);
      //  this.chart.draw(canvas, chartArea);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(200,200);
    }

    public void setChart(AFreeChart chart) {
        this.chart = chart;
    }
}
