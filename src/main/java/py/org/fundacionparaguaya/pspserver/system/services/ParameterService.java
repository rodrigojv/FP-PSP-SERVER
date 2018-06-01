package py.org.fundacionparaguaya.pspserver.system.services;

import java.util.List;

import py.org.fundacionparaguaya.pspserver.system.dtos.ParameterDTO;

public interface ParameterService {

    ParameterDTO updateParameter(Long parameterId, ParameterDTO parameterDTO);

    ParameterDTO addParameter(ParameterDTO parameterDTO);

    ParameterDTO getParameterById(Long parameterId);

    List<ParameterDTO> getAllParameters();

    void deleteParameter(Long parameterId);

    ParameterDTO getParameterByKey(String keyParameter);

}