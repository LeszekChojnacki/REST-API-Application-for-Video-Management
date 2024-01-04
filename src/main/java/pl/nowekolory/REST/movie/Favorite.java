package pl.nowekolory.REST.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue
    private Long id;
    private String imdbID;
}
