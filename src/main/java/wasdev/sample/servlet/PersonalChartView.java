package wasdev.sample.servlet;

import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
public class PersonalChartView implements Serializable {

    private LineChartModel lineModel1;
    private LineChartModel lineModel2;
    private LineChartModel dateModel;
    private MeterGaugeChartModel meterGaugeModel;

    @PostConstruct
    public void init() {
        createLineModels();
        createDateModel();
        createMeterGaugeModel();
    }

    public MeterGaugeChartModel getMeterGaugeModel() {
        return meterGaugeModel;
    }

    public LineChartModel getDateModel() {
        return dateModel;
    }

    public LineChartModel getLineModel1() {
        return lineModel1;
    }

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    private MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>(){{
            add(1);
            add(2);
            add(3);
        }};

        return new MeterGaugeChartModel(2.5, intervals);
    }

    private void createMeterGaugeModel() {
        meterGaugeModel = initMeterGaugeModel();
        meterGaugeModel.setTitle("Your Current Mood");
        meterGaugeModel.setSeriesColors("cc6666,E7E658,93b75f");
        meterGaugeModel.setGaugeLabel("Mood");
        meterGaugeModel.setGaugeLabelPosition("bottom");
        meterGaugeModel.setShowTickLabels(false);
        meterGaugeModel.setLabelHeightAdjust(110);
        meterGaugeModel.setIntervalOuterRadius(100);
    }

    private void createDateModel() {
        dateModel = new LineChartModel();
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Your Mood");

        series1.set("2016-11-01", 1);
        series1.set("2016-11-06", 2);
        series1.set("2016-11-07", 2);
        series1.set("2016-11-08", 3);
        series1.set("2016-11-09", 3);
        series1.set("2016-11-12", 1);
        series1.set("2016-11-14", 3);
        series1.set("2016-11-15", 2);
        series1.set("2016-11-17", 2);
        series1.set("2016-11-18", 3);
        series1.set("2016-11-23", 3);
        series1.set("2016-11-24", 1);
        series1.set("2016-11-27", 1);
        series1.set("2016-11-28", 2);

        series1.set("2016-12-01", 1);
        series1.set("2016-12-06", 3);
        series1.set("2016-12-07", 2);
        series1.set("2016-12-08", 3);
        series1.set("2016-12-09", 2);
        series1.set("2016-12-12", 3);
        series1.set("2016-12-18", 1);
        series1.set("2016-12-23", 3);
        series1.set("2016-12-24", 2);
        series1.set("2016-12-27", 1);
        series1.set("2016-12-28", 3);

        series1.set("2017-01-01", 1);
        series1.set("2017-01-06", 2);
        series1.set("2017-01-07", 3);
        series1.set("2017-01-08", 2);
        series1.set("2017-01-09", 3);
        series1.set("2017-01-12", 1);
        series1.set("2017-01-18", 1);
        series1.set("2017-01-23", 2);
        series1.set("2017-01-24", 2);
        series1.set("2017-01-27", 2);
        series1.set("2017-01-28", 3);
        series1.set("2017-01-29", 3);
        series1.set("2017-01-30", 1);

        dateModel.addSeries(series1);

        dateModel.setTitle("Zoom for Details");
        dateModel.setZoom(true);
        dateModel.getAxis(AxisType.Y).setLabel("Mood");
        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setMax("2017-02-01");
        axis.setTickFormat("%b %#d, %y");

        dateModel.getAxes().put(AxisType.X, axis);
    }

    private void createLineModels() {
        lineModel1 = initLinearModel();
        lineModel1.setTitle("Linear Chart");
        lineModel1.setLegendPosition("e");
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);

        lineModel2 = initCategoryModel();
        lineModel2.setTitle("Category Chart");
        lineModel2.setLegendPosition("e");
        lineModel2.setShowPointLabels(true);
        lineModel2.getAxes().put(AxisType.X, new CategoryAxis("Years"));
        yAxis = lineModel2.getAxis(AxisType.Y);
        yAxis.setLabel("Births");
        yAxis.setMin(0);
        yAxis.setMax(200);
    }

    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");

        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Series 2");

        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);

        model.addSeries(series1);
        model.addSeries(series2);

        return model;
    }

    private LineChartModel initCategoryModel() {
        LineChartModel model = new LineChartModel();

        ChartSeries boys = new ChartSeries();
        boys.setLabel("Boys");
        boys.set("2004", 120);
        boys.set("2005", 100);
        boys.set("2006", 44);
        boys.set("2007", 150);
        boys.set("2008", 25);

        ChartSeries girls = new ChartSeries();
        girls.setLabel("Girls");
        girls.set("2004", 52);
        girls.set("2005", 60);
        girls.set("2006", 110);
        girls.set("2007", 90);
        girls.set("2008", 120);

        model.addSeries(boys);
        model.addSeries(girls);

        return model;
    }

}