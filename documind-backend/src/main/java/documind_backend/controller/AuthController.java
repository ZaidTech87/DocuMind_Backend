package documind_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/login-success")
    public String loginSuccess() {
        return "Login successful and user saved!";
    }
}
