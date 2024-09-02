package trivaw.stage.sarf.services;

import trivaw.stage.sarf.Entities.Notification;

import java.util.List;

public interface INotificationServices {
    public List<Notification> getAllNotification();
    Notification getNotificationById(Integer idNotification);
    Notification createNotification(Notification a);
    Notification updateNotification(Integer idNotification, Notification a);
    void deleteNotification(Integer  idNotification);
    List<Notification> findNotificationByUser(Integer idUser);
}
