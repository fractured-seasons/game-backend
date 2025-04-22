package com.game.backend.controllers.updates;

import com.game.backend.dtos.updates.UpdateDTO;
import com.game.backend.models.updates.Update;
import com.game.backend.security.response.ApiResponse;
import com.game.backend.services.updates.UpdateService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/update")
public class UpdateController {
    private final UpdateService updateService;

    public UpdateController(final UpdateService updateService) {
        this.updateService = updateService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> createUpdate(@RequestBody @Valid UpdateDTO update, @AuthenticationPrincipal UserDetails userDetails) {
        Update createdUpdate = updateService.createUpdate(update, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUpdate);
    }

    @GetMapping()
    public ResponseEntity<Page<UpdateDTO>> getAllUpdates(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UpdateDTO> updatesPage = updateService.getAllUpdates(pageable, userDetails);

        return new ResponseEntity<>(updatesPage, HttpStatus.OK);
    }

    @PutMapping("/{updateId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> updateUpdate(@PathVariable Long updateId, @RequestBody @Valid UpdateDTO updateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        Update updatedUpdate = updateService.updateUpdate(updateId, updateDTO, userDetails);
        return ResponseEntity.ok(updatedUpdate);
    }

    @DeleteMapping("/{updateId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<ApiResponse> deleteUpdate(@PathVariable Long updateId) {
        updateService.deleteUpdateById(updateId);
        return ResponseEntity.ok(new ApiResponse(true, "Update deleted successfully"));
    }
}
