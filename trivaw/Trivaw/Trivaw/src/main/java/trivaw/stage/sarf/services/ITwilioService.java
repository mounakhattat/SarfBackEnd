package trivaw.stage.sarf.services;

public interface ITwilioService {

        void sendSms(String to, String from, String body);

        void makeCall(String from, String to);

    }

