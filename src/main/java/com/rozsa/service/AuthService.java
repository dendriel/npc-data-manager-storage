package com.rozsa.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "auth", url = "http://localhost:8080")
public interface AuthService {

    @RequestMapping(method = RequestMethod.GET, value = "/validate", produces = "application/json")
    AuthResponse validate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
