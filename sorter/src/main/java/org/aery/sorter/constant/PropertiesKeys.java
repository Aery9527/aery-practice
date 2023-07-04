package org.aery.sorter.constant;

public class PropertiesKeys {

    public static final String PREFIX = "sorter";

    public static class Sorter {
        public static final String DATE_PREFIX = PREFIX + ".date";
    }

    public static class Provider {
        private static final String PREFIX = PropertiesKeys.PREFIX + ".provider";
        public static final String CSV_FILE = PREFIX + ".csv-file";
    }

    public static class Exporter {

        private static final String PREFIX = PropertiesKeys.PREFIX + ".exporter";

        /**
         * value mapping to {@link ExporterUse}
         */
        public static final String USE = PREFIX + ".use";

        public static final String TABLE_FILE = PREFIX + "." + ExporterUse.TABLE;

        public static final String CSV_FILE = PREFIX + "." + ExporterUse.CSV;
    }

    public static class ExporterUse {

        public static final String TABLE = "table-file";

        public static final String CSV = "csv-file";

    }

}
