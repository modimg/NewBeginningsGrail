package com.grail.nb.service;

import com.grail.nb.controller.ParticipantController;
import com.grail.nb.model.Participant;
import com.grail.nb.repository.ParticipantRepository;
import com.grail.nb.utils.ParticipantUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component("participantService")
public class ParticipantService {
    private static final Logger log = LoggerFactory.getLogger(ParticipantService.class);

    @Autowired
    ParticipantRepository participantRepository;

    public Participant createParticipant(Participant participant) throws Exception{
        log.info("ParticipantService: createParticipant: " + participant.toString());
        try{
            participant.setParticipantId(ParticipantUtils.getParticipantId());
            participant.getAddress().setAddressId(null);

            participant.setCellphoneno(ParticipantUtils.standardizePhoneNo(participant.getCellphoneno()));
            participant.setWorkphoneno(ParticipantUtils.standardizePhoneNo(participant.getWorkphoneno()));
            participant.setHomephoneno(ParticipantUtils.standardizePhoneNo(participant.getHomephoneno()));

            return participantRepository.save(participant);
        } catch (Exception e) {
            throw new Exception("ParticipantService: createParticipant: participant is null!!!");
        }
    }

    public Participant getParticipantById(String id) {
        log.info("ParticipantService: getParticipantById: " + id);
        return participantRepository.findByParticipantId(id);
    }

    public Participant updateParticipant(Participant participant) throws Exception{
        log.info("ParticipantService: updateParticipant: " + participant.toString());

        if(participant.getParticipantId() == null || participant.getParticipantId().isEmpty()) {
            log.debug("ParticipantService: updateParticipant: Participant's id is empty!!!");
            throw new Exception("Update: Participant's id is empty!!!");
        }
        if(participant.getAddress().getAddressId() == null) {
            log.debug("ParticipantService: updateParticipant: Participant's address id is empty!!!");
            throw new Exception("Update: Participant's address id is empty!!!");
        }
        return participantRepository.save(participant);
    }

    public void deleteParticipant(Participant participant) throws Exception{
        log.info("ParticipantService: deleteParticipant: " + participant.toString());
        if(participant.getParticipantId().isEmpty()) {
            log.debug("ParticipantService: deleteParticipant: Participant Id is empty!");
            throw new Exception("Update: Participant Id is empty!");
        }

        participantRepository.delete(participant);
    }

    public Page<Participant> getAllParticipants(Integer page, Integer size) {
        Page pageOfParticipants = participantRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "participantId")));
        return pageOfParticipants;
    }
}
