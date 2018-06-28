package py.org.fundacionparaguaya.pspserver.web.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import py.org.fundacionparaguaya.pspserver.common.exceptions.UnknownResourceException;
import py.org.fundacionparaguaya.pspserver.network.dtos.LabelDTO;
import py.org.fundacionparaguaya.pspserver.network.services.LabelService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/labels")
public class LabelController {

    private LabelService service;

    public LabelController(LabelService service) {
    this.service = service;
    }

    @GetMapping("/{labelId}")
    public ResponseEntity<LabelDTO> getLabelById(
            @PathVariable("labelId") Long labelId) throws UnknownResourceException {
        LabelDTO dto = service.getLabelById(labelId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        List<LabelDTO> dtoList = service.getAllLabels();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<LabelDTO>> getLabelsByDescription(
            @RequestParam("desc") String description) throws UnknownResourceException {
        List<LabelDTO> dto = service.getLabelsByDescription(description);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LabelDTO> addLabel(
            @Valid @RequestBody LabelDTO dto) throws URISyntaxException {
        LabelDTO result = service.addLabel(dto);
        return ResponseEntity.created(new URI("/labels/" + result.getId()))
                .body(result);
    }
}