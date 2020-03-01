package quicksilver.webapp.simpleserver.controllers.root.components.explorer;

import quicksilver.commons.data.TSDataSet;
import quicksilver.commons.data.TSDataSetFactory;
import quicksilver.webapp.simpleui.bootstrap4.charts.TSFigurePanel;
import quicksilver.webapp.simpleui.bootstrap4.components.*;
import quicksilver.webapp.simpleui.bootstrap4.layouts.BSBorderLayout;
import quicksilver.webapp.simpleui.bootstrap4.quick.QuickBodyPanel;
import quicksilver.webapp.simpleui.html.components.HTMLLineBreak;
import spark.QueryParamsMap;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.charts.ChartBuilder;
import tech.tablesaw.columns.Column;
import tech.tablesaw.plotly.components.Layout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Explorer2 extends Explorer {

    public Explorer2(QueryParamsMap query) {
        super(query);
    }

    protected BSPanel createContentPanelCenter() {
        if (panel == null) {
            panel = new BSPanel();
            //1st call, from constructor?!
            //TODO: EMI: This is a hack, fix the underlying problem on how this method gets called from the constructor
            return panel;
        }

        List<Method> datasetSampleMethods = new ArrayList<>();
        for (Method m : TSDataSetFactory.class.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getName().startsWith("createSample") && m.getParameterCount() == 0) {
                datasetSampleMethods.add(m);
            }
        }
        datasetSampleMethods.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });

        String[] datasets = datasetSampleMethods.stream()
                .map(m -> m.getName().substring("createSample".length()))
                .toArray(String[]::new);

        BSForm form = createForm();

        panel.add(new HTMLLineBreak(1));
        panel.add(form);

        Method sampleMethod = datasetSampleMethods.get(0);
        if (query != null && query.hasKey("dataset")) {
            String dataset = query.get("dataset").value();
            for (int i = 0; i < datasets.length; i++) {
                if (datasets[i].equals(dataset)) {
                    //found it
                    sampleMethod = datasetSampleMethods.get(i);
                }
            }
        }
        Table table;
        try {
            table = ((TSDataSet) sampleMethod.invoke(null)).getTSTable();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //shouldn't happen, but add a fallback
            table = TSDataSetFactory.createSampleCountryEconomicData().getTSTable();
        }

        ChartBuilder.CHART_TYPE chartType = ChartBuilder.CHART_TYPE.VERTICAL_BAR;
        if (query != null && query.hasKey("chartType")) {
            String chart = query.get("chartType").value();
            chartType = ChartBuilder.CHART_TYPE.valueOf(chart);
        }

        ChartBuilder chartBuilder = ChartBuilder.createBuilder()
                .dataTable(table);

        StringBuilder generatedCode = new StringBuilder();
        generatedCode.append("ChartBuilder chartBuilder = ChartBuilder.createBuilder()\n"
                + "  .dataTable(table)\n"
                + "  .chartType(ChartBuilder.CHART_TYPE." + chartType.name()
        );

        List<Object> chartTypeOptions = new ArrayList<>();
        if (query != null && query.hasKey("options")) {
            String options = query.get("options").value().trim();
            if (!options.isEmpty()) {
                for (String name : options.split(" ")) {
                    name = name.trim();
                    if (name.isEmpty()) {
                        continue;
                    }
                    try {
                        Layout.BarMode b = Layout.BarMode.valueOf(name.toUpperCase());
                        chartTypeOptions.add(b);

                        generatedCode.append(", Layout.BarMode.").append(b.name());
                    } catch (IllegalArgumentException iae) {
                        //ignore, continue
                    }
                }
            }
        }
        generatedCode.append(")\n");

        chartBuilder.chartType(chartType, chartTypeOptions.toArray())
                .layout(500, 200, false);

        generatedCode.append("  .layout(500, 200, false);\n");

        if (query != null && query.hasKey("axes")) {
            String axes = query.get("axes").value().trim();
            if (!axes.isEmpty()) {
                try {
                    ChartBuilder.Axes axesType = ChartBuilder.Axes.valueOf(axes);
                    chartBuilder.axesType(axesType);

                    //is it the default axes type?
                    if (axesType != ChartBuilder.DEFAULT_AXES) {
                        generatedCode.append("  .axesType(ChartBuilder.Axes.").append(axesType.name()).append(")\n");
                    }
                } catch (IllegalArgumentException iae) {
                    //ignore, continue
                }
            }
        }
        if (query != null && query.hasKey("groupby")) {
            String groupby = query.get("groupby").value().trim();
            if (!groupby.isEmpty()) {
                chartBuilder.groupBy(groupby);

                generatedCode.append("  .groupBy(\"").append(groupby).append("\")\n");
            }
        }

        if (query != null && query.hasKey("columns")) {
            String[] cols
                    = query.get("columns").value().trim().split(" ");
            chartBuilder.columnsForViewColumns(cols);

            generatedCode.append("  .columnsForViewColumns(");
            generatedCode.append(dataAsString(cols));
            generatedCode.append(")\n");
        }
        if (query != null && query.hasKey("rows")) {
            String[] rows
                    = query.get("rows").value().trim().split(" ");
            chartBuilder.columnsForViewRows(rows);

            generatedCode.append("  .columnsForViewRows(");
            generatedCode.append(dataAsString(rows));
            generatedCode.append(")\n");
        }
        if (query != null && query.hasKey("color")) {
            String color
                    = query.get("color").value();
            if (!color.isEmpty()) {
                chartBuilder.columnForColor(color);

                generatedCode.append("  .columnForColor(");
                generatedCode.append(dataAsString(new String[]{color}));
                generatedCode.append(")\n");
            }
        }
        if (query != null && query.hasKey("size")) {
            String size
                    = query.get("size").value();
            if (!size.isEmpty()) {
                chartBuilder.columnForSize(size);

                generatedCode.append("  .columnForSize(");
                generatedCode.append(dataAsString(new String[]{size}));
                generatedCode.append(")\n");
            }
        }
        if (query != null && query.hasKey("tracecolors")) {
            String tracecolors
                    = query.get("tracecolors").value();
            if (!tracecolors.isEmpty()) {
                String[] colors = Stream.of(tracecolors.split(" "))
                        .filter(x -> !x.isEmpty())
                        .toArray(String[]::new);
                chartBuilder.setTraceColors(colors);

                generatedCode.append("  .setTraceColors(");
                generatedCode.append(dataAsString(colors));
                generatedCode.append(")\n");
            }
        }
        panel.add(
                new BSCard(new TSFigurePanel(chartBuilder.divName("chart").build(), "chart"),
                        "Chart")
        );

        panel.add(new BSText("<br/>"));
        panel.add(new BSText("<pre>" + generatedCode + "</pre>"));

        return panel;
    }


    protected BSForm createForm() {

        BSForm form = new BSForm("/components/explorer2", true);

        List<Method> datasetSampleMethods = new ArrayList<>();
        for (Method m : TSDataSetFactory.class.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) && m.getName().startsWith("createSample") && m.getParameterCount() == 0) {
                datasetSampleMethods.add(m);
            }
        }
        datasetSampleMethods.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });

        String[] datasets = datasetSampleMethods.stream()
                .map(m -> m.getName().substring("createSample".length()))
                .toArray(String[]::new);


        // Build DataSet List
        BSSelect dataSetList = new BSSelect(true);
        for ( int i = 0; i < datasets.length; i++ ) {
            dataSetList.add(new BSSelectOption(datasets[i]));
        }

        // Build Dimension & Measure List

        Method sampleMethod = datasetSampleMethods.get(0);
        if (query != null && query.hasKey("dataset")) {
            String dataset = query.get("dataset").value();
            for (int i = 0; i < datasets.length; i++) {
                if (datasets[i].equals(dataset)) {
                    //found it
                    sampleMethod = datasetSampleMethods.get(i);
                }
            }
        }

        Table table;
        try {
            table = ((TSDataSet) sampleMethod.invoke(null)).getTSTable();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //shouldn't happen, but add a fallback
            table = TSDataSetFactory.createSampleCountryEconomicData().getTSTable();
        }

        BSSelect dimensionList = new BSSelect(true);
        BSSelect measureList = new BSSelect(true);
        int i = 1;
        for (Column c : table.columns()) {
            if (c instanceof NumericColumn) {
                measureList.add(new BSSelectOption(c.name()));
            } else {
                dimensionList.add(new BSSelectOption(c.name()));
            }
        }


        // DataSet Panel
        QuickBodyPanel dataPanel = new QuickBodyPanel();

        dataPanel.addRowOfColumns(new BSText("<b>Data</b>"));
        dataPanel.addRowOfColumns(dataSetList);

        dataPanel.addRowOfColumns(new BSText("<b>Dimensions</b>"));
        dataPanel.addRowOfColumns(dimensionList);

        dataPanel.addRowOfColumns(new BSText("<b>Measures</b>"));
        dataPanel.addRowOfColumns(measureList);

        // Chart Properties Panel

        QuickBodyPanel chartPropertiesPanel = new QuickBodyPanel();

        {
            BSPanel chartPanel = new BSPanel();
            chartPanel.add(new BSText("<b>Chart</b>:"), BSBorderLayout.WEST);
            chartPanel.add(new BSInputSelect("chartType", false,
                    Stream.of(ChartBuilder.CHART_TYPE.values()).map(t -> t.name()).toArray(String[]::new)), BSBorderLayout.CENTER);

            chartPropertiesPanel.addRowsOfComponents();

            chartPropertiesPanel.addRowOfColumns(new BSText("<b>Chart</b>:"));
            chartPropertiesPanel.addRowOfColumns(new BSInputSelect("chartType", false,
                    Stream.of(ChartBuilder.CHART_TYPE.values()).map(t -> t.name()).toArray(String[]::new)));

            chartPropertiesPanel.addRowOfColumns(new BSText("<b>Options</b>:"));
            BSInputText optionsInput;
            chartPropertiesPanel.addRowOfColumns(optionsInput = new BSInputText("Options", "Options", "", "options"));

            if (query != null && query.hasKey("options")) {
                optionsInput.setValue(query.get("options").value());
            }
        }
        {
            BSInputText colorInput;
            chartPropertiesPanel.addRowOfColumns(new BSText("<b>Color</b>:"), colorInput = new BSInputText("Color", "Color", "", "color"));

            if (query != null && query.hasKey("color")) {
                colorInput.setValue(query.get("color").value());
            }

            chartPropertiesPanel.addRowOfColumns(new BSText("<b>Size</b>:"));
            BSInputText sizeInput;
            chartPropertiesPanel.addRowOfColumns(sizeInput = new BSInputText("Size", "Size", "", "size"));

            if (query != null && query.hasKey("size")) {
                sizeInput.setValue(query.get("size").value());
            }
        }
        {
            chartPropertiesPanel.addRowOfColumns(new BSText("<b>Trace colors</b>:"));
            BSInputText tracecolors;
            chartPropertiesPanel.addRowOfColumns(tracecolors = new BSInputText("", "TraceColor", "", "tracecolors"));

            if (query != null && query.hasKey("tracecolors")) {
                tracecolors.setValue(query.get("tracecolors").value());
            }
        }
        {
            chartPropertiesPanel.addRowOfColumns(new BSText("<b>Axes</b>:"));
            BSInputSelect axes;
            chartPropertiesPanel.addRowOfColumns(axes = new BSInputSelect("axes", false,
                    Stream.of(ChartBuilder.Axes.values()).map(t -> t.name()).toArray(String[]::new)));

            if (query != null && query.hasKey("axes")) {
                //TODO: restore axes type selection
                //axes.setValue(query.get("axes").value());
            }

            chartPropertiesPanel.addRowOfColumns(new BSText("<b>Group By</b>:"));
            BSInputText groupBy;
            chartPropertiesPanel.addRowOfColumns(groupBy = new BSInputText("", "groupby", "", "groupby"));

            if (query != null && query.hasKey("groupby")) {
                groupBy.setValue(query.get("groupby").value());
            }
        }

        // View Panel

        QuickBodyPanel viewPanel = new QuickBodyPanel();

        {
            viewPanel.addRowOfColumns(new BSText("<b>Columns</b>:"));
            BSInputText colsInput;
            viewPanel.addRowOfColumns(colsInput = new BSInputText("Columns", "Columns", "", "columns"));

            if (query != null && query.hasKey("columns")) {
                colsInput.setValue(query.get("columns").value());
            }

            viewPanel.addRowOfColumns(new BSText("<b>Rows</b>:"));
            BSInputText rowsInput;
            viewPanel.addRowOfColumns(rowsInput = new BSInputText("Rows", "Rows", "", "rows"));

            if (query != null && query.hasKey("rows")) {
                rowsInput.setValue(query.get("rows").value());
            }
        }



        dataPanel.doLayout();
        chartPropertiesPanel.doLayout();
        viewPanel.doLayout();


        // Full Panel
        QuickBodyPanel formPanel = new QuickBodyPanel();
        formPanel.addRowOfColumns( dataPanel, chartPropertiesPanel, viewPanel );
        formPanel.doLayout();

        BSBorderedPanel bpanel = new BSBorderedPanel();
        bpanel.add(formPanel);

        form.add(bpanel);

        {
            form.add(new BSFormButton("Submit"));
        }

        return form;

    }

}
