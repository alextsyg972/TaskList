package ws.task.tasklist.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Entity.dto.auth.JwtRequest;
import ws.task.tasklist.Entity.dto.auth.JwtResponse;
import ws.task.tasklist.Entity.dto.user.UserDto;
import ws.task.tasklist.Entity.dto.validation.OnCreate;
import ws.task.tasklist.Service.AuthService;
import ws.task.tasklist.Service.Mappers.UserMapper;
import ws.task.tasklist.Service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody final JwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createUser = userService.create(user);
        return userMapper.toDto(createUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }


}
