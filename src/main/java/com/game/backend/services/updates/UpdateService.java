package com.game.backend.services.updates;

import com.game.backend.dtos.updates.UpdateDTO;
import com.game.backend.models.updates.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface UpdateService {
    Update createUpdate(UpdateDTO update, UserDetails userDetails);

    Page<UpdateDTO> getAllUpdates(Pageable pageable, UserDetails userDetails);

    Update updateUpdate(Long updateId, UpdateDTO updateDTO, UserDetails userDetails);

    void deleteUpdateById(Long updateId);
}
