package tech.tablesaw.charts.impl.plotly.plots;

import java.util.ArrayList;
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

        if (columnForColor != null) {
            //add column values
            List<Figure> measureFigures = new ArrayList<>();

            List<Table> tables = table.splitOn(columnForColor).asTableList();
            //create a figure for each measure
            for (String measure : numberColNames) {
                Figure figure = createFigure(tables, groupColName, measure);
                measureFigures.add(figure);
            }

            setFigures(measureFigures.toArray(new Figure[0]));
            return;
        }


        // TODO : Support Clustered & Area display type. Research BarTrace.builder.mode(ScatterTrace.Mode.LINE) & BarTrace.builder.fill(ScatterTrace.Fill.TO_NEXT_Y) as in PlotlyAreaPlot

        Trace[] traces = createTraces(numberColNames, groupColName);

        assert (columnForColor == null);
        //create one figure for each viewRows column
        setFigures(Stream.of(traces)
                .map(t -> new Figure(layout, config, new Trace[]{t}))
                .toArray(Figure[]::new));
    }

    private Trace[] createTraces(String[] numberColNames, String groupColName) {
        Trace[] traces = new Trace[numberColNames.length];
        for (int i = 0; i < numberColNames.length; i++) {
            String name = numberColNames[i];
            BarTrace.BarBuilder builder = createTrace(table, groupColName, name, name);
            if(columnForLabel.isPresent()) {
                builder.text(table.stringColumn(columnForLabel.get()));
            }
            if(size.isPresent()) {
                builder.width(table.numberColumn(size.get()));
            }
            if (columnForColor == null) {
                if (traceColors != null && traceColors.length > i) {
                    builder.marker(Marker.builder()
                            .color(traceColors[i])
                            .build());
                }
            }
            BarTrace trace = builder
                            .build();
            traces[i] = trace;
        }
        return traces;
    }

    private BarTrace.BarBuilder createTrace(Table table, String groupColName, String numberColumn, String traceName) {
        BarTrace.BarBuilder builder
                = BarTrace.builder(table.categoricalColumn(groupColName), table.numberColumn(numberColumn))
                        .orientation(getOrientation())
                        .showLegend(true)
                        .name(traceName);
        return builder;
    }

    protected abstract BarTrace.Orientation getOrientation();

    private Figure createFigure(List<Table> colorTables, String groupColName, String measure) {
        Trace[] traces = colorTables.stream()
                .map(colorTable -> {
                    BarTrace trace = createTrace(colorTable, groupColName, measure, colorTable.name())
                            .build();
                    return trace;
                })
                .toArray(Trace[]::new);

        return new Figure(layout, config, traces);
    }
}
