package com.fundoo.project.service;

import com.fundoo.project.Repositories.UserRepository;
import com.fundoo.project.dto.ForgotPasswordDto;
import com.fundoo.project.dto.LoginDto;
import com.fundoo.project.dto.RegisterDto;
import com.fundoo.project.dto.ResetPasswordDto;
import com.fundoo.project.exception.UserException;
import com.fundoo.project.model.MailModel;
import com.fundoo.project.model.UserModel;
import com.fundoo.project.response.TokenResponse;
import com.fundoo.project.utility.RabbitMQUtil;
import com.fundoo.project.utility.TokenUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Log
@Service
public class UserServiceImpl implements IUser {
    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MailModel mailModel;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RabbitMQUtil rabbitMQUtil;
    @Override
    public String registerUser(RegisterDto registerDto) {
        String token=null;
        String link=null;
        UserModel userModel = new UserModel();
        userModel.setFirstName(registerDto.getFirstName());
        userModel.setLastName(registerDto.getLastName());
        userModel.setEmailId(registerDto.getEmailId());
        userModel.setMobileNumber(registerDto.getMobileNumber());
        userModel.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userModel.setCreatedAt(LocalDate.now());
        userRepository.save(userModel);
        token=tokenUtil.generateToken(userModel.getUserId());
        link="http://localhost:3000/verify/"+token;
        mailModel.setTo(userModel.getEmailId());
        mailModel.setSubject("Registration Link");
        mailModel.setText(link);
        rabbitMQUtil.sendMail(mailModel);
        return "User registered successfully";
    }

    @Override
    public Object loginUser(LoginDto loginDto) {
        Optional<UserModel> optionalUser = userRepository.findByEmailId(loginDto.getEmailId());
        if(!optionalUser.isPresent()){
            throw new UserException("Email id not found");
        }
        return optionalUser.filter(user->{
            return user!=null;
        }).filter(user->{
            return user.isVerified() && passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
        }).map(user->{
           String token=tokenUtil.generateToken(user.getUserId());
            TokenResponse tokenResponse=new TokenResponse(user.getFirstName(),user.getLastName(),user.getEmailId(),token);
            return tokenResponse;
        }).orElseThrow(()->new UserException("Username or password incorrect!!"));
    }

    @Override
    public String emailVerify(String token) {
        Long id =tokenUtil.parseToken(token);
        log.info("id"+id);
        Optional<UserModel> optionalUser = userRepository.findById(id);
        return optionalUser.filter(user -> {
          return user!=null;
        }).map(user->{
            if(user.isVerified()){
                throw new UserException("Email already verified");
            }
            user.setVerified(true);
            userRepository.save(user);
            return "User Verfied Successfully";
        }).orElseThrow(()->new UserException("Email id incorrect"));
    }

    @Override
    public String forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        Optional<UserModel> optionalUserModel = userRepository.findByEmailId(forgotPasswordDto.getEmailId());
        return optionalUserModel.filter(user->{
            return user!=null;
        }).filter(user->{
            return user.isVerified();
        }).map(user->{
            String token=tokenUtil.generateToken(user.getUserId());
            String resetLink="http://localhost:3000/setPassword/"+token;
            mailModel.setTo(user.getEmailId());
            mailModel.setSubject("Reset Password Link");
            mailModel.setText(resetLink);
            rabbitMQUtil.sendMail(mailModel);
            return "Reset Password link sent succesfully to the user {}"+user.getEmailId();
        }).orElseThrow(()->new UserException("Email id not correct"));
    }

    @Override
    public String resetPassword(ResetPasswordDto resetPasswordDto, String token) {
        Long userId=tokenUtil.parseToken(token);
        Optional<UserModel> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            throw new UserException("User not found while resetting");
        }
        return optionalUser.filter(user->{
            return user!=null;
        }).map(user->{
            user.setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
            userRepository.save(user);
            return "Password reset successfully";
        }).orElseThrow(()->new UserException("Invalid token"));

    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserModel> usersList = userRepository.findAll();
        if(usersList.isEmpty()){
            throw new UserException("user not found");
        }
        return usersList;
    }
}
