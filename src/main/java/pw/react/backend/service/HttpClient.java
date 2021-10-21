package pw.react.backend.service;

import pw.react.backend.web.Quote;

public interface HttpClient {
    Quote consume(String url);
}
