package com.volosinzena.barolab;

import com.volosinzena.barolab.controller.dto.PostDto;
import com.volosinzena.barolab.controller.dto.Role;
import com.volosinzena.barolab.logging.JunitLogExtension;
import com.volosinzena.barolab.mapper.PostMapper;
import com.volosinzena.barolab.service.PostService;
import com.volosinzena.barolab.service.TokenService;
import com.volosinzena.barolab.service.UserService;
import com.volosinzena.barolab.service.model.Post;
import com.volosinzena.barolab.service.model.Status;
import com.volosinzena.barolab.service.model.User;
import com.volosinzena.barolab.service.model.VoteValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(JunitLogExtension.class)
@DisplayName("Endpoints/Auth")
class EndpointsAuthTest {

    private static final String USER_TOKEN = "user-token";
    private static final String ADMIN_TOKEN = "admin-token";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        TokenService tokenService() {
            return Mockito.mock(TokenService.class);
        }

        @Bean
        @Primary
        PostService postService() {
            return Mockito.mock(PostService.class);
        }

        @Bean
        @Primary
        PostMapper postMapper() {
            return Mockito.mock(PostMapper.class);
        }

        @Bean
        @Primary
        UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        when(tokenService.isValidToken(USER_TOKEN)).thenReturn(true);
        when(tokenService.getId(USER_TOKEN)).thenReturn(UUID.randomUUID().toString());
        when(tokenService.getRole(USER_TOKEN)).thenReturn(Role.USER);

        when(tokenService.isValidToken(ADMIN_TOKEN)).thenReturn(true);
        when(tokenService.getId(ADMIN_TOKEN)).thenReturn(UUID.randomUUID().toString());
        when(tokenService.getRole(ADMIN_TOKEN)).thenReturn(Role.ADMIN);
    }

    @Test
    @DisplayName("POST /login returns JWT")
    void login_returnsToken() throws Exception {
        User user = new User(
                UUID.randomUUID(),
                "login",
                "email@example.com",
                "username",
                "secret",
                com.volosinzena.barolab.service.model.Status.ACTIVE,
                com.volosinzena.barolab.service.model.Role.USER,
                Instant.now(),
                Instant.now()
        );

        when(userService.login("login", "secret")).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("jwt-token");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"login\",\"password\":\"secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    @DisplayName("POST /login without body -> 400")
    void login_withoutBody_badRequest() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /sign-up returns JWT and 201")
    void signUp_returnsToken() throws Exception {
        User user = new User(
                UUID.randomUUID(),
                "login",
                "email@example.com",
                "username",
                "secret",
                com.volosinzena.barolab.service.model.Status.ACTIVE,
                com.volosinzena.barolab.service.model.Role.USER,
                Instant.now(),
                Instant.now()
        );

        when(userService.signUp("login", "email@example.com", "username", "secret")).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn("jwt-token");

        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"login\",\"email\":\"email@example.com\",\"username\":\"username\",\"password\":\"secret\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    @DisplayName("POST /sign-up without body -> 400")
    void signUp_withoutBody_badRequest() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Public ping")
    void ping_isPublic() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    @DisplayName("GET /posts requires auth")
    void getPosts_requiresAuth() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /posts invalid token -> 401")
    void getPosts_invalidToken_unauthorized() throws Exception {
        mockMvc.perform(get("/posts")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /posts with USER token")
    void getPosts_withUserToken_ok() throws Exception {
        Post post = new Post();
        UUID postId = UUID.randomUUID();
        post.setId(postId);
        post.setUserId(UUID.randomUUID());
        post.setAuthorUsername("alice");
        post.setRating(1);
        post.setMyVote(VoteValue.LIKE);
        post.setStatus(Status.ACTIVE);
        post.setTitle("t");
        post.setContent("c");
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        PostDto dto = new PostDto(
                post.getId(),
                post.getUserId(),
                post.getAuthorUsername(),
                post.getRating(),
                com.volosinzena.barolab.controller.dto.VoteValue.LIKE,
                com.volosinzena.barolab.controller.dto.Status.ACTIVE,
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );

        when(postService.getAllPosts()).thenReturn(List.of(post));
        when(postMapper.toDto(post)).thenReturn(dto);

        mockMvc.perform(get("/posts")
                        .header("Authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(postId.toString()))
                .andExpect(jsonPath("$[0].author_username").value("alice"))
                .andExpect(jsonPath("$[0].title").value("t"))
                .andExpect(jsonPath("$[0].content").value("c"));
    }

    @Test
    @DisplayName("GET /post/{id} with USER token")
    void getPostById_withUserToken_ok() throws Exception {
        UUID postId = UUID.randomUUID();
        Post post = new Post();
        post.setId(postId);
        post.setUserId(UUID.randomUUID());
        post.setAuthorUsername("alice");
        post.setRating(2);
        post.setMyVote(VoteValue.DISLIKE);
        post.setStatus(Status.ACTIVE);
        post.setTitle("title");
        post.setContent("content");
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());

        PostDto dto = new PostDto(
                post.getId(),
                post.getUserId(),
                post.getAuthorUsername(),
                post.getRating(),
                com.volosinzena.barolab.controller.dto.VoteValue.DISLIKE,
                com.volosinzena.barolab.controller.dto.Status.ACTIVE,
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );

        when(postService.getPostById(postId)).thenReturn(post);
        when(postMapper.toDto(post)).thenReturn(dto);

        mockMvc.perform(get("/post/{postId}", postId)
                        .header("Authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId.toString()))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.author_username").value("alice"));
    }
}
