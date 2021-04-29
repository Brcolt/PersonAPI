package dio.codeanywere.personapi.controller;


import dio.codeanywere.personapi.builder.PersonDTOBuilder;
import dio.codeanywere.personapi.builder.PhoneDTOBuilder;
import dio.codeanywere.personapi.dto.request.PersonDTO;
import dio.codeanywere.personapi.dto.request.PhoneDTO;
import dio.codeanywere.personapi.dto.response.MessageResponseDTO;
import dio.codeanywere.personapi.entity.Person;
import dio.codeanywere.personapi.entity.Phone;
import dio.codeanywere.personapi.exception.PersonNotFoundException;
import dio.codeanywere.personapi.mapper.PersonMapper;
import dio.codeanywere.personapi.mapper.PhoneMapper;
import dio.codeanywere.personapi.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dio.codeanywere.personapi.utils.JsonConvertUtils.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private static final String PERSON_API_URL_PATH = "/api/v1/person";
    private static final long VALID_PERSON_ID = 1L;
    private static final long INVALID_PERSON_ID = 2L;

    private MockMvc mockMvc;

    private PersonMapper personMapper = PersonMapper.INSTANCE;

    private PhoneMapper phoneMapper = PhoneMapper.INSTANCE;


    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenAPersonIsCreated() throws Exception {

        //given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        PhoneDTO phoneDTOList = PhoneDTOBuilder.builder().build().toPhoneDTO();
        Phone phone = phoneMapper.toModel(phoneDTOList);

        List<Phone> phones = new ArrayList<>();
        phones.add(phone);
        personDTO.setPhones(phones);
        MessageResponseDTO createdMessage = createExpectedMessageResonse(personDTO.getId());

        //when
        when(personService.createPerson(personDTO)).thenReturn(createdMessage);

        // then
        mockMvc.perform(post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    void whenPOSTIsCalledWithoutExpectedFieldsThenAErrorIsReturned() throws Exception {

        //given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();


        // then
        mockMvc.perform(post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void whenGETListIsCalledThenOkStatusIsReturned() throws Exception {

        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        when(personService.listAll()).thenReturn(Collections.singletonList(personDTO));

        //then
        mockMvc.perform(get(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAGETWithIdIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        when(personService.findById(personDTO.getId())).thenReturn(personDTO);

        //then
        mockMvc.perform(get(PERSON_API_URL_PATH + "/" + VALID_PERSON_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAGETWithInvalidIdIsCalledThenNotFoundIsReturned() throws Exception {
        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        when(personService.findById(personDTO.getId())).thenThrow(PersonNotFoundException.class);

        //then
        mockMvc.perform(get(PERSON_API_URL_PATH + "/" + personDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAPUTIsCalledThenOKStatusIsReturned() throws Exception {

        // given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        PhoneDTO phonesDTO = PhoneDTOBuilder.builder().build().toPhoneDTO();

        Phone phone = phoneMapper.toModel(phonesDTO);

        List<Phone> phones = new ArrayList<>();
        phones.add(phone);
        personDTO.setPhones(phones);

        MessageResponseDTO messageResponseDTO = updatedExpectedMessageResonse(personDTO.getId());

        //when
        PersonDTO updatedPersonDTO = personDTO;
        updatedPersonDTO.setFirstName("Raul");

        when(personService.update(personDTO.getId(), updatedPersonDTO)).thenReturn(messageResponseDTO);

        mockMvc.perform(put(PERSON_API_URL_PATH + "/" + updatedPersonDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatedPersonDTO)))
                .andExpect(status().isOk());



    }

    @Test
    void whenDeleteIsCalledThenNoContentStatusIsReturned() throws Exception{

        //Given
        PersonDTO personDTO = PersonDTOBuilder.builder().build().toPersonDTO();

        //when
        doNothing().when(personService).delete(personDTO.getId());

        mockMvc.perform(delete(PERSON_API_URL_PATH + "/" + personDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDeleteIsCalledWithInvalidIdThenNotFoundStatusIsReturned() throws Exception{


        //when
        doThrow(PersonNotFoundException.class).when(personService).delete(INVALID_PERSON_ID);

        mockMvc.perform(delete(PERSON_API_URL_PATH + "/" + INVALID_PERSON_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private MessageResponseDTO createExpectedMessageResonse(Long id) {
        MessageResponseDTO expectedSuccessMessage = MessageResponseDTO
                .builder()
                .message("Created a person with ID " + id)
                .build();
        return expectedSuccessMessage;
    }

    private MessageResponseDTO updatedExpectedMessageResonse(Long id) {
        MessageResponseDTO expectedSuccessMessage = MessageResponseDTO
                .builder()
                .message("Updated Person with ID " + id)
                .build();
        return expectedSuccessMessage;
    }

}
