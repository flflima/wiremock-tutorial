package br.com.tutorial.wiremock.test

import com.github.kittinunf.fuel.httpGet
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.apache.http.HttpStatus
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
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
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

        assertEquals(HttpStatus.SC_OK, response.statusCode)

        verify(
            getRequestedFor(urlMatching("/my/resource"))
//                .withRequestBody(matching(".*<message>1234</message>.*"))
                .withHeader("Accept", notMatching("application/json"))
        )
    }
}