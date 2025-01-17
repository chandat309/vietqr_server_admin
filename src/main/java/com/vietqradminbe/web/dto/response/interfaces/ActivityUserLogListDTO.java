package com.vietqradminbe.web.dto.response.interfaces;

import org.springframework.beans.factory.annotation.Value;

public interface ActivityUserLogListDTO {

    @Value("#{target.id ?: ''}")
    String getId();

    @Value("#{target.description ?: ''}")
    String getDescription();

    @Value("#{target.username ?: ''}")
    String getUsername();

    @Value("#{target.email ?: ''}")
    String getEmail();

    @Value("#{target.firstname ?: ''}")
    String getFirstname();

    @Value("#{target.lastname ?: ''}")
    String getLastname();

    @Value("#{target.phoneNumber ?: ''}")
    String getPhoneNumber();

    @Value("#{target.timeLog ?: ''}")
    String getTimeLog();

}
