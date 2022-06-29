import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;


public class AuthorisationError {

    @Test
    public void invalidApiAccessKeyTest() {
        Response response = given().get(Consts.URL + "apikey=NQJPIU9tBh4C19TXIMC5jK63t4fJY3BX");
        System.out.println(response.asString());
        response.then().statusCode(404);
    }

    @Test
    public void noAccessKeyTest() {
        Response response = given().get(Consts.URL + "/live");
        System.out.println(response.asString());
        response.then().statusCode(101);
        response.then().body("message", equalTo("No API key found in request"));
    }

    @Test
    public void incorrectCurrencyCodeLiveTest() {
        Response response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + Consts.API_ACCESS_KEY + "&currencies=CAN");
        System.out.println(response.asString());
        response.then().body("error.code", equalTo(202));
        response.then().body("error.info", containsString("one or more invalid Currency Codes"));
    }

    @Test
    public void dateParamIsMissingTest() {
        Response response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + Consts.API_ACCESS_KEY);
        System.out.println(response.asString());
        response.then().body("error.code", equalTo(301));
        response.then().body("error.info", containsString("You have not specified a date"));
    }

    @Test
    public void invalidDateTest() {
        Response response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "date=2020&" + Consts.API_ACCESS_KEY);
        System.out.println(response.asString());
        response.then().body("error.code", equalTo(302));
        response.then().body("error.info", containsString("You have entered an invalid date"));
    }

    @Test
    public void incorrectCurrencyCodeHistoricalTest() {
        Response response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "date=2020-01-02&" + Consts.API_ACCESS_KEY + "&source=EUR&currencies=EUO");
        System.out.println(response.asString());
        response.then().body("error.code", equalTo(202));
        response.then().body("error.info", containsString("one or more invalid Currency Codes"));
    }
}


