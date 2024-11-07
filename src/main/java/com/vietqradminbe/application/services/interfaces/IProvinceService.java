package com.vietqradminbe.application.services.interfaces;

import com.vietqradminbe.domain.models.Province;
import com.vietqradminbe.domain.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProvinceService {
    public List<Province> getAllProvinces();
}
