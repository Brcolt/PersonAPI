package dio.codeanywere.personapi.builder;

import dio.codeanywere.personapi.dto.request.PhoneDTO;
import dio.codeanywere.personapi.enums.PhoneType;
import lombok.Builder;

@Builder
public class PhoneDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private PhoneType type = PhoneType.MOBILE;

    @Builder.Default
    private String number = "84999779244";

    public PhoneDTO toPhoneDTO() {
        return new PhoneDTO(id, type, number);
    }
}
