package trivaw.stage.sarf.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.Entities.Notification;
import trivaw.stage.sarf.services.NotificationServices;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/Notification")
public class NotificationController {
    @Autowired
    NotificationServices notificationServices;
    @GetMapping("/findNotificationByUser/{idUser}")
    public List<Notification> findNotificationByUser(@PathVariable("idUser") Integer idUser) {
        return notificationServices.findNotificationByUser(idUser);
    }

}
