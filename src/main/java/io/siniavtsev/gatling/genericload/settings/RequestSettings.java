package io.siniavtsev.gatling.genericload.settings;


import static io.siniavtsev.gatling.genericload.settings.EnvironmentVariables.getValueOrDefault;

public class RequestSettings {

    public static final String X_API_KEY_HEADER_NAME = "x-api-key";
    public static final String X_API_KEY = System.getenv("X_API_KEY");
    public static final String BASE_URL = System.getenv("BASE_URL");
    public static final String ENDPOINT = System.getenv("ENDPOINT");
    public static final String QUERY_PARAMS = getValueOrDefault("QUERY_PARAMS", "");
    public static final String HTTP_METHOD = getValueOrDefault("HTTP_METHOD", "GET");
    public static boolean REQUEST_BODY_IS_ARRAY = getValueOrDefault("REQUEST_BODY_IS_ARRAY", false);

}
