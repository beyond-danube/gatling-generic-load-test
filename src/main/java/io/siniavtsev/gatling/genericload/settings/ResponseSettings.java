package io.siniavtsev.gatling.genericload.settings;


import static io.siniavtsev.gatling.genericload.settings.EnvironmentVariables.getValueOrDefault;

public class ResponseSettings {
    public static final int EXPECTED_STATUS_CODE = getValueOrDefault("EXPECTED_STATUS_CODE", 200);
    public static final int EXPECTED_MIN_BODY_LENGTH = getValueOrDefault("EXPECTED_MIN_BODY_LENGTH", 100);
    public static final boolean LOG_RESPONSE = getValueOrDefault("LOG_RESPONSE", false);

}
