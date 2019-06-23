package br.com.tutorial.wiremock.test

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule

class StubForTest {

    @get:Rule
    var wireMockRule = WireMockRule(8089)

    fun start() {
        WireMock.stubFor(
            WireMock.get(WireMock.urlEqualTo("/my/resource"))
                .withHeader("Accept", WireMock.equalTo("text/xml"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")
                )
        )

    }
}