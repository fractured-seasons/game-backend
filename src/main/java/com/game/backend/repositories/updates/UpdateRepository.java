package com.game.backend.repositories.updates;

import com.game.backend.dtos.updates.UpdateDTO;
import com.game.backend.models.updates.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UpdateRepository extends JpaRepository<Update, Long> {

    @Query("SELECT new com.game.backend.dtos.updates.UpdateDTO(u.id, u.title, u.content, u.createdAt) " +
            "FROM Update u ORDER BY u.createdAt")
    Page<UpdateDTO> findAllUpdates(Pageable pageable);
}
