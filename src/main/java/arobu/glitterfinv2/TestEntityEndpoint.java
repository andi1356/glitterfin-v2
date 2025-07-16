package arobu.glitterfinv2;

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
}
