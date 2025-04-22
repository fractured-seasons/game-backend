package com.game.backend.services.updates.impl;

import com.game.backend.dtos.updates.UpdateDTO;
import com.game.backend.models.User;
import com.game.backend.models.updates.Update;
import com.game.backend.repositories.UserRepository;
import com.game.backend.repositories.updates.UpdateRepository;
import com.game.backend.services.updates.UpdateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UpdateServiceImpl implements UpdateService {
    private final UserRepository userRepository;
    private final UpdateRepository updateRepository;

    public UpdateServiceImpl(final UserRepository userRepository, final UpdateRepository updateRepository) {
        this.userRepository = userRepository;
        this.updateRepository = updateRepository;
    }

    @Override
    public Update createUpdate(UpdateDTO update, UserDetails userDetails) {
        User currentUser = userRepository.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Update newUpdate = new Update();
        newUpdate.setCreatedBy(currentUser);
        newUpdate.setTitle(update.getTitle());
        newUpdate.setContent(update.getContent());

        return updateRepository.save(newUpdate);
    }

    @Override
    public Page<UpdateDTO> getAllUpdates(Pageable pageable, UserDetails userDetails) {
        return updateRepository.findAllUpdates(pageable);
    }

    @Override
    public Update updateUpdate(Long updateId, UpdateDTO updateDTO, UserDetails userDetails) {
        Update currentUpdate = updateRepository.findById(updateId)
                .orElseThrow(() -> new RuntimeException("Update not found"));

        currentUpdate.setTitle(updateDTO.getTitle());
        currentUpdate.setContent(updateDTO.getContent());

        return updateRepository.save(currentUpdate);
    }

    @Override
    public void deleteUpdateById(Long updateId) {
        updateRepository.deleteById(updateId);
    }
}
