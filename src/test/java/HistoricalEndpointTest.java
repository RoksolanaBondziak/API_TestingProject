import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class HistoricalEndpointTest {
    public static Response response;

    @BeforeAll
    public static void setup() {
        response = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "date=2020-01-02&" + Consts.API_ACCESS_KEY);
        System.out.println(response.asString());
    }

    @Test
    public void historicalResponsesCodeTest() {
        response.then().statusCode(200);
    }

    @Test
    public void getResponseTest() {
        response.then().body("success", notNullValue());
        response.then().body("success", equalTo(true));
        response.then().body("historical", notNullValue());
        response.then().body("historical", equalTo(true));
        response.then().body("source", notNullValue());
        response.then().body("source", equalTo("USD"));
    }

    @Test
    public void currenciesResultTest() {
        response.then().body("quotes", notNullValue());
        response.then().body("quotes", hasKey("USDCAD"));
        response.then().body("quotes", hasKey("USDEUR"));
        response.then().body("quotes", hasKey("USDRUB"));
        response.then().body("quotes", hasKey("USDNIS"));
    }

    @Test
    public void currenciesParamByDefaultTest() {
        Response response1 = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "date=2020-01-02&" + Consts.API_ACCESS_KEY + "&currencies=CAD, EUR, RUB, NIS");
        System.out.println(response1.asString());
        response1.then().body("quotes", hasKey("USDCAD"));
        response1.then().body("quotes", hasKey("USDEUR"));
        response1.then().body("quotes", hasKey("USDRUB"));
        response1.then().body("quotes", hasKey("USDNIS"));
    }

    @Test
    public void currenciesParamTest() {
        Response response2 = given().get(Consts.URL + Consts.HISTORICAL_ENDPOINT + "date=2020-01-02&" + Consts.API_ACCESS_KEY + "&source=EUR&currencies=CAD, EUR, RUB, NIS");
        System.out.println(response2.asString());
        response2.then().body("quotes", hasKey("EURCAD"));
        response2.then().body("quotes", hasKey("EUREUR"));
        response2.then().body("quotes", hasKey("EURRUB"));
        response2.then().body("quotes", hasKey("USDNIS"));
    }

}

