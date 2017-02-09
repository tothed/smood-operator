package wasdev.sample.servlet;

import am.Constant;
import am.hackathon.dao.AsSeries;
import am.hackathon.dao.Dao;
import am.hackathon.dao.model.Perception;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@Named
@SessionScoped
public class TeamChartView implements Serializable {

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

        return new MeterGaugeChartModel(1.5, intervals);
    }

    private void createMeterGaugeModel() {
        meterGaugeModel = initMeterGaugeModel();
        meterGaugeModel.setTitle("Current Team Mood");
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
        Dao dao =new Dao(Constant.PROXY);
        series1.setLabel("Team Mood");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Map<Date, Collection<Perception>> map = AsSeries.perceptionMap(dao).asMap();
        for (Map.Entry< Date, Collection< Perception >> entry: map.entrySet()){
            series1.set(format.format(entry.getKey()),avg(entry.getValue()));
        }
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

    private Number avg(Collection<Perception> values) {
        double vel =0.0;
        for (Perception value: values){
            vel+=value.getScore();
        }
        return vel/values.size();
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