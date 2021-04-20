package dio.codeanywere.personapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

    @RequestMapping("/")
    public String hello() {
        return "Spring Boot API Deployed";
    }
}
