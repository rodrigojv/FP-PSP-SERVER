package py.org.fundacionparaguaya.pspserver.web.rest;

import com.google.common.collect.ImmutableMultimap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import py.org.fundacionparaguaya.pspserver.common.constants.ErrorCodes;
import py.org.fundacionparaguaya.pspserver.common.exceptions.CustomParameterizedException;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDTO;
import py.org.fundacionparaguaya.pspserver.security.services.UserService;
import py.org.fundacionparaguaya.pspserver.util.TestHelper;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rodrigovillalba on 8/27/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private UserController controller;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private UserDTO mockUser;

    @Before
    public void setup() {
        mockUser = UserDTO.builder()
                .username("foo.user")
                .email("foo@bar")
                .pass("123")
                .active(true)
                .build();
    }

    @Test
    public void requestingPutUserShouldAddNewUser() throws Exception {
        when(userService.addUser(anyObject())).thenReturn(mockUser);

        String json = TestHelper.mapToJson(mockUser);
        mockMvc.perform(post("/api/v1/users")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(mockUser.getUsername())));
    }

    @Test
    public void requestingPostUserShouldUpdateUser() throws Exception {
        Long userId = 999L;
        String principal = "principal";
        when(userService.updateUserByRequest(eq(userId), anyObject(), eq(principal)))
                .thenReturn(mockUser);

        String json = TestHelper.mapToJson(mockUser);
        mockMvc.perform(put("/api/v1/users/{userId}", userId)
                .principal(new Principal() {
                    @Override
                    public String getName() {
                        return "principal";
                    }
                })
                .content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(mockUser.getEmail())))
                .andExpect(jsonPath("$.active", is(mockUser.isActive())));
    }

    @Test
    public void requestingPostUserShouldFailIfNotValidArgument()
            throws Exception {
        List<String> properties = Arrays.asList("username", "email", "pass");
        mockMvc.perform(post("/api/v1/users").content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                    containsString(ErrorCodes.VALIDATION_FAILURE.getMessage())))
                .andExpect(jsonPath("$.fieldErrors.[0].objectName",
                    is("userDTO")))
                .andExpect(jsonPath("$.fieldErrors.[0].field",
                    isIn(properties)))
                .andExpect(jsonPath("$.fieldErrors.[0].messages[0]")
                    .value("NotNull"))
                .andExpect(jsonPath("$.fieldErrors.[1].objectName",
                    is("userDTO")))
                .andExpect(jsonPath("$.fieldErrors.[1].field",
                    isIn(properties)))
                .andExpect(jsonPath("$.fieldErrors.[1].messages[0]")
                        .value("NotNull"));
    }

    @Test
    public void requestingPostUserShouldFailIfUserAlreadyExists()
                                                            throws Exception {
        UserDTO dto = UserDTO.builder()
                .username("admin")
                .email("foo@bar")
                .pass("123")
                .build();

        when(userService.addUser(anyObject()))
                .thenThrow(new CustomParameterizedException(
                        "User already exists.",
                        new ImmutableMultimap.Builder<String, String>()
                                .put("username", dto.getUsername())
                                .build()
                                .asMap()));

        String json = TestHelper.mapToJson(dto);
        mockMvc.perform(post("/api/v1/users")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                        containsString("User already exists.")))
                .andExpect(jsonPath("$.fieldErrors.[0].field",
                        is("username")))
                .andExpect(jsonPath("$.fieldErrors.[0].messages[0]")
                        .value("admin"));
    }

    @Test
    public void requestingGetUserShouldFailIfUserIsInvalid() throws Exception {
        String errorMsg = "Argument was -999999 but expected nonnegative";
        when(userService.getUserById(anyLong()))
                .thenThrow(new IllegalArgumentException(errorMsg));

        mockMvc.perform(get("/api/v1/users/{userId}", -999999))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(errorMsg));
    }
}
