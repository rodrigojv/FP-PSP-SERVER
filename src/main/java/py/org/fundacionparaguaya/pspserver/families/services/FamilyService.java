package py.org.fundacionparaguaya.pspserver.families.services;

import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;

import java.util.List;

public interface FamilyService {

    FamilyDTO updateFamily(Long familyId, FamilyDTO familyDTO);

    FamilyDTO addFamily(FamilyDTO familyDTO);

    FamilyDTO getFamilyById(Long familyId);

    List<FamilyDTO> getAllFamilies();

    void deleteFamily(Long familyId);

    String generateFamilyCode(PersonEntity person);

    FamilyEntity createFamilyFromSnapshot(UserDetailsDTO details,
            NewSnapshot snapshot, String code, PersonEntity person);

    List<FamilyDTO> listFamilies(FamilyFilterDTO filter,
            UserDetailsDTO userDetails);

    Long countFamiliesByDetails(UserDetailsDTO userDetails);

    Long countFamiliesByFilter(FamilyFilterDTO filter);

    List<FamilyEntity> findByOrganizationId(Long organizationId);

    FamilyEntity getOrCreateFamilyFromSnapshot(UserDetailsDTO details, NewSnapshot snapshot, PersonEntity personEntity);

}
