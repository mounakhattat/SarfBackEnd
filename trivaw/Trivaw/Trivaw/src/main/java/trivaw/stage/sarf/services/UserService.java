package trivaw.stage.sarf.services;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.repository.AccountRepository;
import trivaw.stage.sarf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements IUserService {
    private static final String SUBJECT = "Bienvenue chez Loan tree ";
    private static final String TEXT = "To confirm your register click here: ";
    @Autowired
    private UserRepository userRepository;


    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    EmailService emailService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public User getUserById(Integer idUser) {
        return userRepository.findById(idUser).orElse(null);
    }


    @Override
    public User createUser(User user) throws MessagingException {

        user.setActived(false);

        return userRepository.save(user);
    }
    /*@Override
    public boolean activateAccount(String confirmationCode) {
        User user = userRepository.findByEmail(confirmationCode);
        if (user != null) {
            user.setActived(true);
            userRepository.save(user);
            return true;
        }
        return false;
    } */

/*  String confirmUrl = "http://localhost:8080/user/createUser" ;
        sendSimpleMessage(user.getEmail(), SUBJECT, TEXT+confirmUrl); */
    // return savedUser;
    //}
    /*    // Generate QR code for the user
        String qrCodeText = "User Id: " + savedUser.getIdUser() + ", Name: " + savedUser.getUsername();
        int size = 250;

        QRCodeGenerator generator = new QRCodeGenerator();
        String qrCodeBase64 = generator.generateQRCodeImage(Integer.valueOf(size));


        savedUser.setQrcode(qrCodeBase64);
        userRepository.save(savedUser); */


    @Override
    public User updateUser(Integer idUser, User user) {
        User existingUser = userRepository.findById(idUser).orElse(null);
        if (existingUser != null) {
            existingUser.setLastName(user.getLastName());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setAge(user.getAge());
            existingUser.setHousing(user.getHousing());
            existingUser.setNumPhone(user.getNumPhone());

            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(Integer idUser) {
        userRepository.deleteById(idUser);
    }


    @Override
    public User saveUser(String username, String password, String confirmedPassword, String role) {
        User user = userRepository.findByUsername(username).get();
        if (user != null) throw new RuntimeException("User already exists");
        if (!password.equals(confirmedPassword)) throw new RuntimeException("Please confirm your password");
        User User = new User();
        User.setUsername(username);

        User.setPassword(passwordEncoder.encode(password));

        userRepository.save(User);

        return User;
    }

    @Override
    public User UpdatePassword(User agent, String password) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        agent.setPassword(encoder.encode(password));
        userRepository.save(agent);
        return agent;
    }
    @Override

    public String getPasswordForAuthentication(User user) {
        if (user != null && user.getNewPassword() != null && !user.getNewPassword().isEmpty()) {
            return user.getNewPassword();
        } else {
            return user.getPassword();
        }
    }
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {         // mailing
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }


  /*  public int calculateUserScore(Integer idUser) {
        int score = 0;
        User user = userRepository.findById(idUser).orElse(null);

        // Check user gender
        if (user.getSex().equalsIgnoreCase("homme")) {
            score += 10;
        } else if (user.getSex().equalsIgnoreCase("femme")) {
            score += 20;
        }

        // Check user location
        if (user.getPlaceBirth().equalsIgnoreCase("New York")) {
            score += 30;
        } else if (user.getPlaceBirth().equalsIgnoreCase("Los Angeles")) {
            score += 20;
        } else if (user.getPlaceBirth().equalsIgnoreCase("Chicago")) {
            score += 10;
        }
        // check user age
        if (user.getAge() > 40) {
            score += 30;
        } else if (user.getAge()> 30) {
            score += 20;
        } else if (user.getAge() > 20) {
            score += 10;
        }

        // Check user address
        if (user.getHousing().toLowerCase().contains("Lac")) {
            score += 10;
        } else if (user.getHousing().toLowerCase().contains("Ibn sina")) {
            score += 20;
        } else if (user.getHousing().toLowerCase().contains("Nacer")) {
            score += 30;
        }



        return score;
    }
*/
    @Override
    public User banUser(Integer idUser, int nbr) {
     User acc = userRepository.findById(idUser).orElse(null);
        acc.setBanned(true);

        acc.setBannedPeriode(new Date(new Date().getTime() + (nbr * 1000 * 60 * 60 * 24)));
        userRepository.save(acc);
        return acc;
    }
    @Override
    public List<User> getVisitors() {
        return userRepository.findVisitors();
    }



}