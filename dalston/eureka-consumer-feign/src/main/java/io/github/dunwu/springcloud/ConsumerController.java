package io.github.dunwu.springcloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private final SendProvider sendProvider;

    public ConsumerController(SendProvider sendProvider) {
        this.sendProvider = sendProvider;
    }

    @GetMapping("/recv")
    public String recv() {
        return sendProvider.consumer();
    }

}
