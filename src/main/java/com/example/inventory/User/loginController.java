package com.example.inventory.User;

import com.example.inventory.global.GlobalData;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class loginController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    RoleRepository rolerepo;

    @GetMapping("/login")
    public String login(){

        GlobalData.cart.clear();
        return "login";
    }

    @GetMapping("/register")
    public String registerGet(){
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request)throws ServletException{
        String password=user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles=new ArrayList<>();
        roles.add(rolerepo.findById(2).get());
        user.setRoles(roles);
        userepo.save(user);
        //request.login(user.getEmail(),password);
        return "login";

    }


    @GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model){
        model.addAttribute("pageTitle", "Forgot Password");
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPasswordForm(HttpServletRequest request,Model model) throws UserNotFoundException, MessagingException, UnsupportedEncodingException {
        String email=request.getParameter("email");
        String token = RandomString.make(45);
        try {
            customUserDetailsService.updateResetPasswordToken(token, email);

            String resetPasswordLink = Utility.getSiteURL(request)+"/reset_password?token="+token;
            sendEmail(email, resetPasswordLink);

            model.addAttribute("message","We have sent a reset password link to your email.");
        }
        catch(UserNotFoundException e){
            model.addAttribute("error",e.getMessage());
        }
        model.addAttribute("pageTitle","Forgot Password");
        return "forgot_password_form";
    }

    private void sendEmail(String email, String resetPasswordLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@osm.com","OSM Support");
        helper.setTo(email);

        String subject=" Here's the link to reset your password";

        String content="<p>Hello,</p>"
                +"<p>You have requested to reset your password.</p>"
                +"<p>Click the link below to change your password:</p>"
                +"<p><b><a href=\""+resetPasswordLink+"\">Change my password</a></b></p>"
                +"<p>Ignore this email if you do remember your password, or you have not made the request.";
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value="token") String token, Model model){
        User user = customUserDetailsService.get(token);
        if(user==null){
            System.out.println(token);
            model.addAttribute("title","Reset your Password");
            model.addAttribute("message","Invalid token");
            return "message";
        }

        model.addAttribute("token", token);
        model.addAttribute("pageTitle", "Reset your password");

        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model){
        String token=request.getParameter("token");
        String password=request.getParameter("password");

        User user = customUserDetailsService.get(token);

        if(user==null){
            model.addAttribute("title","Reset your Password");
            model.addAttribute("message","Invalid token");
            return "message";

        }
        else{
            System.out.println("x");
            customUserDetailsService.updatePassword(user, password);
            model.addAttribute("message","You have successfully changed your password");
        }
        return "message";

    }
    @GetMapping("/message")
    public String mess(){
        return "message";
    }

}
