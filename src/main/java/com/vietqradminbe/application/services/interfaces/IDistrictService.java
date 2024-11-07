package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.District;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IDistrictService {
    public List<District> getAllDistricts();
    public List<District> getAllDistrictsByProvinceId(int provinceId);
}
