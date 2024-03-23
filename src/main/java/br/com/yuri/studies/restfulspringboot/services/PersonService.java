package br.com.yuri.studies.restfulspringboot.services;

import br.com.yuri.studies.restfulspringboot.controllers.PersonController;
import br.com.yuri.studies.restfulspringboot.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.exceptions.ResourceNotFoundException;
import br.com.yuri.studies.restfulspringboot.mapper.Mapper;
import br.com.yuri.studies.restfulspringboot.models.Person;
import br.com.yuri.studies.restfulspringboot.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

	private static final Logger LOGGER = Logger.getLogger(PersonService.class.getName());

	private final PersonRepository personRepository;

	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public List<PersonDTO> findAll() {
		var peaple = personRepository.findAll();

		var peopleDTO = Mapper.parseObject(peaple, PersonDTO.class);
		peopleDTO.forEach(p ->
				p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return peopleDTO;
	}

	public PersonDTO findById(Long id) {
		var personEntity = findEntity(id);
		var personDTO = Mapper.parseObject(personEntity, PersonDTO.class);

		personDTO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personDTO;
	}

	public PersonDTO create(PersonDTO body) {
		Person entity = Mapper.parseObject(body, Person.class);
		entity.setId(null);
		entity = personRepository.save(entity);

		var personDTO = Mapper.parseObject(entity, PersonDTO.class);
		personDTO.add(linkTo(methodOn(PersonController.class).findById(personDTO.getKey())).withSelfRel());
		return personDTO;
	}

	public PersonDTO update(Long id, PersonDTO body) {
		var entity = findEntity(id);
		entity.setFirstName(body.getFirstName());
		entity.setLastName(body.getLastName());
		entity.setAddress(body.getAddress());
		entity.setGender(body.getGender());
		entity = personRepository.save(entity);

		var personDTO = Mapper.parseObject(entity, PersonDTO.class);
		personDTO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personDTO;
	}

	public void deleteById(Long id) {
		var person = findEntity(id);
		personRepository.delete(person);
	}

	private Person findEntity(Long id) {
		LOGGER.info("Finding a Person...");

		return personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found."));
	}
}
