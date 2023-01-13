package com.example.inventory.User;

import com.example.inventory.Wallet.Wallet;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Entity
@Data
@Table(name="users")

public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private Integer id;


 @Column(nullable=false)
@NotEmpty
 private String firstName;

 private String lastName;

@Column (nullable=false,unique=true)
@NotEmpty
@Email(message="{errors.invalid_email}")
 private String  email;


private String password;

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    @Column(name = "reset_password_token")
private String resetPasswordToken;

    @OneToOne(
            mappedBy = "user"
    )
    private Wallet wallet;
@ManyToMany(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
@JoinTable(
        name="user_role",
        joinColumns = {@JoinColumn(name="USER_ID",referencedColumnName = "ID")},
        inverseJoinColumns = {@JoinColumn(name="ROLE_ID",referencedColumnName = "ID")}
)
private List<Role> roles;

public User(User user){
    this.firstName=user.getFirstName();
    this.lastName=user.getLastName();
    this.email=user.getEmail();
    this.password=user.getPassword();
    this.roles=user.getRoles();
}

public User(){

}


}
