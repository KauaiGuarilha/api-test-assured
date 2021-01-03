package br.com.kauai.tasks.apitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class APIRestTest {

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void shouldReturnTasks(){
        RestAssured.given()
                .log().all()
                .when()
                .get("/todo")
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test
    public void shouldReturnTasksSuccessfully(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"task\": \"Teste via API\", \"dueDate\": \"2021-01-03\"}")
                .when()
                .post("/todo")
                .then()
                .statusCode(201);
    }

    @Test
    public void shouldNotReturnTasksWithAnInvalidDate(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"task\": \"Teste via API\", \"dueDate\": \"2020-01-03\"}")
                .when()
                .post("/todo")
                .then()
                .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));
    }

    @Test
    public void shouldntAddTasksWithoutTasks(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"task\": \"\", \"dueDate\": \"2020-01-03\"}")
                .when()
                .post("/todo")
                .then()
                .statusCode(400)
                .body("message", CoreMatchers.is("Fill the task description"));
    }

    @Test
    public void shouldNotAddWithTasksNull(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"dueDate\": \"2020-01-03\"}")
                .when()
                .post("/todo")
                .then()
                .statusCode(400)
                .body("message", CoreMatchers.is("Fill the task description"));
    }

    @Test
    public void shouldNotAddWithDueDateNull(){
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"task\": \"Teste via API\"}")
                .when()
                .post("/todo")
                .then()
                .statusCode(400)
                .body("message", CoreMatchers.is("Fill the due date"));
    }
}
