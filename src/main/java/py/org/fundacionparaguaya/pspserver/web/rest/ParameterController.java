package py.org.fundacionparaguaya.pspserver.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.org.fundacionparaguaya.pspserver.system.dtos.ParameterDTO;
import py.org.fundacionparaguaya.pspserver.system.services.ParameterService;

@RestController
@RequestMapping(value = "/api/v1/parameters")
public class ParameterController {

    private static final Logger LOG = LoggerFactory
            .getLogger(ParameterController.class);

    private ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @PostMapping()
    public ResponseEntity<ParameterDTO> addParameter(
            @Valid @RequestBody ParameterDTO parameterDTO)
            throws URISyntaxException {
        ParameterDTO result = parameterService.addParameter(parameterDTO);
        return ResponseEntity
                .created(new URI(
                        "/api/v1/parameters/" + result.getParameterId()))
                .body(result);
    }

    @PutMapping("/{parameterId}")
    public ResponseEntity<ParameterDTO> updateParameter(
            @PathVariable("parameterId") Long parameterId,
            @RequestBody ParameterDTO parameterDTO) {
        ParameterDTO result = parameterService.updateParameter(parameterId,
                parameterDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{parameterId}")
    public ResponseEntity<ParameterDTO> getParameterById(
            @PathVariable("parameterId") Long parameterId) {
        ParameterDTO dto = parameterService.getParameterById(parameterId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<List<ParameterDTO>> getAllParameters() {
        List<ParameterDTO> parameters = parameterService.getAllParameters();
        return ResponseEntity.ok(parameters);
    }

    @DeleteMapping("/{parameterId}")
    public ResponseEntity<Void> deleteParameter(
            @PathVariable("parameterId") Long parameterId) {
        LOG.debug("REST request to delete Parameter: {}", parameterId);
        parameterService.deleteParameter(parameterId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/key")
    public ResponseEntity<ParameterDTO> getParameterByKey(
            @RequestParam(value = "keyParameter", required = true) String keyParameter) {
        ParameterDTO dto = parameterService.getParameterByKey(keyParameter);
        return ResponseEntity.ok(dto);
    }
}