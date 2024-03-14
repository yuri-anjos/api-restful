package br.com.yuri.studies.restfulspringboot.mapper;

import br.com.yuri.studies.restfulspringboot.dtos.PersonDTO;
import br.com.yuri.studies.restfulspringboot.models.Person;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


public class Mapper {

    private static ModelMapper modelMapper = new ModelMapper();

    private Mapper() {
    }

    static {
        modelMapper.createTypeMap(
                        Person.class,
                        PersonDTO.class)
                .addMapping(Person::getId, PersonDTO::setKey);
        modelMapper.createTypeMap(
                        PersonDTO.class,
                        Person.class)
                .addMapping(PersonDTO::getKey, Person::setId);
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return modelMapper.map(origin, destination);
    }

    public static <O, D> List<D> parseObject(List<O> origin, Class<D> destination) {
        List<D> destinations = new ArrayList<>();
        for (O o : origin) {
            destinations.add(modelMapper.map(o, destination));
        }
        return destinations;
    }
}
