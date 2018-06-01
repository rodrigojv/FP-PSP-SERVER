package py.org.fundacionparaguaya.pspserver.system.mapper;

import py.org.fundacionparaguaya.pspserver.network.dtos.ApplicationDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationDTO;
import py.org.fundacionparaguaya.pspserver.system.entities.CityEntity;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by bsandoval on 23/05/18.
 */
public class ActivityParamMapper {

    private static final String DEFAULT_PARAM = "xx";

    public ActivityParamMapper() {
    }

    public String organizationToParam(OrganizationDTO organization){
        return wrappedObject(organization, OrganizationDTO::getDescription);
    }

    public String applicationToParam(ApplicationDTO application){
        return wrappedObject(application, ApplicationDTO::getDescription);
    }

    public String familyCityToParam(CityEntity city){
        return wrappedObject(city, CityEntity::getCity);
    }

    public String wrappedParam(String s){
        return Optional.ofNullable(s).orElse(DEFAULT_PARAM);
    }

    private <T> String wrappedObject(T object, Function<? super T, String> function){
        return Optional.ofNullable(object).map(function).orElse(DEFAULT_PARAM);
    }
}
