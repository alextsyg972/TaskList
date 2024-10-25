package ws.task.tasklist.Service;

import ws.task.tasklist.Entity.dto.auth.JwtRequest;
import ws.task.tasklist.Entity.dto.auth.JwtResponse;

public interface AuthService {

    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);

}
