package com.fundoo.project.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private int statusCode;
    private String message;
    private Object data;
    public Response(int statusCode, String message){
        this.statusCode=statusCode;
        this.message=message;
    }
}
