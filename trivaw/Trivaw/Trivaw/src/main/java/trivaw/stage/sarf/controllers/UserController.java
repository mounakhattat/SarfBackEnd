package trivaw.stage.sarf.controllers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import trivaw.stage.sarf.Entities.User;
import trivaw.stage.sarf.Export.ExcelExporter;
import trivaw.stage.sarf.Export.PDFGenerator;
import trivaw.stage.sarf.repository.UserRepository;
import trivaw.stage.sarf.Responses.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import trivaw.stage.sarf.services.ComfirmationEmailService;
import trivaw.stage.sarf.services.EmailService;
import trivaw.stage.sarf.services.TwilioService;
import trivaw.stage.sarf.services.UserService;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    TwilioService twilioService;
    @Autowired
    EmailService emailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    private HttpSession session;
    @Autowired
    ComfirmationEmailService comfirmationEmailService;


    //http://localhost:8080/user/retrieve-user
    @GetMapping("/retrieve-user")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //http://localhost:8080/user/get-user/{id}
    @GetMapping("/get-user/{user-id}")
    public User getUserById(@PathVariable("user-id") Integer IdUser) {
        return userService.getUserById(IdUser);
    }

    @GetMapping("/ListTouristes")
    public List<User> getAllVisitors() {
        return userService.getVisitors();
    }

    @Value("${app.twillio.toPhoneNo}")
    private String To;
    @Value("${app.twillio.fromPhoneNo}")
    private String From;

    //http://localhost:8080/user/create-user
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user, HttpSession session) throws MessagingException {
        String msg = "Bonjour, Loantree vous remercie pour votre fédilité..  ..";
        twilioService.sendSms(To, From, msg);
        String confirmationCode = UUID.randomUUID().toString();

        String to = user.getEmail();
        String subject = "Confirmation de compte";
        String body = "Bonjour " + user.getFirstName() + ",\n\n" +
                "Pour la confirmation de votre compte c'est votre code .\n\n";

        String text = "\n Cordialement,\n" +
                "L'équipe de LOANTREE, \n";
        String reset = confirmationCode;
        emailService.sendEmail(to, subject, body + reset + text);
        System.out.println("Session: " + session);
        session.setAttribute("expectedCode", confirmationCode);
        session.setAttribute("user", user);
        return ResponseEntity.ok(new MessageResponse("Merci de verifier votre code"));

    }

    @PostMapping("/confirmEmail/{code}")
    public String confirmEmail(@PathVariable String code, HttpSession session, Model model) {
        String expectedCode = (String) session.getAttribute("expectedCode");
        System.out.println(expectedCode);
        if (comfirmationEmailService.verifyCodeEmail(code, expectedCode)) {
            User user = (User) session.getAttribute("user");
            user.setActived(true);
            userRepository.save(user);
            return "Votre est activé avec succès !";
        } else {
            model.addAttribute("error", "Le code de confirmation est invalide !");
            return "Erreur,Merci de verifier votre code svp";
        }
    /*@GetMapping("/confirmRegistration/{token}")

    public void confirmAgentRegistration(@PathVariable("token") String token,HttpSession session) {

        User user = (User) session.getAttribute("user");
        user.setActived(true);
        userRepository.save(user);

    }

     */
   /* @GetMapping("/confirmEmail/{token}")
    public ResponseEntity<String> confirmEmail(@PathVariable("token") String token) {
        User user = userService.getUserByConfirmationToken(token);
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid confirmation token.");
        }
        user.setActived(true);
        userService.updateUser(user);
        return ResponseEntity.ok("Email confirmed successfully.");
    }*/

    }

    //http://localhost:8080/user/update/{user-id}
    @PutMapping("/update/{user-id}")
    public User updateUser(@PathVariable("user-id") Integer UserId, @RequestBody User user) {
        userService.updateUser(UserId, user);

        return user;
    }

    @Value("${app.twillio.toPhoneNo}")
    private String to;
    @Value("${app.twillio.fromPhoneNo}")
    private String from;

    //http://localhost:8080/user/delete/{UserId}
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") Integer IdUser) {
     //   String body = "Hello. Good Morning!! Vous n'étes plus parti à LoanTree!! A la prochaine..";
      //  twilioService.sendSms(to, from, body);
        userService.deleteUser(IdUser);
    }

    private void authenticate(String username, String pass) throws Exception {
        try {
            SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pass)));
        } catch (DisabledException ex) {
            throw new Exception("User disabled");
        } catch (BadCredentialsException ex) {

            throw new Exception("bad creadentials");
        } catch (Exception e) {

        }
    }


    @GetMapping(value = "/PDF")
    public ResponseEntity<InputStreamResource> employeeReport() throws IOException {
        List<User> p = (List<User>) userService.getAllUsers();

        ByteArrayInputStream bis = PDFGenerator.employeePDFReport(p);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "Online; filename=TableOfUsers.pdf");

        return ResponseEntity.ok().headers(headers)
                .body(new InputStreamResource(bis));

    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> users = userService.getAllUsers();

        ExcelExporter excelExporter = new ExcelExporter(users);

        excelExporter.export(response);
    }

    private static final String QR_CODE_IMAGE_PATH = "C:\\Users\\Mouna\\Desktop\\Finaaal\\pidevmouna\\pidevmouna\\pidevmouna\\src\\main\\resources\\qrCode\\.png";

    @GetMapping("/viewqr/{id}")
    @ResponseBody
    public void viewQRCode(@PathVariable("id") int id, HttpServletResponse response) {
        try {
            User user = userService.getUserById(id);
            String codeText = user.getIdUser() + "-" + user.getUsername() + "-" + user.getEmail() + "-" + user.getFirstName() + "-" + user.getLastName() + "-" + user.getNumPhone();
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = qrCodeWriter.encode(codeText, BarcodeFormat.QR_CODE, 200, 200, hints);
            Path path = FileSystems.getDefault().getPath(QR_CODE_IMAGE_PATH);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
            response.setContentType("image/png");
            ImageIO.write(ImageIO.read(new File(QR_CODE_IMAGE_PATH)), "png", response.getOutputStream());
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

   /* @GetMapping("/calculateUserScore/{IdUser}")
    public Integer calculateUserScore(@PathVariable("IdUser") Integer idUser) {
        return userService.calculateUserScore(idUser);
    }
    */

    @PutMapping("/banUser/{idUser}/{nbr}")
    @ResponseBody
    public User banUser(@PathVariable("idUser") Integer idUser, @PathVariable("nbr") int nbr) {
        return userService.banUser(idUser, nbr);
    }
}






