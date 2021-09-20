package myproject.demo.User.service;


import lombok.RequiredArgsConstructor;
import myproject.demo.KeyCloak.service.DuplicateUserSignUpException;
import myproject.demo.KeyCloak.service.Token;
import myproject.demo.KeyCloak.service.TokenProvider;
import myproject.demo.KeyCloak.service.TokenRequest;
import myproject.demo.User.domain.Password;
import myproject.demo.User.domain.User;
import myproject.demo.User.domain.UserRepository;
import myproject.demo.User.domain.Username;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;

    public Token login(String username, String password){
        TokenRequest tokenRequest
                = TokenRequest.create(username, password);
        return tokenProvider.issue(tokenRequest);
    }

    public void signUp(String username, String password) {
        TokenRequest tokenRequest
                = TokenRequest.create(username, password);
        tokenProvider.signUp(tokenRequest);
        save(username, password);
    }

    public void save(String username, String password){
        User user = User.create(Username.create(username), Password.create(password));
        userRepository.save(user);
    }


    public void checkDuplicateUser(String username){
        if (userRepository.existsByUsername(Username.create(username))){
            throw new DuplicateUserSignUpException();
        }
    }

    public boolean checkExistenceByUsername(String username){
        return userRepository.existsByUsername(Username.create(username));
    }

    public boolean checkExistenceByUserId(Long id){
        return userRepository.existsById(id);
    }

    public UserDto findUserByUsername(String username){
        Optional<User> searchedUser = userRepository.findByUsername(Username.create(username));
        return getUserDto(searchedUser);

    }

    public UserDto findLoggedUser(){
        return findUserByUsername(getLoggedUsername());
    }

    private String getLoggedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            return username = ((UserDetails)principal).getUsername();
        } else {
            return username = principal.toString();
        }
    }

    public UserDto findUserByUserId(Long userId){
        Optional<User> searchedUser = userRepository.findById(userId);
        return getUserDto(searchedUser);
    }

    public UserDto getUserDto(Optional<User> user){
        return new UserDto(user.get().getUserId(), user.get().getUsername());
    }
}
