package py.org.fundacionparaguaya.pspserver.reports.services;

import java.util.List;

import py.org.fundacionparaguaya.pspserver.reports.dtos.OrganizationFamilyDTO;
import py.org.fundacionparaguaya.pspserver.reports.dtos.ReportDTO;
import py.org.fundacionparaguaya.pspserver.reports.dtos.SnapshotFilterDTO;
import py.org.fundacionparaguaya.pspserver.reports.dtos.FamilySnapshotDTO;

/**
 *
 * @author mgonzalez
 *
 */

public interface SnapshotReportManager {

    List<OrganizationFamilyDTO> listFamilyByOrganizationAndCreatedDate(SnapshotFilterDTO filters);

    List<FamilySnapshotDTO> listSnapshotByFamily(SnapshotFilterDTO filters);

    String generateCSVSnapshotByOrganizationAndCreatedDate(SnapshotFilterDTO filters);

    ReportDTO getSnapshotsReportByOrganizationAndCreatedDate(SnapshotFilterDTO filters);

    ReportDTO getSnapshotsReport(SnapshotFilterDTO filters);

    String downloadSnapshotsCSV(SnapshotFilterDTO filters);

}
