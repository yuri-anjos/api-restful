package br.com.yuri.studies.restfulspringboot.services;

import br.com.yuri.studies.restfulspringboot.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.exceptions.ResourceNotFoundException;
import br.com.yuri.studies.restfulspringboot.mapper.Mapper;
import br.com.yuri.studies.restfulspringboot.models.Person;
import br.com.yuri.studies.restfulspringboot.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {

    private final Logger logger = Logger.getLogger(PersonService.class.getName());

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> findAll() {
        var peaple = personRepository.findAll();
        return Mapper.parseObject(peaple, PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        var person = findEntity(id);
        return Mapper.parseObject(person, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO personDTO) {
        Person person = Mapper.parseObject(personDTO, Person.class);
        person.setId(null);

        person = personRepository.save(person);
        return Mapper.parseObject(person, PersonDTO.class);
    }

    public PersonDTO update(Long id, PersonDTO personDTO) {
        var person = findEntity(id);

        person.setFirstName(personDTO.getFirstName());
        person.setLastName(personDTO.getLastName());
        person.setAddress(personDTO.getAddress());
        person.setGender(personDTO.getGender());

        person = personRepository.save(person);
        return Mapper.parseObject(person, PersonDTO.class);
    }

    public void deleteById(Long id) {
        var person = findEntity(id);
        personRepository.delete(person);
    }

    public Person findEntity(Long id) {
        logger.info("Finding a Person...");

        return personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Person not found."));
    }
}
