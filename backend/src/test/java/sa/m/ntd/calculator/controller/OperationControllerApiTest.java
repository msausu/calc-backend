package sa.m.ntd.calculator.controller;

import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import sa.m.ntd.calculator.dto.CalculatorRequest;
import sa.m.ntd.calculator.model.OperationType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
class OperationControllerApiTest {

    private static final String REPORT_ENDPOINT = "http://localhost:8080/api/v1/report";
    private static final String OPERATION_ENDPOINT = "http://localhost:8080/api/v1/calculator";
    private static final String LOGIN_ENDPOINT = "http://localhost:8080/login-form";
    private static final String DOCS_ENDPOINT = "http://localhost:8080/api-docs";
    // Signing key b5498854e238943a22c1f8ccfd4d8523b72b6e8880520e2eb7e72dbc06a17888
    private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZXRlckBnbWFpbC5jb20iLCJpYXQiOjE3MzE0NTEyMTEsImV4cCI6MTczMTQ4NzIxMX0.tMul6Wi7O01iahSGMvSDaz7Ea2z7CsVITe_OFcQlw3I";

    @Autowired
    private RequestSpecification spec;

    @Test
    @Order(1)
    void invalidLoginTest() {
        final String username = "john@gmail.com", password = "john000";
        given()
                .when()
                .log().all()
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body("""
                        { "username": "${username}", "password": "${password}" }
                        """.replace("${username}", username).replace("${password}", password))
                .post(LOGIN_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value()).log().all(); // redirects to login
    }

    @Test
    @Order(2)
    void validLoginTest() {
        final String username = "john@gmail.com", password = "john0000";
        given(spec)
                .filter(did(0, "Get authorization"))
                .when()
                .log().all()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("""
                        { "username": "${username}", "password": "${password}" }
                        """.replace("${username}", username).replace("${password}", password))
                .post(LOGIN_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.OK.value()).log().all();
    }

    @Test
    @Order(4)
    void getReportWithoutTokenTest() {
        given()
                .log().all()
                .get(REPORT_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Order(5)
    void getReportWithInvalidTokenTest() {
        final String peter_at_gmail_dot_com = "peter@gmail.com";
        Arrays.asList(
                REPORT_ENDPOINT,
                REPORT_ENDPOINT + "/" + peter_at_gmail_dot_com + "/last-balance"
                ).forEach(url ->
                        given()
                                .log().all()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + INVALID_TOKEN)
                                .get(url)
                                .then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value())
                );
    }

    @Test
    @Order(6)
    void getReportWithValidTokenTest() {
        final String username = "john@gmail.com", password = "john0000";
        int i = 1;
        List<String> urls = List.of(
                REPORT_ENDPOINT,
                REPORT_ENDPOINT + "/" + username + "/last-balance"
        );
        for(String url : urls) {
            given(spec)
                    .filter(did(i, i++ == 1 ? "Get Report" : "Last Balance"))
                    .header(HttpHeaders.AUTHORIZATION, getAuthorization(username, password))
                    .log().all()
                    .when().get(url)
                    .then().assertThat().statusCode(HttpStatus.OK.value()).log().all();
        }
    }

    @Test
    @Order(10)
    void doAdditionWithInvalidTokenTest() throws JSONException {
        JSONObject operation = new JSONObject();
        operation.put("operation", "ADDITION");
        operation.put("amount", "1 1");
        given().header(HttpHeaders.AUTHORIZATION, "Bearer " + INVALID_TOKEN)
                .contentType(ContentType.JSON).body(operation.toString()).log().all()
                .when().post(OPERATION_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value()).log().all();
    }

    @Test
    @Order(11)
    void doAdditionWithoutTokenTest() throws JSONException {
        JSONObject operation = new JSONObject();
        operation.put("operation", OperationType.ADDITION.name());
        operation.put("amount", "1 1");
        given()
                .contentType(ContentType.JSON).body(operation.toString()).log().all()
                .when().post(OPERATION_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.UNAUTHORIZED.value()).log().all();
    }

    /**
     * Important pre-condition, this should be the first operation otherwise the balance check might be equal
     * user1 does the first operation and user2 has np operation yet
     * @throws JSONException
     */
    @Test
    @Order(12)
    void doAdditionWithValidTokenTest() throws JSONException {
        final String username1 = "mary@gmail.com", password1 = "mary0000", authorization1 = getAuthorization(username1, password1);
        final String username2 = "john@gmail.com", password2 = "john0000", authorization2 = getAuthorization(username2, password2);
        JSONObject operation = new JSONObject();
        operation.put("operation", OperationType.ADDITION.name());
        operation.put("amount", "1 1");
        given(spec)
                .filter(did(3, "Addition"))
                .header("Authorization", authorization1)
                .contentType(ContentType.JSON).body(operation.toString()).log().all()
                .when().post(OPERATION_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.OK.value()).log().all();
        // check balances are per user
        String balance1 = given()
                .header(HttpHeaders.AUTHORIZATION, authorization1)
                .log().all()
                .when().get(REPORT_ENDPOINT + "/" + username1 + "/last-balance")
                .getBody().asString();
        String balance2 = given()
                .header(HttpHeaders.AUTHORIZATION, authorization2)
                .log().all()
                .when().get(REPORT_ENDPOINT + "/" + username2 + "/last-balance")
                .getBody().asString();
        assertTrue(balance1 != null && !balance1.isEmpty());
        assertTrue(balance2 != null && !balance2.isEmpty());
        assertNotEquals(balance1, balance2);
    }

    @Test
    @Order(13)
    void doDeleteRecordWithValidTokenTest() {
        final String username = "john@gmail.com", password = "john0000", authorization = getAuthorization(username, password);
        Response report1 = given()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .log().all()
                .when().get(REPORT_ENDPOINT);
        String reportBody1 = report1.getBody().asString();
        JsonPath jsonPath = new JsonPath(reportBody1);
        String id = jsonPath.getString("content[0].id");
        given(spec)
                .filter(did(4, "(Soft) Delete Record"))
                .header("Authorization", authorization)
                .contentType(ContentType.JSON).body("true").log().all()
                .when().put(REPORT_ENDPOINT + "/" + id + "/is-excluded")
                .then().assertThat().statusCode(HttpStatus.OK.value()).log().all();
        // check id is absent
        String reportBody2 = given()
                .header(HttpHeaders.AUTHORIZATION, authorization)
                .log().all()
                .when().get(REPORT_ENDPOINT).getBody().asString();
        assertFalse(reportBody2.contains(id));
    }

    @Test
    @Order(14)
    void doOperationHavingNoBalanceWithValidTokenTest() {
        final String username = "john@gmail.com", password = "john0000", authorization = getAuthorization(username, password);
        final float randomStringCost = 0.75f;
        final int initialBalance = 100, numAllowedOperations = (int)(initialBalance / randomStringCost);
        CalculatorRequest operation;
        // the next loop with exhaust the balance
        for (int i = 0; i < numAllowedOperations; i++) {
            operation = CalculatorRequest.builder()
                    .operation(OperationType.SQUARE_ROOT)
                    .amount(String.valueOf(i))
                    .build();
            Response response = given()
                    .header("Authorization", authorization)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON).body(new Gson().toJson(operation)).log().all()
                    .when().post(OPERATION_ENDPOINT);
            response.then().assertThat().statusCode(HttpStatus.OK.value()).log().all();
            String body = response.getBody().asString().trim();
            JsonPath jsonPath = new JsonPath(body);
            String operationResponse = jsonPath.getString("operationResponse");
            assertFalse(operationResponse == null || operationResponse.contains("Insufficient"));
        }
        // verify insufficent balance
        operation = CalculatorRequest.builder()
                .operation(OperationType.SQUARE_ROOT)
                .amount(String.valueOf(3))
                .build();
        String body = given()
                .header("Authorization", authorization)
                .contentType(ContentType.JSON).body(new Gson().toJson(operation)).log().all()
                .when().post(OPERATION_ENDPOINT)
                .getBody().asString().trim();
        JsonPath jsonPath = new JsonPath(body);
        String operationResponse = jsonPath.getString("operationResponse");
        assertTrue(operationResponse.contains("Insufficient"));
    }

    @Test
    @Order(20)
    void getApiDocsWithValidTokenTest() {
        final String username = "mary@gmail.com", password = "mary0000";
        given(spec)
                .filter(did(5, "OpenAPI"))
                .header("Authorization", getAuthorization(username, password))
                .accept(ContentType.JSON).log().all()
                .when().get(DOCS_ENDPOINT)
                .then().assertThat().statusCode(HttpStatus.OK.value()).log().all();
    }

    private String getAuthorization(String username, String password) {
        Response response = given()
                .when()
                .log().all()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body("""
                        { "username": "${username}", "password": "${password}" }
                        """.replace("${username}", username).replace("${password}", password))
                .post(LOGIN_ENDPOINT);
        assert response.statusCode() == HttpStatus.OK.value();
        return response.header(HttpHeaders.AUTHORIZATION);
    }

    private RestDocumentationFilter did(int documentCounter, String it) {
        return document(String.format("%02d-", documentCounter) + it);
    }
}