package py.org.fundacionparaguaya.pspserver.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import py.org.fundacionparaguaya.pspserver.security.dtos.UserDetailsDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityDTO;
import py.org.fundacionparaguaya.pspserver.system.dtos.ActivityFeedDTO;
import py.org.fundacionparaguaya.pspserver.system.services.ActivityFeedManager;
import py.org.fundacionparaguaya.pspserver.system.services.ActivityService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/activities")
public class ActivityController {

    private ActivityService activityService;
    private ActivityFeedManager activityFeedManager;

    public ActivityController(ActivityService activityService, ActivityFeedManager activityFeedManager) {
        this.activityService = activityService;
        this.activityFeedManager = activityFeedManager;
    }

    @PostMapping()
    public ResponseEntity<ActivityDTO> addActivity(@Valid @RequestBody ActivityDTO activityDTO) throws URISyntaxException {
        ActivityDTO result = activityService.addActivity(activityDTO);
        return ResponseEntity.created(new URI("/api/v1/activities" + result.getActivityId()))
                .body(result);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ActivityDTO> getActivityById(@PathVariable("activityId") Long activityId) {
        ActivityDTO dto = activityService.getActivityById(activityId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping()
    public ResponseEntity<List<ActivityDTO>> getAllActivities() {
        List<ActivityDTO> activities = activityService.getAllActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/feed")
    public ResponseEntity<List<ActivityFeedDTO>> showActivityFeed(@AuthenticationPrincipal UserDetailsDTO details) {
        return ResponseEntity.ok(activityFeedManager.showActivityFeedByUserDetails(details));
    }
}
