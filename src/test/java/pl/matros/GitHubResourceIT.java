package pl.matros;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusIntegrationTest
public class GitHubResourceIT {

    private final String testUsername = "octocat"; //real test user from github

    @Test
    public void testGetRepositoriesHappyPath() {
        given()
                .when()
                .get("/api/v1/repositories/" + testUsername)
                .then()
                // Then verify the response
                .statusCode(200)
                .contentType(ContentType.JSON)
                // Verify that repositories are returned (octocat has several non-fork repos)
                .body("$", hasSize(greaterThan(0)))
                // Test the first repository in the response
                .body("[0].name", notNullValue())
                .body("[0].ownerLogin", is(testUsername))
                .body("[0].branches", hasSize(greaterThan(0)))
                // Test the first branch of the first repository
                .body("[0].branches[0].name", notNullValue())
                .body("[0].branches[0].lastCommitSha", matchesPattern("^[a-f0-9]{40}$"));
    }
}