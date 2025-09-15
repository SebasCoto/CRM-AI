package com.crmvital.util;

import com.crmvital.service.user.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenCleanUp {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Scheduled(fixedRate = 60*60*1000)
    public void cleanExpiredTokens() {
        refreshTokenService.deleteExpiredTokens();
    }
}
