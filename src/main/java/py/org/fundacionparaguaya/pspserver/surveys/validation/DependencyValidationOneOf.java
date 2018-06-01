package py.org.fundacionparaguaya.pspserver.surveys.validation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.Property;

public class DependencyValidationOneOf implements DependencyValidation{


    public  JsonObject getDependenciesAndRequiredProperties(String keyProperty, JsonElement enumValue,
                                                            JsonObject jsonSchemaDependency){

        if (jsonSchemaDependency.has(keyProperty)) {
            JsonObject jsonObject = new JsonObject();
            JsonArray oneOfArray;
            oneOfArray = jsonSchemaDependency.getAsJsonObject(keyProperty).getAsJsonArray(ONE_OF);
            for (int i = 0; i < oneOfArray.size(); i++) {
                JsonObject oneOfProperties = oneOfArray.get(i).getAsJsonObject().getAsJsonObject(PROPERTIES);
                if (oneOfProperties.getAsJsonObject(keyProperty)
                        .getAsJsonArray(ENUM).contains(enumValue)){
                    jsonObject.add(PROPERTIES, oneOfProperties);
                    if (oneOfArray.get(i).getAsJsonObject().has(REQUIRED)){
                        jsonObject.add(REQUIRED, oneOfArray.get(i)
                                .getAsJsonObject().getAsJsonArray(REQUIRED));
                    }
                    return jsonObject;
                }
            }
        }
        return null;
    }



    public boolean checkDependency(JsonObject jsonDependencyData, JsonObject jsonDependenciesAndRequired){


        // every dependency that is present, must conform to the dependencies in the schema
        JsonObject jsonSchemaProperties= jsonDependenciesAndRequired.getAsJsonObject(PROPERTIES);
        for (String key : jsonDependencyData.keySet()){
            if (!jsonSchemaProperties.has(key)){
                return false;
            }

            // type check
            String propertySchemaType = jsonSchemaProperties.getAsJsonObject(key)
                    .getAsJsonPrimitive("type").getAsString();
            Property property = new Property();
            property.setType(Property.TypeEnum.fromValue(propertySchemaType));

            boolean isValid = PropertyValidator.validType().apply
                    (property, key, new Gson().fromJson(jsonDependencyData.get(key), Object.class)).isValid();

            if (!isValid){
                return false;
            }
        }

        // if there are required elements, every required dependency should be present in the dependencyData
        if (jsonDependenciesAndRequired.has(REQUIRED)){
            JsonArray dependenciesArray = jsonDependenciesAndRequired.getAsJsonArray(REQUIRED);

            for (int i = 0; i < dependenciesArray.size(); i++){
                if (!jsonDependencyData.has(dependenciesArray.get(i).getAsString())){
                    return false;
                }
            }
        }

        return true;
    }

}
