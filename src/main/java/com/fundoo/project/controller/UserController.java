package com.fundoo.project.controller;

import com.fundoo.project.dto.ForgotPasswordDto;
import com.fundoo.project.dto.LoginDto;
import com.fundoo.project.dto.RegisterDto;
import com.fundoo.project.dto.ResetPasswordDto;
import com.fundoo.project.model.UserModel;
import com.fundoo.project.response.Response;
import com.fundoo.project.service.IUser;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Log
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUser iUser;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody RegisterDto registerDto){
        String message=iUser.registerUser(registerDto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new Response(HttpStatus.CREATED.value(),message));
    }
    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginDto loginDto){
        Object loginData=iUser.loginUser(loginDto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new Response(HttpStatus.OK.value(),"User logged in successfully",loginData));
    }
    @PutMapping("/verify")
    public ResponseEntity<Response> emailVerify(@RequestHeader(name = "token") String token){
        String message=iUser.emailVerify(token);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new Response(HttpStatus.ACCEPTED.value(),message));
    }
    @PostMapping("/forgotPassword")
    public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto){
        String message=iUser.forgotPassword(forgotPasswordDto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new Response(HttpStatus.ACCEPTED.value(),message));
    }
    @PostMapping("/resetPassword")
    public ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto, @RequestHeader String token){
        String message=iUser.resetPassword(resetPasswordDto,token);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new Response(HttpStatus.ACCEPTED.value(),message));
    }
    @GetMapping("/getUsers")
    public ResponseEntity<Response> resetPassword(){
        List<UserModel> usersList=iUser.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK.value()).body(new Response(HttpStatus.FOUND.value(),"users retrieved successfully",usersList));
    }
}