package py.org.fundacionparaguaya.pspserver.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import py.org.fundacionparaguaya.pspserver.security.constants.TermCondPolType;
import py.org.fundacionparaguaya.pspserver.security.dtos.TermCondPolDTO;
import py.org.fundacionparaguaya.pspserver.security.services.TermCondPolService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/termcondpol")
public class TermCondPolController {

    private TermCondPolService service;

    public TermCondPolController(TermCondPolService service) {
        this.service = service;
    }


    @GetMapping()
    public ResponseEntity<List<TermCondPolDTO>> findAll() {
        List<TermCondPolDTO> dtoList = service.getAll();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/last")
    public ResponseEntity<TermCondPolDTO> getLastTermCondPolByType(
            @RequestParam(value = "type", required = true)
                    TermCondPolType type) {
        TermCondPolDTO dto = service.getLastTermCondPol(type);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{termCondPolId}")
    public ResponseEntity<TermCondPolDTO> update(@RequestParam("html_file") MultipartFile htmlFile,
                                                 @PathVariable Long termCondPolId) throws URISyntaxException {
        TermCondPolDTO dto = service.updateTerms(htmlFile, termCondPolId);
        return ResponseEntity.ok(dto);
    }


    @PostMapping()
    public ResponseEntity<TermCondPolDTO> save(
            @RequestParam("html_file") MultipartFile htmlFile,
            @RequestParam("type") TermCondPolType type,
            @RequestParam("version") String version,
            @RequestParam("year") Integer year)
            throws URISyntaxException {
        TermCondPolDTO dto = service.saveTerms(htmlFile,
                TermCondPolDTO.builder().typeCod(type).version(version).year(year).build());
        URI location = new URI("/termcondpol/" + dto.getId());
        return ResponseEntity.created(location).build();
    }
}
