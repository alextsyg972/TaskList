package ws.task.tasklist.config;

import freemarker.template.Configuration;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ws.task.tasklist.Repository.TaskRepository;
import ws.task.tasklist.Repository.UserRepository;
import ws.task.tasklist.Service.ImageService;
import ws.task.tasklist.Service.Impl.*;
import ws.task.tasklist.Service.UserService;
import ws.task.tasklist.Service.props.JwtProperties;
import ws.task.tasklist.Service.props.MinioProperties;
import ws.task.tasklist.security.JwtTokenProvider;
import ws.task.tasklist.security.JwtUserDetailsService;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    @Bean
    @Primary
    public BCryptPasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtProperties jwtProperties() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(
                "dmdqYmhqbmttYmNhamNjZWhxa25hd2puY2xhZWtic3ZlaGtzYmJ1dg=="
        );
        return jwtProperties;
    }

    @Bean
    public UserDetailsService userDetailsService(
            final UserRepository userRepository
    ) {
        return new JwtUserDetailsService(userService(userRepository));
    }

    @Bean
    public MinioClient minioClient() {
        return Mockito.mock(MinioClient.class);
    }

    @Bean
    public MinioProperties minioProperties() {
        MinioProperties properties = new MinioProperties();
        properties.setBucket("images");
        return properties;
    }

    @Bean
    public Configuration configuration() {
        return Mockito.mock(Configuration.class);
    }

    @Bean
    public JavaMailSender mailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    @Primary
    public MailServiceImpl mailService() {
        return new MailServiceImpl(configuration(), mailSender());
    }

    @Bean
    @Primary
    public ImageService imageService() {
        return new ImageServiceImpl(minioClient(), minioProperties());
    }

    @Bean
    public JwtTokenProvider tokenProvider(
            final UserRepository userRepository
    ) {
        return new JwtTokenProvider(jwtProperties(),
                userDetailsService(userRepository),
                userService(userRepository));
    }

    @Bean
    @Primary
    public UserService userService(
            final UserRepository userRepository
    ) {
        return new UserServiceImpl(
                userRepository,
                testPasswordEncoder()
        );
    }

    @Bean
    @Primary
    public TaskServiceImpl taskService(
            final TaskRepository taskRepository
    ) {
        return new TaskServiceImpl(taskRepository, imageService());
    }

    @Bean
    @Primary
    public AuthServiceImpl authService(
            final UserRepository userRepository,
            final AuthenticationManager authenticationManager
    ) {
        return new AuthServiceImpl(
                authenticationManager,
                userService(userRepository),
                tokenProvider(userRepository)
        );
    }

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public TaskRepository taskRepository() {
        return Mockito.mock(TaskRepository.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }
}
