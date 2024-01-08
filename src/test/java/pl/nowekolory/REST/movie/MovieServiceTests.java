package pl.nowekolory.REST.movie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.nowekolory.REST.exception.ResponseBodyFormatException;
import pl.nowekolory.REST.exception.ResponseNullException;
import pl.nowekolory.REST.integration.OmdbIntegration;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
    @Mock
    private OmdbIntegration omdbIntegration;
    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    private MovieService movieService;
    @Test
    public void getMoviesBySearch_FormatSuccessTest() {
        String response = """
                {
                    "Search": [
                        {
                            "Title": "Scary Movie",
                            "Year": "2000",
                            "imdbID": "tt0175142",
                            "Type": "movie",
                            "Poster": "https://m.media-amazon.com/images/M/MV5BMGEzZjdjMGQtZmYzZC00N2I4LThiY2QtNWY5ZmQ3M2ExZmM4XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg"
                        },
                        {
                            "Title": "Scary Movie 2",
                            "Year": "2001",
                            "imdbID": "tt0257106",
                            "Type": "movie",
                            "Poster": "https://m.media-amazon.com/images/M/MV5BMzQxYjU1OTUtYjRiOC00NDg2LWI4MWUtZGU5YzdkYTcwNTBlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg"
                        }
                    ],
                    "totalResults": "293",
                    "Response": "True"
                }
                """;
        Mockito.when(omdbIntegration.searchByKeyword(Mockito.any())).thenReturn(response);
        List<Movie> movies = this.movieService.getMoviesBySearch("scary");
        Assertions.assertEquals(2, movies.size());
    }
    @Test
    public void getMoviesBySearch_FormatFailedTest() {
        String response = " {[}";
        Mockito.when(omdbIntegration.searchByKeyword(Mockito.any())).thenReturn(response);
        Assertions.assertThrows(ResponseBodyFormatException.class, ()-> {
            this.movieService.getMoviesBySearch("scary");
        });
    }
    @Test
    public void getMoviesBySearch_OmdbIntegrationFailedTest() {
        Mockito.when(omdbIntegration.searchByKeyword(Mockito.any())).thenReturn(null);
        Assertions.assertThrows(ResponseNullException.class, ()-> {
            this.movieService.getMoviesBySearch("scary");
        });
    }
    @Test
    public void getMoviesByIdOrTitle_FormatSuccessTest() {
        String response = """
                {
                    "Title": "Guardians of the Galaxy Vol. 2",
                    "Year": "2017",
                    "Rated": "PG-13",
                    "Released": "05 May 2017",
                    "Runtime": "136 min",
                    "Genre": "Action, Adventure, Comedy",
                    "Director": "James Gunn",
                    "Writer": "James Gunn, Dan Abnett, Andy Lanning",
                    "Actors": "Chris Pratt, Zoe Saldana, Dave Bautista",
                    "Plot": "The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord's encounter with his father, the ambitious celestial being Ego.",
                    "Language": "English",
                    "Country": "United States",
                    "Awards": "Nominated for 1 Oscar. 15 wins & 60 nominations total",
                    "Poster": "https://m.media-amazon.com/images/M/MV5BNjM0NTc0NzItM2FlYS00YzEwLWE0YmUtNTA2ZWIzODc2OTgxXkEyXkFqcGdeQXVyNTgwNzIyNzg@._V1_SX300.jpg",
                    "Ratings": [
                        {
                            "Source": "Internet Movie Database",
                            "Value": "7.6/10"
                        },
                        {
                            "Source": "Rotten Tomatoes",
                            "Value": "85%"
                        },
                        {
                            "Source": "Metacritic",
                            "Value": "67/100"
                        }
                    ],
                    "Metascore": "67",
                    "imdbRating": "7.6",
                    "imdbVotes": "745,617",
                    "imdbID": "tt3896198",
                    "Type": "movie",
                    "DVD": "10 Jul 2017",
                    "BoxOffice": "$389,813,101",
                    "Production": "N/A",
                    "Website": "N/A",
                    "Response": "True"
                }
                """;
        Mockito.when(omdbIntegration.searchByIdOrTitle(Mockito.any(),Mockito.any())).thenReturn(response);
        Movie movie = this.movieService.getMoviesByIdOrTitle("tt3896198",null);
        Assertions.assertEquals("tt3896198", movie.getImdbID());
    }
    @Test
    public void getMoviesByIdOrTitle_FormatFailedTest() {
        String response = " {[}";
        Mockito.when(omdbIntegration.searchByIdOrTitle(Mockito.any(),Mockito.any())).thenReturn(response);
        Assertions.assertThrows(ResponseBodyFormatException.class, ()-> {
            this.movieService.getMoviesByIdOrTitle("tt3896198",null);
        });
    }
    @Test
    public void getMoviesByIdOrTitle_OmdbIntegrationFailedTest() {
        Mockito.when(omdbIntegration.searchByIdOrTitle(Mockito.any(),Mockito.any())).thenReturn(null);
        Assertions.assertThrows(ResponseNullException.class, ()-> {
            this.movieService.getMoviesByIdOrTitle("tt3896198",null);
        });
    }
    @Test
    public void getFavoriteMovies_FormatSuccessTest() {
        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setImdbID("tt0175142");
        Favorite favorite2 = new Favorite();
        favorite2.setId(2L);
        favorite2.setImdbID("tt0257106");
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(favorite1);
        favorites.add(favorite2);
        Mockito.when(movieRepository.findAll()).thenReturn(favorites);
        Mockito.when(omdbIntegration.searchByIdOrTitle("tt0175142",null)).thenReturn("""
                {
                    "Title": "Scary Movie",
                    "Year": "2000",
                    "Rated": "R",
                    "Released": "07 Jul 2000",
                    "Runtime": "88 min",
                    "Genre": "Comedy",
                    "Director": "Keenen Ivory Wayans",
                    "Writer": "Shawn Wayans, Marlon Wayans, Buddy Johnson",
                    "Actors": "Anna Faris, Jon Abrahams, Marlon Wayans",
                    "Plot": "A year after disposing of the body of a man they accidentally killed, a group of dumb teenagers are stalked by a bumbling serial killer.",
                    "Language": "English",
                    "Country": "United States",
                    "Awards": "7 wins & 6 nominations",
                    "Poster": "https://m.media-amazon.com/images/M/MV5BMGEzZjdjMGQtZmYzZC00N2I4LThiY2QtNWY5ZmQ3M2ExZmM4XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
                    "Ratings": [
                        {
                            "Source": "Internet Movie Database",
                            "Value": "6.3/10"
                        },
                        {
                            "Source": "Rotten Tomatoes",
                            "Value": "51%"
                        },
                        {
                            "Source": "Metacritic",
                            "Value": "48/100"
                        }
                    ],
                    "Metascore": "48",
                    "imdbRating": "6.3",
                    "imdbVotes": "281,457",
                    "imdbID": "tt0175142",
                    "Type": "movie",
                    "DVD": "20 Jan 2016",
                    "BoxOffice": "$157,019,771",
                    "Production": "N/A",
                    "Website": "N/A",
                    "Response": "True"
                }
                """);
        Mockito.when(omdbIntegration.searchByIdOrTitle("tt0257106",null)).thenReturn("""
                {
                    "Title": "Scary Movie 2",
                    "Year": "2001",
                    "Rated": "R",
                    "Released": "04 Jul 2001",
                    "Runtime": "83 min",
                    "Genre": "Comedy, Horror",
                    "Director": "Keenen Ivory Wayans",
                    "Writer": "Shawn Wayans, Marlon Wayans, Buddy Johnson",
                    "Actors": "Anna Faris, Marlon Wayans, Antony Acker",
                    "Plot": "Four teens are tricked by Professor Oldman (Tim Curry) into visiting a haunted house for a school project.",
                    "Language": "English",
                    "Country": "United States",
                    "Awards": "3 wins & 5 nominations",
                    "Poster": "https://m.media-amazon.com/images/M/MV5BMzQxYjU1OTUtYjRiOC00NDg2LWI4MWUtZGU5YzdkYTcwNTBlXkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
                    "Ratings": [
                        {
                            "Source": "Internet Movie Database",
                            "Value": "5.3/10"
                        },
                        {
                            "Source": "Rotten Tomatoes",
                            "Value": "14%"
                        },
                        {
                            "Source": "Metacritic",
                            "Value": "29/100"
                        }
                    ],
                    "Metascore": "29",
                    "imdbRating": "5.3",
                    "imdbVotes": "172,497",
                    "imdbID": "tt0257106",
                    "Type": "movie",
                    "DVD": "02 Aug 2016",
                    "BoxOffice": "$71,308,997",
                    "Production": "N/A",
                    "Website": "N/A",
                    "Response": "True"
                }
                """);
        List<Movie> movies = this.movieService.getFavoriteMovies();
        Assertions.assertEquals(2, movies.size());
    }
    @Test
    public void getFavoriteMovies_FormatFailedTest() {
        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setImdbID("tt0175142");
        Favorite favorite2 = new Favorite();
        favorite2.setId(2L);
        favorite2.setImdbID("tt0257106");
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(favorite1);
        favorites.add(favorite2);
        Mockito.when(movieRepository.findAll()).thenReturn(favorites);
        Mockito.when(omdbIntegration.searchByIdOrTitle("tt0175142",null)).thenReturn("""
                {
                    "Title": "Scary Movie",
                    "Year": "2000",
                    "Rated": "R",
                    "Released": "07 Jul 2000",
                    "Runtime": "88 min",
                    "Genre": "Comedy",
                    "Director": "Keenen Ivory Wayans",
                    "Writer": "Shawn Wayans, Marlon Wayans, Buddy Johnson",
                    "Actors": "Anna Faris, Jon Abrahams, Marlon Wayans",
                    "Plot": "A year after disposing of the body of a man they accidentally killed, a group of dumb teenagers are stalked by a bumbling serial killer.",
                    "Language": "English",
                    "Country": "United States",
                    "Awards": "7 wins & 6 nominations",
                    "Poster": "https://m.media-amazon.com/images/M/MV5BMGEzZjdjMGQtZmYzZC00N2I4LThiY2QtNWY5ZmQ3M2ExZmM4XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
                    "Ratings": [
                        {
                            "Source": "Internet Movie Database",
                            "Value": "6.3/10"
                        },
                        {
                            "Source": "Rotten Tomatoes",
                            "Value": "51%"
                        },
                        {
                            "Source": "Metacritic",
                            "Value": "48/100"
                        }
                    ],
                    "Metascore": "48",
                    "imdbRating": "6.3",
                    "imdbVotes": "281,457",
                    "imdbID": "tt0175142",
                    "Type": "movie",
                    "DVD": "20 Jan 2016",
                    "BoxOffice": "$157,019,771",
                    "Production": "N/A",
                    "Website": "N/A",
                    "Response": "True"
                }
                """);
        Mockito.when(omdbIntegration.searchByIdOrTitle("tt0257106",null)).thenReturn("{[}");
        Assertions.assertThrows(ResponseBodyFormatException.class, ()-> {
            this.movieService.getFavoriteMovies();
        });
    }
    @Test
    public void getFavoriteMovies_OmdbIntegrationFailedTest() {
        Favorite favorite1 = new Favorite();
        favorite1.setId(1L);
        favorite1.setImdbID("tt0175142");
        Favorite favorite2 = new Favorite();
        favorite2.setId(2L);
        favorite2.setImdbID("tt0257106");
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(favorite1);
        favorites.add(favorite2);
        Mockito.when(movieRepository.findAll()).thenReturn(favorites);
        Mockito.when(omdbIntegration.searchByIdOrTitle("tt0175142",null)).thenReturn("""
                {
                    "Title": "Scary Movie",
                    "Year": "2000",
                    "Rated": "R",
                    "Released": "07 Jul 2000",
                    "Runtime": "88 min",
                    "Genre": "Comedy",
                    "Director": "Keenen Ivory Wayans",
                    "Writer": "Shawn Wayans, Marlon Wayans, Buddy Johnson",
                    "Actors": "Anna Faris, Jon Abrahams, Marlon Wayans",
                    "Plot": "A year after disposing of the body of a man they accidentally killed, a group of dumb teenagers are stalked by a bumbling serial killer.",
                    "Language": "English",
                    "Country": "United States",
                    "Awards": "7 wins & 6 nominations",
                    "Poster": "https://m.media-amazon.com/images/M/MV5BMGEzZjdjMGQtZmYzZC00N2I4LThiY2QtNWY5ZmQ3M2ExZmM4XkEyXkFqcGdeQXVyMTQxNzMzNDI@._V1_SX300.jpg",
                    "Ratings": [
                        {
                            "Source": "Internet Movie Database",
                            "Value": "6.3/10"
                        },
                        {
                            "Source": "Rotten Tomatoes",
                            "Value": "51%"
                        },
                        {
                            "Source": "Metacritic",
                            "Value": "48/100"
                        }
                    ],
                    "Metascore": "48",
                    "imdbRating": "6.3",
                    "imdbVotes": "281,457",
                    "imdbID": "tt0175142",
                    "Type": "movie",
                    "DVD": "20 Jan 2016",
                    "BoxOffice": "$157,019,771",
                    "Production": "N/A",
                    "Website": "N/A",
                    "Response": "True"
                }
                """);
        Mockito.when(omdbIntegration.searchByIdOrTitle("tt0257106",null)).thenReturn(null);
        Assertions.assertThrows(ResponseNullException.class, ()-> {
            this.movieService.getFavoriteMovies();
        });
    }
}
