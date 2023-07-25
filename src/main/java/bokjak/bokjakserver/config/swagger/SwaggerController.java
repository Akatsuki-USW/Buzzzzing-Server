package bokjak.bokjakserver.config.swagger;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/docs")
@Hidden
public class SwaggerController {
    @GetMapping
    public String redirect() {
        return "redirect:/swagger-ui/index.html";
    }
}
