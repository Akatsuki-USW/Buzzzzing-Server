package bokjak.bokjakserver.common;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/hello")
@RequiredArgsConstructor
@Hidden
public class HelloController {
    @Value("${server.port}")
    private String port;

    @GetMapping()
    public String sayHello() {
        return "✅ server listening on port " + port;
    }
}
