package tech.tablesaw.charts.impl.plotly.plots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tech.tablesaw.api.Table;
import tech.tablesaw.charts.ChartBuilder;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.BarTrace;
import tech.tablesaw.plotly.traces.Trace;

public abstract class PlotlyBarPlot extends PlotlyAbstractPlot {

    private final static Logger LOG = LogManager.getLogger();

    private final Optional<String> size;
    private final Optional<String> columnForLabel;

    public PlotlyBarPlot(ChartBuilder chartBuilder) {
        setChartBuilder(chartBuilder);
        String groupColName = columnsForViewColumns[0];
        if (columnsForViewColumns.length > 1) {
            LOG.warn("Bar plot will only take into account the 1st view colum ({} received)", columnsForViewColumns.length);
        }

        final String[] numberColNames = columnsForViewRows;

        // TODO : columnForDetails -

        if (columnsForLabels != null && columnsForLabels.length > 0) {
            columnForLabel = Optional.of(columnsForLabels[0]);
            if(columnsForLabels.length > 1) {
                LOG.warn("Bar plot will only take into account the 1st label column ({} received)", columnsForLabels.length);
            }
        } else {
            columnForLabel = Optional.empty();
        }

        size = Optional.ofNullable(this.columnForSize);
        // TODO : Support Clustered & Area display type. Research BarTrace.builder.mode(ScatterTrace.Mode.LINE) & BarTrace.builder.fill(ScatterTrace.Fill.TO_NEXT_Y) as in PlotlyAreaPlot

        if (columnForColor != null) {
            //add column values

            List<Table> tables = table.splitOn(columnForColor).asTableList();
            FigureBag bag = individualAxes
                    ? new IndividualFigureBag(this::createFigure)
                    : new SharedAxisFigureBag(this::createFigure);

            //XXX: use same color for a given color column value across measures? Eg. a continent across multiple country-level measurements should get the same color or not?
            for (String measure : numberColNames) {
                Trace[] measureTraces = createTraces(tables, groupColName, measure);
                bag.addTraces(measureTraces);
            }

            setFigures(bag.getFigures());
        } else {
            Trace[] traces = createTraces(numberColNames, groupColName);

            if (individualAxes) {
                //create one figure for each viewRows column
                setFigures(Stream.of(traces)
                        .map(t -> new Figure(layout, config, new Trace[]{t}))
                        .toArray(Figure[]::new));
            } else {
                //shared axis, create a single figure for all measures
                setFigure(new Figure(layout, config, traces));
            }
        }
    }

    private Figure createFigure(Trace[] traces) {
        return new Figure(layout, config, traces);
    }

    private Trace[] createTraces(String[] numberColNames, String groupColName) {
        Trace[] traces = new Trace[numberColNames.length];
        for (int i = 0; i < numberColNames.length; i++) {
            String traceColor = null;
            String name = numberColNames[i];
            if (columnForColor == null) {
                if (traceColors != null && traceColors.length > i) {
                    traceColor = traceColors[i];
                }
            }
            traces[i] = createTrace(name, groupColName, traceColor);
        }
        return traces;
    }

    private Trace createTrace(String name, String groupColName, String traceColor) {
            BarTrace.BarBuilder builder = createTrace(table, groupColName, name, name);
            if(traceColor != null) {
                    builder.marker(Marker.builder()
                            .color(traceColor)
                            .build());
            }
            BarTrace trace = builder
                            .build();
            return trace;
    }

    private BarTrace.BarBuilder createTrace(Table table, String groupColName, String numberColumn, String traceName) {
        BarTrace.BarBuilder builder
                = BarTrace.builder(table.categoricalColumn(groupColName), table.numberColumn(numberColumn))
                        .orientation(getOrientation())
                        .showLegend(true)
                        .name(traceName);
        if (columnForLabel.isPresent()) {
            builder.text(table.stringColumn(columnForLabel.get()));
        }
        if (size.isPresent()) {
            builder.width(table.numberColumn(size.get()));
        }
        //TODO: add trace color here too (and merge / remove the same chunk from createTraces)
        return builder;
    }

    protected abstract BarTrace.Orientation getOrientation();

    private Trace[] createTraces(List<Table> colorTables, String groupColName, String measure) {
        Trace[] traces = colorTables.stream()
                .map(colorTable -> {
                    BarTrace trace = createTrace(colorTable, groupColName, measure, colorTable.name())
                            .build();
                    return trace;
                })
                .toArray(Trace[]::new);
        return traces;
    }
}
