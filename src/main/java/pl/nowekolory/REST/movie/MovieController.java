package pl.nowekolory.REST.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/movie")
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public Movie getMoviesByIdOrTitle(@RequestParam(required = false, value = "id") String id,
                                 @RequestParam(required = false, value = "title") String title)
    {
        return this.movieService.getMoviesByIdOrTitle(id, title);
    }

    @GetMapping("/search")
    public JsonNode getMoviesBySearch(@RequestParam(required = false, value = "search") String search) throws JsonProcessingException {
        return this.movieService.getMoviesBySearch(search);
    }

    @PostMapping("/favorite")
    public Favorite addFavoriteMovie(@RequestParam("id") String imdbID) {
        return this.movieService.addFavoriteMovie(imdbID);
    }
    @GetMapping("/favorite")
    public List<Movie> getFavoriteMovies() {
        return this.movieService.getFavoriteMovies();
    }
}
