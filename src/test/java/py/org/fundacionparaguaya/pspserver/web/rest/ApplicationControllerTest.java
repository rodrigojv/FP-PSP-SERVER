package py.org.fundacionparaguaya.pspserver.web.rest;

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

import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.DashboardDTO;
import py.org.fundacionparaguaya.pspserver.network.services.ApplicationService;
import py.org.fundacionparaguaya.pspserver.network.services.OrganizationService;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotIndicators;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SnapshotTaken;
import py.org.fundacionparaguaya.pspserver.system.dtos.CityDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.CountryDTO;
import py.org.fundacionparaguaya.pspserver.util.TestHelper;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * created by mcespedes on 9/4/17
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = ApplicationController.class)
@ActiveProfiles("test")
public class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    //CHECKSTYLE:OFF
    @MockBean
    private OrganizationService organizationService;
    //CHECKSTYLE:ON

    private ApplicationDTO mockApplication;

    @Before
    public void setup() {
        mockApplication = ApplicationDTO.builder()
                .name("foo.name")
                .code("foo.code")
                .description("foo.description")
                .isActive(true)
                .country(getCountryTest())
                .city(getCityTest())
                .information("foo.information")
                .build();
    }

    @Test
    public void requestingPutApplicationShouldAddNewApplication() throws Exception {

        when(applicationService
                .addApplication(anyObject()))
                .thenReturn(mockApplication);

        String json = TestHelper.mapToJson(mockApplication);

        mockMvc.perform(post("/api/v1/applications")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(mockApplication.getName())));
    }

    @Test
    public void requestingPostApplicationShouldUpdateApplication() throws Exception {
        Long applicationId = 9999L;

        when(applicationService
                .updateApplication(eq(applicationId), anyObject()))
                .thenReturn(mockApplication);

        String json = TestHelper.mapToJson(mockApplication);
        mockMvc
                .perform(put("/api/v1/applications/{applicationId}", applicationId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(mockApplication.getName())));
    }

    private CountryDTO getCountryTest() {
        CountryDTO dto = new CountryDTO();
        dto.setId(new Long(1));
        dto.setCountry("foo.COUNTRY");
        return dto;
    }

    private CityDTO getCityTest() {
        CityDTO dto = new CityDTO();
        dto.setId(new Long(1));
        dto.setCity("foo.CITY");
        return dto;
    }

    private DashboardDTO getDashboardTest() {
        DashboardDTO dto = new DashboardDTO();
        dto.setNumberOfFamilies(new Long(1));
        dto.setActivityFeed(new ArrayList<>());
        dto.setSnapshotIndicators(new SnapshotIndicators());
        dto.setSnapshotTaken(new SnapshotTaken());
        dto.setTopOfIndicators(new ArrayList<>());
        return dto;
    }

}