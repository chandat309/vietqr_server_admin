package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IRefreshTokenService;
import com.vietqradminbe.domain.exceptions.BadRequestException;
import com.vietqradminbe.domain.exceptions.ErrorCode;
import com.vietqradminbe.domain.models.RefreshToken;
import com.vietqradminbe.domain.repositories.RefreshTokenRepository;
import com.vietqradminbe.infrastructure.configuration.timehelper.TimeHelperUtil;
import org.springframework.beans.factory.annotation.Value;
import com.vietqradminbe.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${jwt.refreshtoken.lifespan}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setId(UUID.randomUUID().toString());
        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenDurationMs).toString());
        refreshToken.setCreatedAt(TimeHelperUtil.getCurrentTime());
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        Instant expiresAt = Instant.parse(token.getExpiresAt());
        if (expiresAt.compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(String userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
