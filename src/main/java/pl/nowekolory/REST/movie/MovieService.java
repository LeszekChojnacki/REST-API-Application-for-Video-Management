package pl.nowekolory.REST.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {
    private MovieRepository movieRepository;
    private final Environment environment;
    public Movie getMoviesByIdOrTitle(String id, String title) {
        RestTemplate restTemplate = new RestTemplate();
        String condition = "";

        if (id != null) {
            condition = condition + "&i=" + id;
        }

        if (title != null) {
            condition = condition + "&t=" + title;
        }

        ResponseEntity<Movie> responseEntity = restTemplate.getForEntity(this.environment.getProperty("rest.api.endpoint") + condition, Movie.class);
        return responseEntity.getBody();
    }

    public JsonNode getMoviesBySearch(String keyword) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(this.environment.getProperty("rest.api.endpoint") + "&s=" + keyword, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseEntity.getBody()).get("Search");
    }

    public List<Movie> getFavoriteMovies() {
        List<Favorite> favorites = this.movieRepository.findAll();
        if (favorites.isEmpty()) {
            return null;
        }
        return favorites.stream().map(favorite -> this.getMoviesByIdOrTitle(favorite.getImdbID(), null)).collect(Collectors.toList());
    }

    public Favorite addFavoriteMovie(String id) {
        Favorite favorite = new Favorite();
        favorite.setImdbID(id);
        return this.movieRepository.save(favorite);
    }
}
