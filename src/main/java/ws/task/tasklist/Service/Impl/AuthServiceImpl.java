package ws.task.tasklist.Service.Impl;

import org.springframework.stereotype.Service;
import ws.task.tasklist.Entity.dto.auth.JwtRequest;
import ws.task.tasklist.Entity.dto.auth.JwtResponse;
import ws.task.tasklist.Service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
