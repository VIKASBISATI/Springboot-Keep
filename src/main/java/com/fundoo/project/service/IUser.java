package com.fundoo.project.service;

import com.fundoo.project.dto.ForgotPasswordDto;
import com.fundoo.project.dto.LoginDto;
import com.fundoo.project.dto.RegisterDto;
import com.fundoo.project.dto.ResetPasswordDto;
import com.fundoo.project.model.UserModel;

import java.util.List;

public interface IUser {
    String registerUser(RegisterDto registerDto);
    Object loginUser(LoginDto loginDto);
    String emailVerify(String token);
    String forgotPassword(ForgotPasswordDto forgotPasswordDto);
    String resetPassword(ResetPasswordDto resetPasswordDto, String token);
    List<UserModel> getAllUsers();
}
