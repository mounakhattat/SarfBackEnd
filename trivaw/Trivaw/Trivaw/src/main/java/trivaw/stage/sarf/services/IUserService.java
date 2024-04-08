package trivaw.stage.sarf.services;


import trivaw.stage.sarf.Entities.User;


import javax.mail.MessagingException;
import java.util.List;

public interface IUserService {
    public List<User> getAllUsers();

    User getUserById(Integer idUser);
    String getPasswordForAuthentication(User user);
    User createUser(User user) throws MessagingException;
    User updateUser(Integer idUser, User user);
    //public User findByConfirmationCode(String code);


    void deleteUser(Integer  idUser);
    // public boolean activateAccount(String confirmationCode) ;
    void sendSimpleMessage(String to, String subject, String text);
    List<User> getVisitors();
    User banUser(Integer idUser, int nbr);

    User saveUser(String username, String password, String confirmedPassword, String role);

    User UpdatePassword(User agent,String password);

    // public User saveUser(String username, String password, String confirmedPassword,String role);

    // public UserDetails loadUserByUsername(String username);

    // public void addRoleToUser(String username,String name);

    // public User findUserByUserName(String userName) ;





}