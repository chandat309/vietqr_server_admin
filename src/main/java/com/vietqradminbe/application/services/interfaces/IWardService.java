package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.District;
import com.vietqradminbe.domain.models.Ward;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWardService {
    public List<Ward> getAllWards();
    public List<Ward> getAllWardsByDistrictId(int districtId);
}
