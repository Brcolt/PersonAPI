package dio.codeanywere.personapi.service;


import dio.codeanywere.personapi.dto.request.PersonDTO;
import dio.codeanywere.personapi.dto.response.MessageResponseDTO;
import dio.codeanywere.personapi.entity.Person;
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
        return MessageResponseDTO
                .builder()
                .message("Created a person with ID " + savedPerson.getId())
                .build();
    }

    public List<PersonDTO> listAll() {
      return personRepository.findAll()
              .stream()
              .map(personMapper::toDTO)
              .collect(Collectors.toList());
    }

}
