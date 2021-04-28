package dio.codeanywere.personapi.service;


import dio.codeanywere.personapi.dto.request.PersonDTO;
import dio.codeanywere.personapi.dto.response.MessageResponseDTO;
import dio.codeanywere.personapi.entity.Person;
import dio.codeanywere.personapi.exception.PersonNotFoundException;
import dio.codeanywere.personapi.mapper.PersonMapper;
import dio.codeanywere.personapi.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    public MessageResponseDTO createPerson(PersonDTO personDTO) {
        Person personToSave = personMapper.toModel(personDTO);

        Person savedPerson = personRepository.save(personToSave);
        return createMethodResponse(savedPerson, "Created a person with ID ");
    }

    public List<PersonDTO> listAll() {
      return personRepository.findAll()
              .stream()
              .map(personMapper::toDTO)
              .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Person person = verifyIfExists(id);
        return personMapper.toDTO(person);
    }

    public void delete(Long id) throws PersonNotFoundException {
        verifyIfExists(id);
        personRepository.deleteById(id);
    }

    public MessageResponseDTO update(Long id, PersonDTO personDTO) throws PersonNotFoundException {
        verifyIfExists(id);

        Person personToUpdate = personMapper.toModel(personDTO);

        Person updatedPerson = personRepository.save(personToUpdate);

        return createMethodResponse(updatedPerson, "Updated Person with ID ");
    }

    private MessageResponseDTO createMethodResponse(Person savedPerson, String s) {
        return MessageResponseDTO
                .builder()
                .message(s + savedPerson.getId())
                .build();
    }

    private Person verifyIfExists(Long id) throws PersonNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
