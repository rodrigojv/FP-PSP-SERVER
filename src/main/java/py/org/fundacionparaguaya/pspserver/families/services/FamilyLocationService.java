package py.org.fundacionparaguaya.pspserver.families.services;

import py.org.fundacionparaguaya.pspserver.families.dtos.FamilyLocationDTO;
import py.org.fundacionparaguaya.pspserver.surveys.dtos.NewSnapshot;

/**
 * Created by rodrigovillalba on 7/16/18.
 */
public interface FamilyLocationService {

    FamilyLocationDTO getFamilyLocationFromSnapshot(NewSnapshot snapshot);

}
