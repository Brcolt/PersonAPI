package dio.codeanywere.personapi.repository;

import dio.codeanywere.personapi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
