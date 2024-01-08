package pl.nowekolory.REST.movie;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
