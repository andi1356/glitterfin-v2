package arobu.glitterfinv2.controller;

import arobu.glitterfinv2.model.entity.TestEntity;
import arobu.glitterfinv2.model.repository.TestEntityRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEntityEndpoint{

    private final TestEntityRepo repo;

    public TestEntityEndpoint(TestEntityRepo repo) {
        this.repo = repo;
    }

    @GetMapping("test")
    public String test(){
        TestEntity test = new TestEntity();
        test.setId(1L);
        test.setName("test");
        return "entity saved: " + repo.save(test);
    }

    @GetMapping("finances")
    public String finances(){
        return "TBD These will be your finances";
    }
}
