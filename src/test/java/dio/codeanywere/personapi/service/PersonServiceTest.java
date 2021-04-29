package dio.codeanywere.personapi.service;

import dio.codeanywere.personapi.builder.PersonDTOBuilder;
import dio.codeanywere.personapi.dto.request.PersonDTO;
import dio.codeanywere.personapi.dto.response.MessageResponseDTO;
import dio.codeanywere.personapi.entity.Person;
import dio.codeanywere.personapi.exception.PersonNotFoundException;
import dio.codeanywere.personapi.mapper.PersonMapper;
import dio.codeanywere.personapi.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    private PersonMapper personMapper = PersonMapper.INSTANCE;

    @InjectMocks
    private PersonService personService;

    @Test
    void whenValidPersonIsInformedThenReturnSaveMessage() {

        // Given
        PersonDTO expectedPersonDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        Person expectedSavedPerson = personMapper.toModel(expectedPersonDTO);

        //when
        when(personRepository.save(expectedSavedPerson)).thenReturn(expectedSavedPerson);

        //then
        MessageResponseDTO expectedMessageResponseDTO = createExpectedMessageResonse(expectedSavedPerson.getId());

        MessageResponseDTO successMessage = personService.createPerson(expectedPersonDTO);

        verify(personRepository, times(1)).save(expectedSavedPerson);
        assertEquals(expectedMessageResponseDTO, successMessage);
    }

    @Test
    void whenAListIsCalledThenReturnAlistOfPerson() {
        // given
        PersonDTO expectedFoundPersonDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        Person expectedFondPerson = personMapper.toModel(expectedFoundPersonDTO);

        //when
        when(personRepository.findAll()).thenReturn(Collections.singletonList(expectedFondPerson));

        //then
        List<PersonDTO> foundedPersonDTO = personService.listAll();

        verify(personRepository, times(1)).findAll();
        assertThat(foundedPersonDTO, is(not(empty())));
        assertThat(foundedPersonDTO.get(0).getFirstName(), is(equalTo(expectedFoundPersonDTO.getFirstName())));

    }

    @Test
    void whenAIdPersonIsInformedThenReturnThePerson() throws PersonNotFoundException {
        // given
        PersonDTO expectedFoundPersonDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        Person expectedFoundPerson = personMapper.toModel(expectedFoundPersonDTO);

        //when
        when(personRepository.findById(expectedFoundPersonDTO.getId())).thenReturn(Optional.of(expectedFoundPerson));

        //Then
        PersonDTO personFindedDTO = personService.findById(expectedFoundPerson.getId());

        verify(personRepository, times(1)).findById(expectedFoundPersonDTO.getId());
        assertThat(personFindedDTO.getFirstName(), equalTo(expectedFoundPerson.getFirstName()));

    }

    @Test
    void whenAIdIsIformedToDeletPersonThenThePersonIsDeleted() throws PersonNotFoundException {
        //Given
        PersonDTO expectedFoundPersonDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        Person expectedFoundPerson = personMapper.toModel(expectedFoundPersonDTO);

        //When
        when(personRepository.findById(expectedFoundPersonDTO.getId())).thenReturn(Optional.of(expectedFoundPerson));
        doNothing().when(personRepository).deleteById(expectedFoundPersonDTO.getId());

        //Then
        personService.delete(expectedFoundPersonDTO.getId());

        verify(personRepository, times(1)).findById(expectedFoundPersonDTO.getId());
        verify(personRepository, times(1)).deleteById(expectedFoundPersonDTO.getId());
    }

    @Test
    void whenAPersonIsInformedWithNewInformationsThenShouldBeUpdate() throws PersonNotFoundException {

        // Given
        PersonDTO expectedPersonDTO = PersonDTOBuilder.builder().build().toPersonDTO();
        Person expectedSavedPerson = personMapper.toModel(expectedPersonDTO);

        //when
        when(personRepository.findById(expectedPersonDTO.getId())).thenReturn(Optional.of(expectedSavedPerson));
        when(personRepository.save(expectedSavedPerson)).thenReturn(expectedSavedPerson);

        //then
        MessageResponseDTO expectedMessageResponseDTO = updatedExpectedMessageResonse(expectedPersonDTO.getId());

        MessageResponseDTO successMessage = personService.update(expectedPersonDTO.getId(), expectedPersonDTO);

        assertEquals(expectedMessageResponseDTO, successMessage);
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
