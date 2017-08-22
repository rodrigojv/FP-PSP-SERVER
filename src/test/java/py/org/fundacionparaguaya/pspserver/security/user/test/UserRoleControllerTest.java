package py.org.fundacionparaguaya.pspserver.security.user.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import py.org.fundacionparaguaya.pspserver.PspServerApplication;
import py.org.fundacionparaguaya.pspserver.security.user.domain.User;
import py.org.fundacionparaguaya.pspserver.security.userrole.domain.Role;
import py.org.fundacionparaguaya.pspserver.security.userrole.domain.UserRole;
import py.org.fundacionparaguaya.pspserver.security.userrole.web.controller.UserRoleController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = PspServerApplication.class, loader = AnnotationConfigContextLoader.class)
public class UserRoleControllerTest {

	private static final String RESOURCE_LOCATION_PATTERN = "http://localhost/api/v1/user-roles/[0-9]+";

	@InjectMocks
	UserRoleController controller;

	@Autowired
	WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void initTests() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
//	@Test
    public void shouldHaveEmptyDB() throws Exception {
        mvc.perform(get("/api/v1/user-roles")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
    
    
    @Test
    public void shouldCreateRetrieveDelete() throws Exception {
        UserRole r1 = mockUserRole();
        byte[] r1Json = toJson(r1);

        //CREATE
        MvcResult result = mvc.perform(post("/api/v1/user-roles")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RESOURCE_LOCATION_PATTERN))
                .andReturn();
        long userroleid = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        //RETRIEVE
        mvc.perform(get("/api/v1/user-role/" + userroleid)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userRoleId", is((int) userroleid)))
                .andExpect(jsonPath("$.role", is(r1.getRole())));

        //DELETE
        mvc.perform(delete("/api/v1/user-roles/" + userroleid))
                .andExpect(status().isNoContent());

        //RETRIEVE should fail
        mvc.perform(get("/api/v1/user-roles/" + userroleid)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    //@Test
    public void shouldCreateAndUpdateAndDelete() throws Exception {
    	UserRole r1 = mockUserRole();
        byte[] r1Json = toJson(r1);
        //CREATE
        MvcResult result = mvc.perform(post("/api/v1/user-roles")
                .content(r1Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern(RESOURCE_LOCATION_PATTERN))
                .andReturn();
        long userRoleId = getResourceIdFromUrl(result.getResponse().getRedirectedUrl());

        UserRole r2 = mockUserRole();
        r2.setUserRoleId(userRoleId);
        byte[] r2Json = toJson(r2);

        //UPDATE
        result = mvc.perform(put("/api/v1/user-roles/" + userRoleId)
                .content(r2Json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        //RETRIEVE updated
        mvc.perform(get("/api/v1/user-roles/" + userRoleId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userRoleId", is((int) userRoleId)))
                .andExpect(jsonPath("$.role", is(r2.getRole())));

        //DELETE
        mvc.perform(delete("/api/v1/user-roles/" + userRoleId))
                .andExpect(status().isNoContent());
    }


    private long getResourceIdFromUrl(String locationUrl) {
        String[] parts = locationUrl.split("/");
        return Long.valueOf(parts[parts.length - 1]);
    }


    private UserRole mockUserRole() {
    	UserRole r = new UserRole();
    	r.setUser(new User(new Long(1),"user","pass",true));
        r.setRole(Role.ADMIN);
        return r;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        return map.writeValueAsString(r).getBytes();
    }

    private static ResultMatcher redirectedUrlPattern(final String expectedUrlPattern) {
        return new ResultMatcher() {
            public void match(MvcResult result) {
                Pattern pattern = Pattern.compile("\\A" + expectedUrlPattern + "\\z");
                assertTrue(pattern.matcher(result.getResponse().getRedirectedUrl()).find());
            }
        };
    }

}
