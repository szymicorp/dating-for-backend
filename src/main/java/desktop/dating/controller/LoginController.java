package desktop.dating.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @PostMapping
    public ResponseEntity<Void> login() {
        return ResponseEntity.ok().build();
    }
}
