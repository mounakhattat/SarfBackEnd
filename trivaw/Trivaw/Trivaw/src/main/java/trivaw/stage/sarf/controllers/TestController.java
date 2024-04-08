package trivaw.stage.sarf.controllers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")            // donne l'interface selon le role
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('CUSTOMER') ")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/agent")
    @PreAuthorize("hasRole('AGENT')")
    public String agentAccess() {
        return "agent Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
    @GetMapping("/visitor")
    @PreAuthorize("hasRole('VISITOR') or hasRole('VISITOR')")
    public String visitorAccess() {
        return "visitor Board.";
    }
}
