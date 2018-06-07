package py.org.fundacionparaguaya.pspserver.network.services;

import py.org.fundacionparaguaya.pspserver.network.dtos.LabelDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationLabelDTO;
import py.org.fundacionparaguaya.pspserver.network.dtos.OrganizationLabelDTORequest;

import java.util.List;

public interface OrganizationLabelService {

    OrganizationLabelDTO getOrganizationLabelById(Long organizationLabelId);

    List<OrganizationLabelDTO> getAllOrganizationsLabels();

    OrganizationLabelDTO updateOrganizationLabel(Long organizationLabelId, OrganizationLabelDTO dto);

    List<OrganizationLabelDTO> addOrganizationLabel(OrganizationLabelDTORequest dto);

    List<LabelDTO> getLabelsByOrganizationId(Long organizationId);

    void deleteOrganizationLabel(Long organizationLabelId);

}
