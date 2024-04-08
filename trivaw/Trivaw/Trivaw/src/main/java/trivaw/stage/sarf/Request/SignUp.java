package trivaw.stage.sarf.Request;

import trivaw.stage.sarf.Entities.ERole;

import java.util.Set;

import javax.validation.constraints.*;

public class SignUp {
    @NotBlank
   @Size(min = 3, max = 20)
    private String username;

   @NotBlank
  @Size(max = 50)
    @Email
    private String email;

    private ERole role;

    @NotBlank
   @Size(min = 6, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ERole getRole() {
        return this.role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public static class Builder {
        private String providerUserID;
        private String displayName;
        private String email;
        private String password;

    }



}
