package io.siniavtsev.gatling.genericload.settings;

public class EnvironmentVariables {

    static double getValueOrDefault(String key, double fallback) {
        String value = System.getenv(key);
        if (value == null) {
            return fallback;
        }
        return Double.parseDouble(value);
    }

    static int getValueOrDefault(String key, int fallback) {
        String value = System.getenv(key);
        if (value == null) {
            return fallback;
        }
        return Integer.parseInt(value);
    }

    static String getValueOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        if (value == null) {
            return fallback;
        }
        return value;
    }

    static boolean getValueOrDefault(String key, boolean fallback) {
        String value = System.getenv(key);
        if (value == null) {
            return fallback;
        }
        return Boolean.parseBoolean(value);
    }

}
