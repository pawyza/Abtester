package ib.edu.Abtester;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class BarGraphActivity extends Activity {

    private float totalTime;
    private float totalLinesTime;
    private float totalSpaceTime;
    private float averageSpeed;


    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bar_graph);
            totalTime = getIntent().getFloatExtra("totalTime",0);
            totalSpaceTime = getIntent().getFloatExtra("totalSpaceTime",0);
            totalLinesTime = getIntent().getFloatExtra("totalLinesTime",0);
            averageSpeed = getIntent().getFloatExtra("averageSpeed",0);
            openChart();
        }

        private void openChart(){
            /*HandSpeed: 0.086803 mm/ms
            TimeForSingleLine: 253.44 ms
            TimeSpaceBetweenLines: 380.60 ms
            TotalTime: 3801.7 ms*/
            float[] defaultValues = { 0.090f,250,380,3800};

            XYSeries totalTimeSeries = new XYSeries("Total time");
            XYSeries totalSpaceTimeSeries = new XYSeries("Total space time");
            XYSeries totalLinesTimeSeries = new XYSeries("Total lines time");
            XYSeries averageSpeedSeries = new XYSeries("Average speed");

            XYSeries totalTimeSeriesStandard = new XYSeries("Standard total time");
            XYSeries totalSpaceTimeSeriesStandard = new XYSeries("Standard total space time");
            XYSeries totalLinesTimeSeriesStandard = new XYSeries("Standard total single line time");
            XYSeries averageSpeedSeriesStandard = new XYSeries("Standard average speed");

            totalTimeSeries.add(0,totalTime);
            totalTimeSeriesStandard.add(1,defaultValues[3]);
            totalTimeSeries.add(3,0);
            totalTimeSeriesStandard.add(4,0);

            totalSpaceTimeSeries.add(0,totalSpaceTime);
            totalSpaceTimeSeriesStandard.add(1,defaultValues[2]);
            totalSpaceTimeSeries.add(3,0);
            totalSpaceTimeSeriesStandard.add(4,0);

            totalLinesTimeSeries.add(0,totalLinesTime);
            totalLinesTimeSeriesStandard.add(1,defaultValues[1]);
            totalLinesTimeSeries.add(3,0);
            totalLinesTimeSeriesStandard.add(4,0);

            averageSpeedSeries.add(0,averageSpeed);
            averageSpeedSeriesStandard.add(1,defaultValues[0]);
            averageSpeedSeries.add(3,0);
            averageSpeedSeriesStandard.add(4,0);


            XYMultipleSeriesDataset dataset1 = new XYMultipleSeriesDataset();

            dataset1.addSeries(totalTimeSeries);
            dataset1.addSeries(totalTimeSeriesStandard);

            XYMultipleSeriesDataset dataset2 = new XYMultipleSeriesDataset();

            dataset2.addSeries(totalSpaceTimeSeries);
            dataset2.addSeries(totalSpaceTimeSeriesStandard);

            XYMultipleSeriesDataset dataset3 = new XYMultipleSeriesDataset();

            dataset3.addSeries(totalLinesTimeSeries);
            dataset3.addSeries(totalLinesTimeSeriesStandard);

            XYMultipleSeriesDataset dataset4 = new XYMultipleSeriesDataset();

            dataset4.addSeries(averageSpeedSeries);
            dataset4.addSeries(averageSpeedSeriesStandard);

            XYSeriesRenderer renderer1 = new XYSeriesRenderer();
            renderer1.setColor(Color.rgb(80, 220, 80));
            renderer1.setFillPoints(true);
            renderer1.setLineWidth(2);
            renderer1.setDisplayChartValues(true);

            XYSeriesRenderer renderer2 = new XYSeriesRenderer();
            renderer2.setColor(Color.rgb(220, 80, 80));
            renderer2.setFillPoints(true);
            renderer2.setLineWidth(2);
            renderer2.setDisplayChartValues(true);

            // Creating a XYMultipleSeriesRenderer to customize the whole chart
            XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
            multiRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
            multiRenderer.setXLabels(0);
            multiRenderer.setChartTitle("");
            multiRenderer.setXTitle("");
            multiRenderer.setYTitle("");
            multiRenderer.addXTextLabel(1, "");


            // Adding incomeRenderer and expenseRenderer to multipleRenderer
            // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
            // should be same
            multiRenderer.addSeriesRenderer(renderer1);
            multiRenderer.addSeriesRenderer(renderer2);

            // Creating an intent to plot bar chart using dataset and multipleRenderer
            LinearLayout chartContainer1 = (LinearLayout) findViewById(R.id.chartView1);
            View chart1 = ChartFactory.getBarChartView(getBaseContext(), dataset4, multiRenderer, BarChart.Type.DEFAULT);
            chartContainer1.addView(chart1);

            LinearLayout chartContainer2 = (LinearLayout) findViewById(R.id.chartView2);
            View chart2 = ChartFactory.getBarChartView(getBaseContext(), dataset3, multiRenderer, BarChart.Type.DEFAULT);
            chartContainer2.addView(chart2);

            LinearLayout chartContainer3 = (LinearLayout) findViewById(R.id.chartView3);
            View chart3 = ChartFactory.getBarChartView(getBaseContext(), dataset2, multiRenderer, BarChart.Type.DEFAULT);
            chartContainer3.addView(chart3);

            LinearLayout chartContainer4 = (LinearLayout) findViewById(R.id.chartView4);
            View chart4 = ChartFactory.getBarChartView(getBaseContext(), dataset1, multiRenderer, BarChart.Type.DEFAULT);
            chartContainer4.addView(chart4);


    }

    public void onButtonReturn(View view) {
        Intent intent = new Intent(view.getContext(), MenuController.class);
        Bundle bundle = new Bundle();
        bundle.putString("profileName",getIntent().getStringExtra("profileName"));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}