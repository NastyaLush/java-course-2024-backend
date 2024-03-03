package edu.java.scrapper.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.LinksClient;
import edu.java.model.AddLinkRequest;
import edu.java.model.LinkResponse;
import edu.java.model.ListLinksResponse;
import edu.java.model.RemoveLinkRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WireMockTest
public class LinksClientTest {
    public static final String HEADER_NAME = "Tg-Chat-Id";
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    @Test
    public void linksPost_shouldWorkCorrectlyIfSuccess() throws JsonProcessingException {
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        LinkResponse linkResponse = new LinkResponse();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        wireMockExtension.stubFor(WireMock.post("/mock")
                .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(addLinkRequest)))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(objectWriter.writeValueAsString(linkResponse))));

        LinksClient linksClient = new LinksClient(wireMockExtension.baseUrl()+"/mock");

        assertDoesNotThrow(() -> linksClient.linksPost(1L, addLinkRequest));

    }
    @Test
    public void linksPost_shouldThrowErrorIf404() throws JsonProcessingException {
        AddLinkRequest addLinkRequest = new AddLinkRequest();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        wireMockExtension.stubFor(WireMock.post("/mock")
                .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(addLinkRequest)))
                .willReturn(WireMock.aResponse().withStatus(404)));

        LinksClient linksClient = new LinksClient(wireMockExtension.baseUrl()+"/mock");

        assertThrows(WebClientResponseException.class,() -> linksClient.linksPost(1L, addLinkRequest));

    }
    @Test
    public void linksDelete_shouldWorkCorrectlyIfSuccess() throws JsonProcessingException {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest();
        LinkResponse linkResponse = new LinkResponse();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        wireMockExtension.stubFor(WireMock.delete("/mock")
                .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(removeLinkRequest)))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(objectWriter.writeValueAsString(linkResponse))));

        LinksClient linksClient = new LinksClient(wireMockExtension.baseUrl()+"/mock");

        assertDoesNotThrow(() -> linksClient.linksDelete(1L, removeLinkRequest));
    }
    @Test
    public void linksDelete_shouldThrowErrorIf404() throws JsonProcessingException {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        wireMockExtension.stubFor(WireMock.delete("/mock")
                .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                .withRequestBody(WireMock.equalToJson(objectWriter.writeValueAsString(removeLinkRequest)))
                .willReturn(WireMock.aResponse().withStatus(404)));

        LinksClient linksClient = new LinksClient(wireMockExtension.baseUrl()+"/mock");

        assertThrows(WebClientResponseException.class,() -> linksClient.linksDelete(1L, removeLinkRequest));
    }
    @Test
    public void linksGet_shouldWorkCorrectlyIfSuccess() throws JsonProcessingException {
        ListLinksResponse listLinksResponse = new ListLinksResponse();
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        wireMockExtension.stubFor(WireMock.get("/mock")
                .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                .willReturn(WireMock.aResponse().withStatus(200).withHeader("Content-Type", "application/json")
                        .withBody(objectWriter.writeValueAsString(listLinksResponse))));

        LinksClient linksClient = new LinksClient(wireMockExtension.baseUrl()+"/mock");

        assertDoesNotThrow(() -> linksClient.linksGet(1L));
    }
    @Test
    public void linksGet_shouldThrowErrorIf404() throws JsonProcessingException {
        wireMockExtension.stubFor(WireMock.get("/mock")
                .withHeader(HEADER_NAME, WireMock.equalTo("1"))
                .willReturn(WireMock.aResponse().withStatus(404)));

        LinksClient linksClient = new LinksClient(wireMockExtension.baseUrl()+"/mock");

        assertThrows(WebClientResponseException.class,() -> linksClient.linksGet(1L));
    }
}
