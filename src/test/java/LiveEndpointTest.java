import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class LiveEndpointTest {
    public static Response response;

    @BeforeAll
    public static void setup() {
        response = given().get(Consts.URL + Consts.LIVE_ENDPOINT + Consts.API_ACCESS_KEY);
        System.out.println(response.asString());
    }

    @Test
    public void lifeResponsesCodeTest() {
        response.then().statusCode(200);
    }

    @Test
    public void getResponseTest() {
        response.then().body("success", notNullValue());
        response.then().body("success", equalTo(true));
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
        Response response1 = given().get(Consts.URL + Consts.LIVE_ENDPOINT + Consts.API_ACCESS_KEY + "&currencies=CAD, EUR, RUB, NIS");
        System.out.println(response1.asString());
        response1.then().body("quotes", hasKey("USDCAD"));
        response1.then().body("quotes", hasKey("USDEUR"));
        response1.then().body("quotes", hasKey("USDRUB"));
        response1.then().body("quotes", hasKey("USDNIS"));
    }

    @Test
    public void currenciesParamTest() {
        Response response2 = given().get(Consts.URL + Consts.LIVE_ENDPOINT + Consts.API_ACCESS_KEY + "&source=EUR&currencies=CAD, EUR, RUB, NIS");
        System.out.println(response2.asString());
        response2.then().body("quotes", hasKey("EURCAD"));
        response2.then().body("quotes", hasKey("EUREUR"));
        response2.then().body("quotes", hasKey("EURRUB"));
        response2.then().body("quotes", hasKey("USDNIS"));
    }

    @Test
    public void timeStampTest() {
        String expected = LocalDate.now().toString();
        System.out.println(expected);
        Integer actualMs = response.path("timestamp");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date((long) actualMs * 1000);
        String actual = format.format(date2.getTime());
        System.out.println(actual);

        Assertions.assertEquals(expected, actual);
    }

}
