import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;


@WireMockTest(httpPort = 9876)
public class MockApiTest {
    //Test class for mocking API responses

    String successResponse = "{coord:{lon:-0.1257,lat:51.5085},weather:[{id:804,main:Clouds,description:overcast clouds,icon:04d}],base:stations,main:{temp:13.13,feels_like:12.69,temp_min:12.81,temp_max:14.23,pressure:1010,humidity:84,sea_level:1010,grnd_level:1007},visibility:10000,wind:{speed:6.17,deg:220},clouds:{all:100},dt:1775217704,sys:{type:2,id:2075535,country:GB,sunrise:1775194293,sunset:1775241345},timezone:3600,id:2643743,name:London,cod:200}";
    String failureResponse = "HTTP GET request failed with response code: 401";

    String endpoint = "/data/2.5/weather?q=London";

    private RequestSpecification requestSpec;

    @BeforeEach
    public void createRequestSpec() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(9876).
                build();
    }

    //Setup GET request stub - 200 Response
    public void setupGetWeatherApiStubSuccess() {

        stubFor(
                get(
                        urlEqualTo(endpoint)
                )
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withStatus(200)
                                        .withBody(successResponse))
        );
    }

    //Setup GET request stub - 401 Response
    public void setupGetWeatherApiStubFailure() {

        stubFor(
                get(
                        urlEqualTo(endpoint)
                )
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json")
                                        .withStatus(401)
                                        .withBody(failureResponse))
        );
    }

    //Test GET request - 200 Response
    @Test
    void testGetWeatherApiSuccess()  {

        setupGetWeatherApiStubSuccess();

        given().
                when().
                spec(requestSpec).
                get(endpoint).
                then().
                assertThat().
                statusCode(200).
                and().
                body(org.hamcrest.Matchers.equalTo(successResponse));
    }

    //Test GET request - 401 Response
    @Test
    void testGetWeatherApiFailure()  {

        setupGetWeatherApiStubFailure();

        given().
                when().
                spec(requestSpec).
                get(endpoint).
                then().
                assertThat().
                statusCode(401).
                and().
                body(org.hamcrest.Matchers.equalTo(failureResponse));
    }
}
