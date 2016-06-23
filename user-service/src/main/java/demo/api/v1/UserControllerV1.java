package demo.api.v1;

import demo.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/v1")
public class UserControllerV1 {

    private UserServiceV1 userService;
    private final Logger log = LoggerFactory.getLogger(UserControllerV1.class);
    
    @Autowired
    public UserControllerV1(UserServiceV1 userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/me")
    public ResponseEntity me(Principal principal) {
        User user = null;
        
        log.info("getting UserByUsername in controller");
        
        if(principal != null) {
            user = userService.getUserByUsername(principal.getName());
        }

        return Optional.ofNullable(user)
                .map(a -> new ResponseEntity<User>(a, HttpStatus.OK))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
