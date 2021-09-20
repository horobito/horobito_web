package myproject.demo.User.serviceTest;


import myproject.demo.KeyCloak.service.DuplicateUserSignUpException;
import myproject.demo.KeyCloak.service.TokenProvider;
import myproject.demo.KeyCloak.service.TokenRequest;
import myproject.demo.User.controller.UserExceptionHandler;
import myproject.demo.User.domain.UserRepository;
import myproject.demo.User.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignUpTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    UserExceptionHandler handler;



    @DisplayName(" SignUp Test 1. normal condition")
    @Test
    public void test1(){

        UserService sut = new UserService(userRepository, tokenProvider);


        sut.signUp("test", "test", "test`");

        verify(tokenProvider, times(1)).signUp(any());
        verify(userRepository, times(1)).save(any());

    }

    @DisplayName(" SignUp Test 2. abnormal condition - already Exist")
    @Test
    public void test2(){

        UserService sut = new UserService(userRepository, tokenProvider);

        when(userRepository.existsByUsername(any())).thenThrow(new DuplicateUserSignUpException());
//        doThrow(new DuplicateUserSignUpException()).when(sut).checkDuplicateUser(any());
//        verify(handler, times(1)).duplicateUserHandler(any());
        assertThrows(DuplicateUserSignUpException.class, () -> sut.signUp("test","t","t"));
    }

}
