package com.fundoo.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String firstName;
    private String lastName;
    private String emailId;
    @JsonIgnore
    private String password;
    private String mobileNumber;
    private boolean isVerified;
    private LocalDate createdAt;
    @OneToMany(targetEntity = NoteModel.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private List<NoteModel> notes;

    public UserModel(UserModel userModel){
        this.userId=userModel.userId;
        this.firstName=userModel.firstName;
        this.lastName=userModel.lastName;
        this.emailId=userModel.emailId;
        this.password=userModel.password;
        this.isVerified=userModel.isVerified;
        this.mobileNumber=userModel.mobileNumber;
    }
}
