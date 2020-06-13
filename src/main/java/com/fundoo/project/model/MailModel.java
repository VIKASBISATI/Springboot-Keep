package com.fundoo.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class MailModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String to;
    private String subject;
    private String text;
}
