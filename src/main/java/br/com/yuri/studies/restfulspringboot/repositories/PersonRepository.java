package br.com.yuri.studies.restfulspringboot.repositories;

import br.com.yuri.studies.restfulspringboot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
