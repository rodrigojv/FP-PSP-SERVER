package py.org.fundacionparaguaya.pspserver.web.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.SurveyDefinition;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveyService;
import py.org.fundacionparaguaya.pspserver.surveys.services.SurveySnapshotsManager;
import py.org.fundacionparaguaya.pspserver.util.TestHelper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rodrigovillalba on 11/3/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SurveyController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class SurveyControllerTest {

    private static final Long SURVEY_ID = 1L;
    private static final String SURVEY_DEFAULTS = "/survey_defaults.json";
    public static final String LAST_MODIFIED = "2018-03-16T15:30";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @MockBean
    private SurveySnapshotsManager surveySnapshotsManager;

    @Autowired
    private SurveyController controller;

    @Test
    public void shouldGetAllSurveys() throws Exception {
        List<SurveyDefinition> surveyDefinitions = surveyList();
        when(surveyService.listSurveys(anyObject(), anyString()))
                .thenReturn(surveyDefinitions);

        this.mockMvc.perform(get("/api/v1/surveys")).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("surveys-list",
                        preprocessResponse(prettyPrint()),
                        responseFields(surveys)));
    }

    @Test
    public void shouldGetSurveysByLastModfiedGtThan() throws Exception {
        List<SurveyDefinition> surveyDefinitions = surveyList();
        when(surveyService.listSurveys(anyObject(), eq(LAST_MODIFIED)))
                .thenReturn(surveyDefinitions);

        this.mockMvc.perform(get("/api/v1/surveys").param("last_modified_gt", LAST_MODIFIED)).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("surveys-by-last_modified_gt",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(surveys)));
    }

    @Test
    public void shouldGetSurveyById() throws Exception {

        when(surveyService.getSurveyDefinition(eq(SURVEY_ID)))
                .thenReturn(getDefinition());

        this.mockMvc
                .perform(RestDocumentationRequestBuilders
                        .get("/api/v1/surveys/{survey_id}", SURVEY_ID))
                .andDo(print()).andExpect(status().isOk())
                .andDo(document("surveys-by-id",
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("survey_id")
                                .description("The survey's id")),
                        responseFields(survey)));
    }

    @Test
    public void shouldPostToCreateSurvey() throws Exception {
        SurveyDefinition definition = getDefinition();

        when(surveyService.addSurveyDefinition(anyObject()))
                .thenReturn(definition);

        String content = TestHelper.mapToJson(definition);
        this.mockMvc
                .perform(post("/api/v1/surveys").content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print()).andExpect(status().isCreated())
                .andDo(document("surveys-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(survey)));
    }

    private List<SurveyDefinition> surveyList() {
        return Arrays.asList(getDefinition());
    }

    public SurveyDefinition getDefinition() {
        SurveyDefinition def = (SurveyDefinition) TestHelper
                .mapToObjectFromFile(SURVEY_DEFAULTS, SurveyDefinition.class);

        return new SurveyDefinition().id(SURVEY_ID).title(def.getTitle())
                .description(def.getDescription())
                .surveySchema(def.getSurveySchema())
                .surveyUISchema(def.getSurveyUISchema())
                .organizations(def.getOrganizations())
                .applications(def.getApplications());
    }

    private FieldDescriptor[] survey = new FieldDescriptor[] {
            fieldWithPath("id").type(JsonFieldType.NUMBER)
                    .description("The survey's id"),
            fieldWithPath("title").type(JsonFieldType.STRING)
                    .description("The survey's main title"),
            fieldWithPath("description").type(JsonFieldType.STRING)
                    .description("The survey's short description"),
            fieldWithPath("created_at").type(JsonFieldType.NULL)
                    .description("ISO 8601 formatted creation date"),
            fieldWithPath("last_modified_at").type(JsonFieldType.NULL)
                    .description("ISO 8601 formatted last modification date"),
            fieldWithPath("user_created").type(JsonFieldType.NULL)
                    .description("The user that created this survey"),
            fieldWithPath("survey_schema").type(JsonFieldType.OBJECT)
                    .description(
                            "The schema of this survey. For each key/value pair,"
                            + "defines the type and other attributes"),
            fieldWithPath("survey_ui_schema").type(JsonFieldType.OBJECT)
                    .description(
                            "The UI schema of this survey. Similar to survey_schema, this property"
                            + "describes the attributes of this survey that should be taken into"
                            + "consideration when rendering this survey."),
            fieldWithPath("organizations").type(JsonFieldType.ARRAY)
                    .description("The list of organizations"),
            fieldWithPath("applications").type(JsonFieldType.ARRAY)
                    .description("The list of applications"), };

    private FieldDescriptor[] surveys = new FieldDescriptor[] {
            fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                    .description("The survey's id"),
            fieldWithPath("[].title").type(JsonFieldType.STRING)
                    .description("The survey's main title"),
            fieldWithPath("[].description").type(JsonFieldType.STRING)
                    .description("The survey's short description"),
            fieldWithPath("[].created_at").type(JsonFieldType.NULL)
                    .description("ISO 8601 formatted creation date"),
            fieldWithPath("[].last_modified_at").type(JsonFieldType.NULL)
                    .description("ISO 8601 formatted last modification date"),
            fieldWithPath("[].user_created").type(JsonFieldType.NULL)
                    .description("The user that created this survey"),
            fieldWithPath("[].survey_schema").type(JsonFieldType.OBJECT)
                    .description(
                            "The schema of this survey. For each key/value pair,"
                            + "defines the type and other attributes"),
            fieldWithPath("[].survey_ui_schema").type(JsonFieldType.OBJECT)
                    .description(
                            "The UI schema of this survey. Similar to survey_schema,"
                            + "this property describes the attributes of this survey"
                            + "that should be taken into consideration when rendering this survey."),
            fieldWithPath("[].organizations").type(JsonFieldType.ARRAY)
                    .description("The list of organizations"),
            fieldWithPath("[].applications").type(JsonFieldType.ARRAY)
                    .description("The list of applications"),
            fieldWithPath("[].applications").type(JsonFieldType.ARRAY)
                    .description("The list of applications")};
}
