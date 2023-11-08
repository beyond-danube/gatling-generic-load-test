package io.siniavtsev.gatling.genericload.settings;


import static io.siniavtsev.gatling.genericload.settings.EnvironmentVariables.getValueOrDefault;

public class SimulationSettings {

    public static final int CONSTANT_CONCURRENT_USERS = getValueOrDefault("CONSTANT_CONCURRENT_USERS", 10);
    public static final long DURATION_MIN = getValueOrDefault("DURATION_MIN", 1);
    public static final long USER_RAMP_DURING_SEC = getValueOrDefault("USER_RAMP_DURING_SEC", 15);
    public static final int P95_RESPONSE_TIME_MS = getValueOrDefault("P95_RESPONSE_TIME_MS", 1000);
    public static final double SUCCESS_PERCENT = getValueOrDefault("SUCCESS_PERCENT", 95.0);
}
