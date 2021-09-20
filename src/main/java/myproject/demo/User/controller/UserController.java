package myproject.demo.User.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import myproject.demo.KeyCloak.service.Token;
import myproject.demo.KeyCloak.service.TokenProvider;
import myproject.demo.User.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        Token token = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<Token> refresh (@RequestBody RefreshTokenPayload refreshTokenPayLoad){
        return ResponseEntity.ok(refresh(refreshTokenPayLoad.refreshToken));
    }

    private Token refresh(String refreshToken) {
        return tokenProvider.refresh(refreshToken);
    }


}

@Value
@Getter
class LoginRequest{
    String username;
    String password;
}

@Value
class LoginResponse{
    Token token;
    public LoginResponse(Token token) {
        this.token = token;
    }
}

@Value
class RefreshTokenPayload {
    public String refreshToken;
}
