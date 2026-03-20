package com.bootcamp4u.service;

import com.bootcamp4u.dto.request.LoginRequest;
import com.bootcamp4u.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
