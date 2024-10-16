package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IUserFeatureService;
import com.vietqradminbe.web.dto.request.FeatureCreationRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserFeatureService implements IUserFeatureService {
}
