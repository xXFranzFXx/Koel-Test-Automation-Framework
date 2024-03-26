package util.restUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import models.playlist.Playlist;
import util.listeners.TestListener;

import java.util.*;
import java.util.stream.Collectors;

public class RestUtil {
    private static RequestSpecification getRequestSpecification(String endPoint,  Map<String, String> headers) {
        return RestAssured.given()
                .baseUri(endPoint)
                .headers(headers)
                .contentType(ContentType.JSON);
    }

    private static void printRequestLogInReport(RequestSpecification requestSpecification, String jsonObject) {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
        TestListener.logInfoDetails("Endpoint is " + queryableRequestSpecification.getBaseUri());
        TestListener.logInfoDetails("Method is " + queryableRequestSpecification.getMethod());
        TestListener.logInfoDetails("Headers are ");
        TestListener.logHeaders(queryableRequestSpecification.getHeaders().asList());
        TestListener.logInfoDetails("Request body is ");
        TestListener.logJson(jsonObject);
    }
    private static void printRequestLogInReport(RequestSpecification requestSpecification) {
        QueryableRequestSpecification queryableRequestSpecification = SpecificationQuerier.query(requestSpecification);
        TestListener.logInfoDetails("Endpoint is " + queryableRequestSpecification.getBaseUri());
        TestListener.logInfoDetails("Method is " + queryableRequestSpecification.getMethod());
        TestListener.logInfoDetails("Headers are ");
        TestListener.logHeaders(queryableRequestSpecification.getHeaders().asList());
        TestListener.logInfoDetails("Request params are ");
        TestListener.logInfoDetails(queryableRequestSpecification.getRequestParams().toString());
    }

    private static void printResponseLogInReport(Response response) {
        TestListener.logInfoDetails("Response status is " + response.getStatusCode());
        TestListener.logInfoDetails("Response Headers are ");
        TestListener.logHeaders(response.getHeaders().asList());
        TestListener.logInfoDetails("Response body is ");
        TestListener.logJson(response.getBody().prettyPrint());
    }


    public static void getRequestDetailsForLog(Response response, RequestSpecification requestSpecification, String requestPayload) {
        printRequestLogInReport(requestSpecification, requestPayload);
        printResponseLogInReport(response);
    }
    public static void getRequestDetailsForLog(Response response, RequestSpecification requestSpecification) {
        printRequestLogInReport(requestSpecification);
        printResponseLogInReport(response);
    }
    public static List<String> getPlaylistNames(Response response) {
        Playlist[] playlists = response.as(Playlist[].class);
        return Arrays.stream(playlists).map(Playlist::getName).collect(Collectors.toList());
    }
    public boolean playlistExists(Response response, String playlistName) {
          return  getPlaylistNames(response).contains(playlistName);
    }

}

