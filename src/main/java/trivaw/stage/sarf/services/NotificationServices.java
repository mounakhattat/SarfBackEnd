package trivaw.stage.sarf.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trivaw.stage.sarf.Entities.Notification;
import trivaw.stage.sarf.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationServices implements  INotificationServices{
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification getNotificationById(Integer idNotification) {
        return notificationRepository.findById(idNotification).orElse(null);
    }
    @Override
    public List<Notification> findNotificationByUser(Integer idUser) {
        List<Notification> notifications = notificationRepository.findNotificationByUser(idUser);
        // Trier les notifications par date (les plus récentes en premier)
        notifications.sort((n1, n2) -> n2.getDate().compareTo(n1.getDate()));

        return notifications;

    }

    @Override
    public Notification createNotification(Notification a) {
        return notificationRepository.save(a);
    }

    @Override
    public Notification updateNotification(Integer idNotification, Notification a) {
        Notification existingNotification = notificationRepository.findById(idNotification).orElse(null);
        if (existingNotification != null) {

            return notificationRepository.save(existingNotification);

        } else {
            return null;
        }
    }

    @Override
    public void deleteNotification(Integer idNotification) {
        notificationRepository.deleteById(idNotification);
    }


}


