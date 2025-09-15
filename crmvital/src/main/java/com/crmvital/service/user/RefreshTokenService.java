package com.crmvital.service.user;

import com.crmvital.model.RefreshToken;
import com.crmvital.repository.user.RefreshTokenRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
    }
    private final Map<String, RefreshToken> tokenStore = new HashMap<>();
    private final long refreshTokenDuration = 7 * 24 * 60 * 60 * 1000L;

    public RefreshToken getRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + refreshTokenDuration));

        return refreshTokenRepo.save(refreshToken);
    }

    @Transactional
    public Optional<RefreshToken> verifyToken(String token) {
        Optional<RefreshToken> rtOpt = refreshTokenRepo.findByToken(token);
        if(rtOpt.isEmpty()) return Optional.empty();

        RefreshToken rt = rtOpt.get();
        if(rt.getExpiryDate().before(new Date())) {
            refreshTokenRepo.delete(rt);
            return Optional.empty();
        }
        return Optional.of(rt);
    }


    @Transactional
    public void deleteToken(String token) {
        refreshTokenRepo.deleteByToken(token);
    }

    public void deleteExpiredTokens() {
        refreshTokenRepo.deleteByExpiryDateBefore(new Date());
    }

}
