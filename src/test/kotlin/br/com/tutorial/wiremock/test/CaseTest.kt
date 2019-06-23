package br.com.tutorial.wiremock.test

import com.github.kittinunf.fuel.httpGet
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import org.apache.http.HttpStatus
import org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR
import org.apache.http.HttpStatus.SC_OK
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.BeforeEach

class CaseTest {

    @get:Rule
    var wireMockRule = WireMockRule(8089)

    @BeforeEach
    fun init() {

    }

    @Test
    fun testing() {
        stubFor(
            get(urlEqualTo("/my/resource"))
                .inScenario("To do list")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("success")
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(
                    aResponse()
                        .withStatus(SC_INTERNAL_SERVER_ERROR)
                        .withBody("<response>ERROR</response>")
                )
        )


        stubFor(
            get(urlEqualTo("/my/resource"))
                .inScenario("To do list")
                .whenScenarioStateIs("success")
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(
                    aResponse()
                        .withStatus(SC_OK)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")
                )
        )

//        StubForTest().start()

        val (_, response, _) = "http://localhost:8089/my/resource"
            .httpGet()
            .header(mapOf("Accept" to "text/xml"))
            .response()


        println(String(response.data))

        assertEquals(SC_INTERNAL_SERVER_ERROR, response.statusCode)

        val (_, response2, _) = "http://localhost:8089/my/resource"
            .httpGet()
            .header(mapOf("Accept" to "text/xml"))
            .response()


        println(String(response2.data))

        assertEquals(SC_OK, response2.statusCode)

        verify(
            getRequestedFor(urlMatching("/my/resource"))
//                .withRequestBody(matching(".*<message>1234</message>.*"))
                .withHeader("Accept", notMatching("application/json"))
        )
    }
}