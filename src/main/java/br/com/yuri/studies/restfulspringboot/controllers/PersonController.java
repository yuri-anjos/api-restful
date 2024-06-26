package br.com.yuri.studies.restfulspringboot.controllers;

import br.com.yuri.studies.restfulspringboot.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.services.PersonService;
import br.com.yuri.studies.restfulspringboot.util.ProjectMediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/person/v1")
@Tag(name = "People", description = "Endpoints that manages people")
public class PersonController {

	@Autowired
	private PagedResourcesAssembler<PersonDTO> personDtoAssembler;

	private final PersonService personService;

	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@Operation(
			summary = "Finds all People",
			description = "Finds all People",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = @Content(
									array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class)))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	@GetMapping(produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
	public PagedModel<EntityModel<PersonDTO>> findAll(@RequestParam(defaultValue = "0") Integer page,
													  @RequestParam(defaultValue = "12") Integer size,
													  @RequestParam(defaultValue = "firstName") String sortBy,
													  @RequestParam(defaultValue = "ASC") String sortDirection) {

		var direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
		var pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));
		var pageDTO = personService.findAll(pageRequest);

		Link link = linkTo(methodOn(PersonController.class)
				.findAll(page, size, sortBy, sortDirection)).withSelfRel();
		return personDtoAssembler.toModel(pageDTO, link);
	}

	@Operation(
			summary = "Finds a Person",
			description = "Finds a Person by id",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = @Content(
									schema = @Schema(implementation = PersonDTO.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	@GetMapping(value = "/{id}",
			produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
	public PersonDTO findById(@PathVariable Long id) {
		return personService.findById(id);
	}

	@Operation(
			summary = "Adds a new Person",
			description = "Adds a new person by passing it as a JSON/XML/YAML",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "201",
							content = @Content(
									schema = @Schema(implementation = PersonDTO.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	@PostMapping(
			consumes = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML},
			produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
	public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO personDTO) {
		var person = personService.create(personDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(person);
	}

	@Operation(
			summary = "Updates an existing Person",
			description = "Updates an existing person by passing it as a JSON/XML/YAML and ID",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200",
							content = @Content(
									schema = @Schema(implementation = PersonDTO.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	@PutMapping(value = "/{id}",
			consumes = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML},
			produces = {ProjectMediaType.APPLICATION_JSON, ProjectMediaType.APPLICATION_XML, ProjectMediaType.APPLICATION_YML})
	public PersonDTO update(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
		return personService.update(id, personDTO);
	}

	@Operation(
			summary = "Deletes a Person",
			description = "Deletes a Person by passing its ID",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	@PreAuthorize(value = "hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		personService.deleteById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@Operation(
			summary = "Disables a Person",
			description = "Disables a Person by passing its ID",
			tags = {"People"},
			responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			}
	)
	@PreAuthorize(value = "hasRole('ADMIN')")
	@PatchMapping("/{id}/disable")
	public ResponseEntity<Void> disablePerson(@PathVariable Long id) {
		personService.disablePerson(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
