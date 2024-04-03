package br.com.yuri.studies.restfulspringboot.repositories;

import br.com.yuri.studies.restfulspringboot.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	@Modifying
	@Query("UPDATE Person p SET p.enabled = FALSE WHERE p.id = ?1")
	void disablePerson(Long id);
}
