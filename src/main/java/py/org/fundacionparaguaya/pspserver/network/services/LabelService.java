package py.org.fundacionparaguaya.pspserver.network.services;

import py.org.fundacionparaguaya.pspserver.network.dtos.LabelDTO;

import java.util.List;

/**
 * author: nvaldez
 */
public interface LabelService {
    LabelDTO getLabelById(Long labelId);
    List<LabelDTO> getAllLabels();
    LabelDTO addLabel(LabelDTO labelDTO);
    List<LabelDTO> getLabelsByDescription(String description);
}
