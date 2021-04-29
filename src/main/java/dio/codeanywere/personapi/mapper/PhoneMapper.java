package dio.codeanywere.personapi.mapper;


import dio.codeanywere.personapi.dto.request.PhoneDTO;
import dio.codeanywere.personapi.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PhoneMapper {

    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

    Phone toModel(PhoneDTO phoneDTO);

    PhoneDTO toDTO(Phone phone);
}
