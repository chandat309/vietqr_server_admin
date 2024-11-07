package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IUserFeatureService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserFeatureService implements IUserFeatureService {
}
