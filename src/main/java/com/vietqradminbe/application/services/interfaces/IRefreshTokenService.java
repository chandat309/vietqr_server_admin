package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IRefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(String userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByUserId(String userId);
}
