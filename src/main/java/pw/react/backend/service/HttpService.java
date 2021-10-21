package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import pw.react.backend.web.Quote;

public class HttpService implements HttpClient {

    private final Logger logger = LoggerFactory.getLogger(HttpService.class);

    private final RestTemplate restTemplate;

    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Quote consume(String url) {
        final Quote quote = restTemplate.getForObject(url, Quote.class);
        if (quote != null) {
            logger.info("This is Quote: {}", quote.toString());
        } else {
            logger.warn("Quote is null");
        }
        return quote;
    }
}
