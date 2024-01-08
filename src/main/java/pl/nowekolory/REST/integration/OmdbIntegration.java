package pl.nowekolory.REST.integration;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
@AllArgsConstructor
public class OmdbIntegration {
    private final Environment environment;
    public String searchByKeyword(String keyword) {
        String condition = "&s=" + keyword;
        return this.executeSearch(condition);
    }

    public String searchByIdOrTitle(String id, String title) {
        String condition = "";

        if (id != null) {
            condition = condition + "&i=" + id;
        }

        if (title != null) {
            condition = condition + "&t=" + title;
        }

        return this.executeSearch(condition);
    }

    private String executeSearch(String condition) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForEntity(this.environment.getProperty("rest.api.endpoint") + condition, String.class).getBody();
    }
}
