package br.com.controlapi.web;

import br.com.controlapi.model.entity.User;
import br.com.controlapi.repository.UserRepository;
import br.com.controlapi.service.EmailService;
import br.com.controlapi.service.UserService;
import br.com.controlapi.web.erros.ApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    private ApiExceptionHandler apiExceptionHandler;

    private HttpMessageConverter<?>[] httpMessageConverters;

    @Mock
    private MessageSource messageSource;

    @Mock
    private UserService mockUserService;

    @Mock
    private EmailService mockMailService;

    private MockMvc restMvc;

    private MockMvc restUserMockMvc;

    @BeforeEach
    public void setup() {
        // create ApiExceptionHandler and inject a mocked MessageSource so message lookups don't NPE
        apiExceptionHandler = new ApiExceptionHandler();
        ReflectionTestUtils.setField(apiExceptionHandler, "messageSource", messageSource);

        // provide a real HttpMessageConverter so MockMvc can write/read JSON
        httpMessageConverters = new HttpMessageConverter<?>[]{ new MappingJackson2HttpMessageConverter() };

        AccountController accountController =
                new AccountController(userRepository, userService, mockMailService);



        AccountController accountUserMockController = new AccountController(userRepository, mockUserService, mockMailService);
        this.restMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setMessageConverters(httpMessageConverters)
                .setControllerAdvice(apiExceptionHandler)
                .build();
        this.restUserMockMvc = MockMvcBuilders
                .standaloneSetup(accountUserMockController)
                .setControllerAdvice(apiExceptionHandler)
                .build();
    }

    @Test
    public void testNonAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get("/api/autenticado")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get("/api/autenticado")
             .with(request -> {
                request.setRemoteUser("authenticatedUser");
                return request;
            })
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(view().name("authenticatedUser"));
    }
}
