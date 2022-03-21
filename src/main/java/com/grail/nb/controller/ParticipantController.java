package com.grail.nb.controller;

import com.grail.nb.model.Participant;
import com.grail.nb.service.ParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/participant")
@Api(tags = {"participants"})
public class ParticipantController {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantController.class);
    protected static final String  DEFAULT_PAGE_SIZE = "10";
    protected static final String DEFAULT_PAGE_NUM = "0";

    @Autowired
    private ParticipantService participantService;

    @PostMapping("")
    @ApiOperation(value = "Create a participant with personal information.", notes = "Returns new participant with participant id and address id.")
    public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant,
                                            HttpServletRequest request, HttpServletResponse response) {
        try {
            Participant createdParticipant = this.participantService.createParticipant(participant);
            return new ResponseEntity<>(createdParticipant, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Retrieve a participant with personal information.", notes = "Need to provide participant id as a path param.")
    public ResponseEntity<Participant> getParticipant(@PathVariable("id") String id) {
        try {
            Participant participant = this.participantService.getParticipantById(id);

            if (participant != null) {
                return new ResponseEntity<>(participant, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAll")
    @ApiOperation(value = "Retrieve list of all participants.", notes = "The output is paginated. Default Page number is 0 and page size is 10 participants.")
    Page<Participant> getAllParticipants(@RequestParam(value = "page", required = true, defaultValue = DEFAULT_PAGE_NUM) Integer page,
                                         @RequestParam(value = "size", required = true, defaultValue = DEFAULT_PAGE_SIZE) Integer size,
                                         HttpServletRequest request, HttpServletResponse response) {
        return this.participantService.getAllParticipants(page, size);
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Update participant personal information.", notes = "You need to provide participant id with new information.")
    public ResponseEntity <Participant> updateParticipant(@RequestBody Participant participant) {
        try {
            Participant foundParticipant = participantService.getParticipantById(participant.getParticipantId());
            if(foundParticipant == null){
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Participant updatedParticipant = participantService.updateParticipant(participant);
            return new ResponseEntity<>(updatedParticipant, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete existing participant.", notes = "You need to provide participant id.")
    public ResponseEntity <Map< String, Boolean >> deleteUser(@PathVariable("id") String id){
        try {
            Participant foundParticipant = participantService.getParticipantById(id);
            if(foundParticipant == null){
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            participantService.deleteParticipant(foundParticipant);
            Map < String, Boolean > response = new HashMap< >();
            response.put("deleted", Boolean.TRUE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
