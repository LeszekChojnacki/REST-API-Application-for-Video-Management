package pl.nowekolory.REST.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    private final String BASE_URL = "https://www.omdbapi.com/?apikey=26eac013";
    public Movie getMoviesByIdOrTitle(String id, String title) {
        RestTemplate restTemplate = new RestTemplate();
        String url = this.BASE_URL;

        if (id != null) {
            url = url + "&i=" + id;
        }

        if (title != null) {
            url = url + "&t=" + title;
        }

        ResponseEntity<Movie> responseEntity = restTemplate.getForEntity(url, Movie.class);
        return responseEntity.getBody();
    }

    public JsonNode getMoviesBySearch(String search) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(this.BASE_URL + "&s=" + search, String.class);
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
