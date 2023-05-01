package example.springcloud.eureka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private final InfoProvider infoProvider;

    public ConsumerController(InfoProvider infoProvider) { this.infoProvider = infoProvider; }

    @GetMapping("/info")
    public String info() {
        return infoProvider.info();
    }

}
