package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IDistrictService;
import com.vietqradminbe.domain.models.District;
import com.vietqradminbe.domain.repositories.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService implements IDistrictService {

    @Autowired
    DistrictRepository districtRepository;

    @Override
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    @Override
    public List<District> getAllDistrictsByProvinceId(int provinceId) {
        return districtRepository.getDistrictByProvinceCode(provinceId);
    }
}
