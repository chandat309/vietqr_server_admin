package com.vietqradminbe.application.services;

import com.vietqradminbe.application.services.interfaces.IProvinceService;
import com.vietqradminbe.domain.models.Province;
import com.vietqradminbe.domain.repositories.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvinceService implements IProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
}
