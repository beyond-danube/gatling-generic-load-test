package io.siniavtsev.gatling.genericload.scenario;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.siniavtsev.gatling.genericload.settings.RequestSettings;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.siniavtsev.gatling.genericload.settings.RequestSettings.REQUEST_BODY_IS_ARRAY;

public class ScenarioSelector {

    static final String SCENARIO_DESCRIPTION = "Generic request, method: " + RequestSettings.HTTP_METHOD;
    static final String ENDPOINT_DESCRIPTION = RequestSettings.HTTP_METHOD + ": " + RequestSettings.ENDPOINT;
    static final String REQUEST_BODY_FILENAME = "request_body.json";
    static final String REQUEST_BODY_ARRAY_MAP = "src/main/resources/request_body_array_map.txt";

    public static ScenarioBuilder getScenarioForMethod() throws IOException {

        ScenarioBuilder selectedScenario = null;

        switch (RequestSettings.HTTP_METHOD) {
            case "GET" : selectedScenario = scenario(SCENARIO_DESCRIPTION)
                    .exec(http(ENDPOINT_DESCRIPTION)
                            .get(RequestSettings.ENDPOINT)
                            .queryParamMap(getQueryParams()));
            break;

            case "POST" : {
                if (REQUEST_BODY_IS_ARRAY) {

                    var feeder = jsonFile(REQUEST_BODY_FILENAME).random();

                    File file = new File(REQUEST_BODY_ARRAY_MAP);
                    String content = FileUtils.readFileToString(file, "UTF-8");

                    selectedScenario = scenario(SCENARIO_DESCRIPTION)
                            .feed(feeder)
                            .exec(http(ENDPOINT_DESCRIPTION)
                                    .post(RequestSettings.ENDPOINT)
                                    .body(StringBody(content))
                                    .queryParamMap(getQueryParams()));

                } else {
                    selectedScenario = scenario(SCENARIO_DESCRIPTION)
                            .exec(http(ENDPOINT_DESCRIPTION)
                                    .post(RequestSettings.ENDPOINT)
                                    .body(RawFileBody(REQUEST_BODY_FILENAME))
                                    .queryParamMap(getQueryParams()));
                }
            }

        }

        return selectedScenario;
    }

    static Map<String, Object> getQueryParams() {
        Map<String, Object> params = new HashMap<>();

        var paramList = Arrays.stream(RequestSettings.QUERY_PARAMS.replace("?", "").split("&")).collect(Collectors.toList());

        paramList.forEach(paramItem -> {
            if (!paramItem.isBlank() && !paramItem.isEmpty()) {
                var param = paramItem.split("=");
                params.put(param[0], param[1]);
            }
        });

        return params;
    }
}
