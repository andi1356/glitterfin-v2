package arobu.glitterfinv2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestEndpoint {
    public String get(){
        return "Hello World";
    }
}
