package com.fundoo.project.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
@Log
@Component
public class TokenUtil {
    private static final String SECRET_KEY="vikas";

    public String generateToken(Long userId){
        String token=null;
        try {
            token= JWT.create().withClaim("userId", userId).sign(Algorithm.HMAC256(SECRET_KEY));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }

    public Long parseToken(String token) {
        Long userId;
        Verification verification = null;
        try {
            verification = JWT.require(Algorithm.HMAC256(SECRET_KEY));
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JWTVerifier jwtVerifier = verification.build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Claim claim = decodedJWT.getClaim("userId");
        userId = claim.asLong();
        return userId;
    }
}
