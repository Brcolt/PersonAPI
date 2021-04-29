package dio.codeanywere.personapi.controller;

import dio.codeanywere.personapi.dto.request.PersonDTO;
import dio.codeanywere.personapi.dto.response.MessageResponseDTO;
import dio.codeanywere.personapi.exception.PersonNotFoundException;
import dio.codeanywere.personapi.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/v1/person")
public class PersonController {

    private PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createPerson(@RequestBody @Valid PersonDTO personDTO) {
        return personService.createPerson(personDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDTO> listAll() {
        return personService.listAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDTO findById(@PathVariable Long id) throws PersonNotFoundException {
        return personService.findById(id);
    }

    @PutMapping("/{id}")
    public MessageResponseDTO updateById(@PathVariable Long id,
                                         @RequestBody @Valid PersonDTO personDTO) throws PersonNotFoundException {
        return personService.update(id, personDTO);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByid(@PathVariable Long id) throws PersonNotFoundException {
        personService.delete(id);
    }

}
