package apitests;

import api.KoelApiSpec;
import io.restassured.response.Response;
import models.playlist.Playlist;
import models.user.User;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.restUtils.RestUtil;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SongsTests extends KoelApiSpec {
    private final String recentlyPlayed = "https://qa.koel.app/api/interaction/recently-played";
    private final String likeUnlikeOneSong = "https://qa.koel.app/api/like";

    @Test
    public void getRecentlyPlayed() {
        Response response = given()
                .spec(getAuthRequestSpec())
                .contentType("application/json")
                .accept("application/json")
                .when()
                .get(recentlyPlayed)
                .then().statusCode(200)
                .extract().response();

        Assert.assertEquals(response.statusCode(), 200);
        RestUtil.getRequestDetailsForLog(response, getAuthRequestSpec());
    }

}
