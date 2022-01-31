package com.azure.spring.sample.aad.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@RestController
public class WebClientDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientDemoController.class);

    private static final String GRAPH_ME_ENDPOINT = "https://graph.microsoft.com/v1.0/me";

    @Autowired
    private WebClient webClient;

    @GetMapping("/call-graph")
    public String callGraph(@RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph) {
        return callMicrosoftGraphMeEndpoint(oauth2AuthorizedClient(graph));
    }

    @GetMapping("/call-graph2")
    public String callGraph2() {
        return callMicrosoftGraphMeEndpoint(clientRegistrationId("graph"));
    }

    private String callMicrosoftGraphMeEndpoint(Consumer<Map<String, Object>> attribute) {
        String body = webClient
                .get()
                .uri(GRAPH_ME_ENDPOINT)
                .attributes(attribute)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        LOGGER.info("Response from Graph: {}", body);
        return "Graph response " + (null != body ? "success." : "failed.") + " Body: " + body;
    }
}
