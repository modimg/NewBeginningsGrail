package com.grail.nb.repository;

import com.grail.nb.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ParticipantRepository extends PagingAndSortingRepository<Participant, String> {
    Participant findByParticipantId(String participantId);
    Page findAll(Pageable pageable);
}
