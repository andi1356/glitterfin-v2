package arobu.glitterfinv2.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonEater {

    @PostMapping("/api/json")
    public String eatJson(@RequestBody String transactionBody) {
        return "ok: \n" + transactionBody;
    }
}