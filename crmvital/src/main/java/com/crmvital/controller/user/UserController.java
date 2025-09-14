package com.crmvital.controller.user;


import com.crmvital.model.dto.userDTO.UserDto;
import com.crmvital.service.user.UserService;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }




    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto){
        return userService.insertUser(userDto);
    }
}
