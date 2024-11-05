package ws.task.tasklist.Entity.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {

    @Schema(description = "username", example = "johndoe@gmail.com")
    @NotNull(message = "username must be not null")
    private String username;

    @Schema(description = "password", example = "12345")
    @NotNull(message = "password must be not null")
    private String password;
}
