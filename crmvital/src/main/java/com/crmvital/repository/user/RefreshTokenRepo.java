package com.crmvital.repository.user;

import com.crmvital.model.RefreshToken;
import com.crmvital.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByExpiryDateBefore(Date now);
    boolean existsByUserIdAndExpiryDateAfter(int userId, Date now);
    void deleteByUserId(int userId);

}
