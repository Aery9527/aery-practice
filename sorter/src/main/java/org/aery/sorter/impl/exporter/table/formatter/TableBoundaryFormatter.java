package org.aery.sorter.impl.exporter.table.formatter;

import java.util.List;

public class TableBoundaryFormatter {

    private final String cornerDelimiter;

    private final String lineDelimiter;

    private final String rowDelimiter;

    private final int[] columnMaxLength;

    private final String boundary;

    public TableBoundaryFormatter(String cornerDelimiter, String lineDelimiter, String rowDelimiter, int[] columnMaxLength) {
        this.cornerDelimiter = cornerDelimiter;
        this.lineDelimiter = " " + lineDelimiter + " ";
        this.rowDelimiter = rowDelimiter;
        this.columnMaxLength = columnMaxLength;
        this.boundary = genBoundary(columnMaxLength);
    }

    public void printTitle(StringBuilder sb, List<TableCellFormatter> formatterList) {
        String systemLineSeparator = System.lineSeparator();

        sb.append(this.boundary).append(systemLineSeparator);

        sb.append(this.lineDelimiter);
        for (int index = 0; index < formatterList.size(); index++) {
            TableCellFormatter formatter = formatterList.get(index);
            String key = formatter.getKey();
            int length = this.columnMaxLength[index];
            sb.append(String.format("%-" + length + "s", key)).append(this.lineDelimiter);
        }
        sb.append(systemLineSeparator);

        sb.append(this.boundary);
    }

    public void printTail(StringBuilder sb) {
        sb.append(this.boundary);
    }

    private String genBoundary(int[] columnMaxLength) {
        StringBuilder boundary = new StringBuilder(" ").append(this.cornerDelimiter);
        for (int length : columnMaxLength) {
            length += 3;
            for (int i = 0; i < length; i++) {
                boundary.append(this.rowDelimiter);
            }
        }
        boundary.delete(boundary.length() - 1, boundary.length());
        boundary.append(this.cornerDelimiter);

        return boundary.toString();
    }

}
