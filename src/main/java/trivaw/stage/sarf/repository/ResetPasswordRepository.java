package trivaw.stage.sarf.repository;

import trivaw.stage.sarf.Entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

    @Repository
    public interface ResetPasswordRepository extends JpaRepository  <PasswordResetToken, Long> {

        PasswordResetToken findByToken(String token);

        @Query("Select T From PasswordResetToken T where T.expireDate < ?1")
        List<PasswordResetToken> findExpireToken(Instant date);

    }


