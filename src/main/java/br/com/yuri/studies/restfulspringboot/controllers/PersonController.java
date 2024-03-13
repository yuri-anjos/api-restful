package br.com.yuri.studies.restfulspringboot.controllers;

import br.com.yuri.studies.restfulspringboot.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.services.PersonService;
import br.com.yuri.studies.restfulspringboot.util.ProjectMediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
    public List<PersonDTO> findAll() {
        return personService.findAll();
    }

    @GetMapping(value = "/{id}",
            produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
    public PersonDTO findById(@PathVariable Long id) {
        return personService.findById(id);
    }

    @PostMapping(
            consumes = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML},
            produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
    public PersonDTO create(@RequestBody PersonDTO personDTO) {
        return personService.create(personDTO);
    }

    @PutMapping(value = "/{id}",
            consumes = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML},
            produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
    public PersonDTO update(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        return personService.update(id, personDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        personService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
