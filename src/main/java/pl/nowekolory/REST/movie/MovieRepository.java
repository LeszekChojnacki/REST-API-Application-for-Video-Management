package pl.nowekolory.REST.movie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Favorite, Long> {
}
