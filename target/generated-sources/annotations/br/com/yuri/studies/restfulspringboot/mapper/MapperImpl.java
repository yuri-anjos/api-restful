package br.com.yuri.studies.restfulspringboot.mapper;

import br.com.yuri.studies.restfulspringboot.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.models.Person;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-12T17:00:39-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class MapperImpl implements Mapper {

    @Override
    public PersonDTO personToPersonDTO(Person person) {
        if ( person == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        personDTO.setId( person.getId() );
        personDTO.setFirstName( person.getFirstName() );
        personDTO.setLastName( person.getLastName() );
        personDTO.setAddress( person.getAddress() );
        personDTO.setGender( person.getGender() );

        return personDTO;
    }

    @Override
    public Person personDTOToPerson(PersonDTO person) {
        if ( person == null ) {
            return null;
        }

        Person person1 = new Person();

        person1.setId( person.getId() );
        person1.setFirstName( person.getFirstName() );
        person1.setLastName( person.getLastName() );
        person1.setAddress( person.getAddress() );
        person1.setGender( person.getGender() );

        return person1;
    }

    @Override
    public List<PersonDTO> personToPersonDTO(List<Person> people) {
        if ( people == null ) {
            return null;
        }

        List<PersonDTO> list = new ArrayList<PersonDTO>( people.size() );
        for ( Person person : people ) {
            list.add( personToPersonDTO( person ) );
        }

        return list;
    }

    @Override
    public List<Person> personDTOToPerson(List<PersonDTO> people) {
        if ( people == null ) {
            return null;
        }

        List<Person> list = new ArrayList<Person>( people.size() );
        for ( PersonDTO personDTO : people ) {
            list.add( personDTOToPerson( personDTO ) );
        }

        return list;
    }
}
