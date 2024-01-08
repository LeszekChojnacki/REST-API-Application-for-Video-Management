package pl.nowekolory.REST.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.nowekolory.REST.exception.ResponseBodyFormatException;
import pl.nowekolory.REST.exception.ResponseNullException;
import pl.nowekolory.REST.integration.OmdbIntegration;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {
    private MovieRepository movieRepository;
    private OmdbIntegration omdbIntegration;
    public Movie getMoviesByIdOrTitle(String id, String title) {
        String response = this.omdbIntegration.searchByIdOrTitle(id, title);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode searchNode = objectMapper.readTree(response);
        return objectMapper.convertValue(searchNode, new TypeReference<Movie>(){});
        } catch (JsonProcessingException e) {
            throw new ResponseBodyFormatException(String.format("REST Response body JSON format not compatible: %s", response));
        } catch (IllegalArgumentException e) {
            throw new ResponseNullException(String.format("REST Response NULL: %s", response));
        }
    }

    public List<Movie> getMoviesBySearch(String keyword) {
        String response = this.omdbIntegration.searchByKeyword(keyword);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode searchNode = objectMapper.readTree(response).get("Search");
            return objectMapper.convertValue(searchNode, new TypeReference<List<Movie>>(){});
        } catch (JsonProcessingException e) {
            throw new ResponseBodyFormatException(String.format("REST Response body JSON format not compatible: %s", response));
        } catch (IllegalArgumentException e) {
            throw new ResponseNullException(String.format("REST Response NULL: %s", response));
        }
    }

    public List<Movie> getFavoriteMovies() {
        List<Favorite> favorites = this.movieRepository.findAll();
        return favorites.stream().map(favorite -> this.getMoviesByIdOrTitle(favorite.getImdbID(), null)).collect(Collectors.toList());
    }

    public Favorite addFavoriteMovie(String id) {
        Favorite favorite = new Favorite();
        favorite.setImdbID(id);
        return this.movieRepository.save(favorite);
    }
}
