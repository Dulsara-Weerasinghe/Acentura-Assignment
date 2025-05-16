package com.example.eventManagement.controller;


import com.example.eventManagement.dto.AuthRequestDto;
import com.example.eventManagement.dto.LoginResponseBean;
import com.example.eventManagement.dto.ResponseBean;
import com.example.eventManagement.entity.User;
import com.example.eventManagement.exception.DataNotFounException;
import com.example.eventManagement.repository.UserRepository;
import com.example.eventManagement.util.EndPoint;
import com.example.eventManagement.util.JwtUtil;
import com.example.eventManagement.util.MessageVarList;
import com.example.eventManagement.util.StatusVarList;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TokenController {

    private JwtUtil jwtUtil;
    private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    private static  UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private  AuthenticationManager authenticationManager;



    @PostMapping(value = EndPoint.TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBean getToken(@RequestBody AuthRequestDto authRequest) throws DataNotFounException {

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUserName());
                final String token = jwtUtil.generateToken(userDetails);

                User user = userRepository.findByUserName(authRequest.getUserName()).orElseThrow(() -> new DataNotFounException("User Details not found"));
                log.info("GET user detail " + user);

                String userType = user.getRole();

                if (!userType.isEmpty()) {
                    log.info("send  Response: " + new LoginResponseBean(token, userType).toString());
                    LoginResponseBean loginResponseBean = new LoginResponseBean(token, userType);
                    return new ResponseBean(MessageVarList.RSP_SUCCESS, StatusVarList.SUCCESS, loginResponseBean);
                } else {
                    //default user type admin set
                    log.info("send RESPONSE : " + new LoginResponseBean(token, "ADMIN").toString());
                    LoginResponseBean loginResponseBean = new LoginResponseBean(token, "ADMIN");
                    return new ResponseBean(MessageVarList.RSP_SUCCESS, "success",loginResponseBean);
                }


            } else {
                log.info("user authenticate field");
                log.info("send to web portal : " + authenticate, toString());
                return new ResponseBean(MessageVarList.RSP_NO_DATA_FOUND, StatusVarList.FAILED, null);
            }

        } catch (Exception e) {
            log.error("error  " + e.getMessage());
            return new ResponseBean(MessageVarList.RSP_NO_DATA_FOUND, e.getMessage(), null);
        }


    }


}
