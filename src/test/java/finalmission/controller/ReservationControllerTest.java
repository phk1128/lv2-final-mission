package finalmission.controller;

import finalmission.dto.LoginRequest;
import finalmission.dto.ReservationRequest;
import finalmission.dto.ReservationUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationControllerTest {

    private String sessionId;

    @BeforeEach
    void setUp() {
        final LoginRequest loginRequest = new LoginRequest("test", "1234");
        sessionId = RestAssured.given().contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .extract().cookie("JSESSIONID");
    }

    @Test
    void addReservationTest() {
        final ReservationRequest reservationRequest = new ReservationRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .sessionId(sessionId)
                .body(reservationRequest)
                .when()
                .post("/reservation")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void getReservationsTest() {

        final ReservationRequest reservationRequest = new ReservationRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .sessionId(sessionId)
                .body(reservationRequest)
                .when()
                .post("/reservation")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .sessionId(sessionId)
                .when()
                .get("/reservation")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasSize(greaterThanOrEqualTo(1)))
                .body("email", everyItem(equalTo("test")))
                .body("numberOfPeople", hasItem(4));
    }

    @Test
    void updateReservationTest() {
        final ReservationRequest reservationRequest = new ReservationRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
        final int reservationId = RestAssured.given()
                .contentType(ContentType.JSON)
                .sessionId(sessionId)
                .body(reservationRequest)
                .when()
                .post("/reservation")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");


        final ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                LocalDate.of(2025, 12, 26), 
                10, 
                12, 
                3
        );
        
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .sessionId(sessionId)
                .body(updateRequest)
                .when()
                .patch("/reservation/" + reservationId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(reservationId))
                .body("email", equalTo("test"))
                .body("startTime", equalTo(10))
                .body("endTime", equalTo(12))
                .body("numberOfPeople", equalTo(3));
    }

    @Test
    void deleteReservationTest() {
        final ReservationRequest reservationRequest = new ReservationRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
        final int reservationId = RestAssured.given()
                .contentType(ContentType.JSON)
                .sessionId(sessionId)
                .body(reservationRequest)
                .when()
                .post("/reservation")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .sessionId(sessionId)
                .when()
                .delete("/reservation/" + reservationId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
