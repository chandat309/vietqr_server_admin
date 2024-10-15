package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenService {
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(String userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByUserId(String userId);
}
