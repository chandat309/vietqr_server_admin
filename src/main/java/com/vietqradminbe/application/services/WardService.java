package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IWardService;
import com.vietqradminbe.domain.models.Ward;
import com.vietqradminbe.domain.repositories.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardService implements IWardService {

    @Autowired
    WardRepository wardRepository;

    @Override
    public List<Ward> getAllWards() {
        return wardRepository.findAll();
    }

    @Override
    public List<Ward> getAllWardsByDistrictId(int districtId) {
        return wardRepository.getWardsByDistrictCode(districtId);
    }
}
