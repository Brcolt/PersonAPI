package dio.codeanywere.personapi.builder;

import dio.codeanywere.personapi.dto.request.PersonDTO;
import dio.codeanywere.personapi.entity.Phone;
import lombok.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public class PersonDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String firstName = "Bruno";

    @Builder.Default
    private String lastName = "Colt";

    @Builder.Default
    private String cpf = "085.785.114-41";

    @Builder.Default
    private String birthDate = "24-10-1989";

    @Builder.Default
    private List<Phone> phones = Collections.emptyList();

    public PersonDTO toPersonDTO() {
        return new PersonDTO(id, firstName, lastName, cpf, birthDate, phones);
    }
}
