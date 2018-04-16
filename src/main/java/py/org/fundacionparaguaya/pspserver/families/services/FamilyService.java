package py.org.fundacionparaguaya.pspserver.families.services;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyDTO;
import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyFilterDTO;
import py.org.fundacionparaguaya.pspserver.families.entities.FamilyEntity;
import py.org.fundacionparaguaya.pspserver.families.entities.PersonEntity;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;

import java.io.IOException;
import java.util.List;

public interface FamilyService {

    FamilyDTO updateFamily(Long familyId, FamilyDTO familyDTO);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    FamilyDTO updateFamilyAsync(Long familyId);

    FamilyDTO addFamily(FamilyDTO familyDTO);

    FamilyDTO getFamilyById(Long familyId);

    List<FamilyDTO> getAllFamilies();

    void deleteFamily(Long familyId);

    String generateFamilyCode(PersonEntity person);

    FamilyEntity createOrReturnFamilyFromSnapshot(UserDetailsDTO details,
            NewSnapshot snapshot, String code, PersonEntity person);

    List<FamilyDTO> listFamilies(FamilyFilterDTO filter,
            UserDetailsDTO userDetails);

    Long countFamiliesByDetails(UserDetailsDTO userDetails);

    Long countFamiliesByFilter(FamilyFilterDTO filter);

    List<FamilyEntity> findByOrganizationId(Long organizationId);

    FamilyEntity getOrCreateFamilyFromSnapshot(UserDetailsDTO details,
            NewSnapshot snapshot, PersonEntity personEntity);

    List<FamilyDTO> listDistinctFamiliesSnapshotByUser(UserDetailsDTO details,
            String name);

    FamilyDTO updateFamily(Long familyId);

    String imageUpload(Long idFamily, MultipartFile file) throws IOException;
}
