package trivaw.stage.sarf.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/python")
public class PythonController {

    @GetMapping("/mouna/{currency}")
    public ResponseEntity<String> runPythonScript(@PathVariable String currency) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5002/mouna/" + currency;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(flaskUrl, String.class);
            return new ResponseEntity<>(response.getBody(), response.getStatusCode());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'appel à l'API Flask : " + e.getMessage());
        }
    }

}
