package pl.nowekolory.REST.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Movie {
    @JsonProperty("imdbID")
    private String imdbID;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Plot")
    private String plot;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Director")
    private String director;
    @JsonProperty("Poster")
    private String poster;
}
