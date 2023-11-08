package io.siniavtsev.gatling.genericload;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.siniavtsev.gatling.genericload.scenario.ScenarioSelector;
import io.siniavtsev.gatling.genericload.settings.RequestSettings;
import io.siniavtsev.gatling.genericload.settings.ResponseSettings;
import io.siniavtsev.gatling.genericload.settings.SimulationSettings;

import static io.siniavtsev.gatling.genericload.settings.ResponseSettings.LOG_RESPONSE;

public class GenericSimulation extends Simulation {


    {
        System.out.printf("Start Simulation: %s with %s users per sec during %s minutes\n", RequestSettings.BASE_URL, SimulationSettings.CONSTANT_CONCURRENT_USERS, SimulationSettings.DURATION_MIN);
        System.out.printf("Request URL: %s\n", RequestSettings.BASE_URL + RequestSettings.ENDPOINT + RequestSettings.QUERY_PARAMS);

        if (LOG_RESPONSE) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            Logger logger = loggerContext.getLogger("root");
            logger.setLevel(Level.ALL);
        }

        try {
            setUp(ScenarioSelector.getScenarioForMethod().injectClosed(
                    rampConcurrentUsers(0).to(SimulationSettings.CONSTANT_CONCURRENT_USERS).during(Duration.ofSeconds(SimulationSettings.USER_RAMP_DURING_SEC)),
                    constantConcurrentUsers(SimulationSettings.CONSTANT_CONCURRENT_USERS).during(Duration.ofMinutes(SimulationSettings.DURATION_MIN))))
                    .protocols(getHttpProtocol())
                    .assertions(global().responseTime().percentile3().lt(SimulationSettings.P95_RESPONSE_TIME_MS),
                            global().successfulRequests().percent().gt(SimulationSettings.SUCCESS_PERCENT));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read Request Array from provided JSON\n" + e);
        }
    }

    HttpProtocolBuilder getHttpProtocol() {

        Map<String, String> headers = new HashMap<>();

        if (System.getenv("HEADERS") != null) {
            headers = Arrays.stream(System.getenv("HEADERS").split("\\|"))
                    .map(header -> header.split("="))
                    .collect(Collectors.toMap(key -> key[0], value -> value[1]));

            headers.forEach((key, value) -> System.out.printf("Add custom Header %s=%s\n", key, value));
        }

        return  !(RequestSettings.X_API_KEY == null) && !RequestSettings.X_API_KEY.isBlank() && !RequestSettings.X_API_KEY.isEmpty()
                ? http
                .header("Content-Type", "application/json")
                .header("Accept-Encoding", "gzip")
                .header(RequestSettings.X_API_KEY_HEADER_NAME, RequestSettings.X_API_KEY)
                .headers(headers)
                .baseUrl(RequestSettings.BASE_URL)
                .check(status().is(ResponseSettings.EXPECTED_STATUS_CODE))
                .check(bodyLength().gt(ResponseSettings.EXPECTED_MIN_BODY_LENGTH))
                : http
                .header("Content-Type", "application/json")
                .header("Accept-Encoding", "gzip")
                .headers(headers)
                .baseUrl(RequestSettings.BASE_URL)
                .check(status().is(ResponseSettings.EXPECTED_STATUS_CODE))
                .check(bodyLength().gt(ResponseSettings.EXPECTED_MIN_BODY_LENGTH));
    }

}
