package com.sn.Controller;



import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/sec", produces="application/json;charset=utf-8")
public class SecurityController {


    @PreAuthorize("hasRole('USER')") //有ROLE_USER权限的用户可以访问
    @GetMapping("/healthcheck")
    @ResponseBody
    public String healthcheck(){return "success";}

    @PreAuthorize("hasRole('ADMIN')") //有ROLE_USER权限的用户可以访问
    @GetMapping("/current")
    @ResponseBody
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }

}

