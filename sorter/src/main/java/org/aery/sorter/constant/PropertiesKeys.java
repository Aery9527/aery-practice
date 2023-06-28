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
        public static final String CSV_FILE = PREFIX + ".csv-file";
    }

}
