package de.jonashackt.springbootvuejs.controller;

import de.jonashackt.springbootvuejs.domain.Role;
import de.jonashackt.springbootvuejs.domain.User;
import de.jonashackt.springbootvuejs.repository.UserRepository;
import de.jonashackt.springbootvuejs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RestController()
@RequestMapping("/api")
public class BackendController {

    private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

    public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping(path = "/hello")
    public @ResponseBody String sayHello() {
        LOG.info("GET called on /hello resource");
        return HELLO_TEXT;
    }

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) {
        User user = new User(name, email);
        userRepository.save(user);

        LOG.info(user.toString() + " successfully saved into DB");

        return user.getEmail();
    }

    @GetMapping(path= "/user/{email}")
    public @ResponseBody User getUserByEmail(@PathVariable("email") String email) {
        LOG.info("Reading user with email " + email + " from database.");
        return userRepository.findByEmail(email);
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String register(@RequestParam String email, @RequestParam String password, @RequestParam String confPassword){
        if(!password.equals(confPassword)) {
            LOG.info("Error the two passwords do not match");
            return "Error the two passwords do not match";
        }
        else if(userService.getUser(email) != null) {
            LOG.info("Error this username already exists");
            return "Error this username already exists";
        }

        //Checking for non alphanumerical characters in the username.
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        if(pattern.matcher(email).find()) {
            LOG.info("Error this username already exists");
            return "No special characters are allowed in the username";
        }

        userService.save(new User(email, password, Arrays.asList(new Role("USER"), new Role("ACTUATOR"))));
        LOG.info("User created");
        return "User created";
    }

    @GetMapping(value = "/users")
    public List<User> users(){
        return userService.getAllUsers();
    }

    @GetMapping(value = "/logouts")
    public void logout(@RequestParam (value = "access_token") String accessToken){
        tokenStore.removeAccessToken(tokenStore.readAccessToken(accessToken));
    }

    @GetMapping(value ="/getUsername")
    public String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
