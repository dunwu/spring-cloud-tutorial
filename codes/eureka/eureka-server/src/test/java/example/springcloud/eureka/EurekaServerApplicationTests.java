package example.springcloud.eureka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SuppressWarnings("rawtypes")
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class EurekaServerApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void catalogLoads() {
        ResponseEntity<Map> entity = testRestTemplate.getForEntity("/eureka/apps", Map.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void adminLoads() {
        ResponseEntity<Map> entity = testRestTemplate.getForEntity("/actuator/env", Map.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
