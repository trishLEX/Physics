import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;

public class Drawer extends ApplicationFrame {
    public Drawer(
            String title,
            double[] xArray,
            double[] yArray,
            String xLabel,
            String yLabel
    ) {
        super(title);

        XYSeries series = new XYSeries(title);
        for (int i = 0; i < yArray.length; i++) {
            series.add(xArray[i], yArray[i]);
        }

        XYDataset data = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                xLabel,
                yLabel,
                data,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(640, 480));
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesLinesVisible(0, false);
        renderer.setBaseLinesVisible(true);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        chart.getXYPlot().setRenderer(renderer);
        setContentPane(panel);
    }

    public void draw() {
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }
}
