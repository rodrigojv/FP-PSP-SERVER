package py.org.fundacionparaguaya.pspserver.surveys.validation;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface DependencyValidation {

     String SURVEY_SCHEMA = "survey_schema";

     String DEPENDENCIES = "dependencies";

     String PROPERTIES = "properties";

     String REQUIRED = "required";

     String ONE_OF = "oneOf";

     String ENUM = "enum";

    boolean checkDependency(JsonObject jsonDependencyData, JsonObject jsonDependenciesAndRequired);
    JsonObject getDependenciesAndRequiredProperties(String keyProperty, JsonElement enumValue,
                                                            JsonObject jsonSchemaDependency);

    }
