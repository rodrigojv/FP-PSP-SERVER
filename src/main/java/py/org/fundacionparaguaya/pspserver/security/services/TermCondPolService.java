package py.org.fundacionparaguaya.pspserver.security.services;

import py.org.fundacionparaguaya.pspserver.security.constants.TermCondPolType;
import py.org.fundacionparaguaya.pspserver.security.dtos.TermCondPolDTO;

import java.util.List;

/**
 * @author mgonzalez
 */
public interface TermCondPolService {
    TermCondPolDTO getLastTermCondPol(TermCondPolType type, Long applicationId);

    TermCondPolDTO updateTerms(String htmlFile, Long termCondPolId);

    TermCondPolDTO saveTerms(TermCondPolDTO termCondPolDTO);

    List<TermCondPolDTO> getAll();
}
