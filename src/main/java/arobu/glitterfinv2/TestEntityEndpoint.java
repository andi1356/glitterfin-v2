package arobu.glitterfinv2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEntityEndpoint{

    @GetMapping("test")
    public String test(){
        return "this is a TEST";
    }
}
