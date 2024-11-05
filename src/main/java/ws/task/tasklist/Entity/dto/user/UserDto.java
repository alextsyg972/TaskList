package ws.task.tasklist.Entity.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ws.task.tasklist.Entity.dto.validation.OnCreate;
import ws.task.tasklist.Entity.dto.validation.OnUpdate;

@Data
@Schema(description = "User DTO")
public class UserDto {

    @Schema(description = "User id", example = "1")
    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "User name", example = "Alex Tsyg")
    @NotNull(message = "name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "name length musst be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @Schema(description = "Username", example = "newpls")
    @NotNull(message = "username must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "username length musst be smaller than 255 symbols", groups = {OnUpdate.class, OnCreate.class})
    private String username;

    @Schema(description = "User password", example = "123")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @Schema(description = "User password confirmation")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = {OnCreate.class})
    private String passwordConfirmation;

}
