package br.com.yuri.studies.restfulspringboot.mapper;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;


public class Mapper {

    private static ModelMapper mapper = new ModelMapper();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseObject(List<O> origin, Class<D> destination) {
        List<D> destinations = new ArrayList<>();
        for (O o : origin) {
            destinations.add(mapper.map(o, destination));
        }
        return destinations;
    }
}
