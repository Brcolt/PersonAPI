package dio.codeanywere.personapi.mapper;

import dio.codeanywere.personapi.dto.request.PersonDTO;
import dio.codeanywere.personapi.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "dd-MM-yyyy")
    Person toModel(PersonDTO personDTO);

    @Mapping(target = "birthDate", source = "birthDate", dateFormat = "dd-MM-yyyy")
    PersonDTO toDTO(Person person);
}
