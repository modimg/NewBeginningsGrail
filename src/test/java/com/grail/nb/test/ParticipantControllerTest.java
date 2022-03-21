package com.grail.nb.test;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.grail.nb.NewBeginnings;
import com.grail.nb.controller.ParticipantController;
import com.grail.nb.model.Participant;
import com.grail.nb.model.Address;

import org.json.JSONObject;
import org.junit.runner.RunWith;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = NewBeginnings.class)
@ActiveProfiles("test")
public class ParticipantControllerTest {

    @InjectMocks
    ParticipantController controller;

    @Autowired
    WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testCreateRetrieveDelete() throws Exception {
        Participant per = getParticipant("testCreateRetrieveDelete");
        byte[] perJson = toJson(per);

        MvcResult result = mvc.perform(post("/api/v1/participant")
                        .content(perJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String participantId = getParticipantIdFromResponse(result.getResponse().getContentAsString());


        mvc.perform(get("/api/v1/participant/" + participantId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantId", is(participantId)))
                .andExpect(jsonPath("$.fname", is(per.getFname())))
                .andExpect(jsonPath("$.lname", is(per.getLname())))
                .andExpect(jsonPath("$.mname", is(per.getMname())))
                .andExpect(jsonPath("$.dob", is(per.getDob().toString())))
                .andExpect(jsonPath("$.cellphoneno", is(per.getCellphoneno())))
                .andExpect(jsonPath("$.homephoneno", is(per.getHomephoneno())))
                .andExpect(jsonPath("$.workphoneno", is(per.getWorkphoneno())));

        mvc.perform(delete("/api/v1/participant/" + participantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".deleted", hasItem(true)));

        mvc.perform(get("/api/v1/participant/" + participantId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUpdateDelete() throws Exception {
        Participant per = getParticipant("testCreateUpdateDelete");
        byte[] perJson = toJson(per);


        MvcResult result = mvc.perform(post("/api/v1/participant")
                        .content(perJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String participantId = getParticipantIdFromResponse(result.getResponse().getContentAsString());
        Long addressId = getAddressIdFromResponse(result.getResponse().getContentAsString());
        per.setParticipantId(participantId);
        per.getAddress().setAddressId(addressId);

        per.setFname("FNameChanged");
        per.setLname("LNameChanged");
        per.setCellphoneno("908-765-4321");


        byte[] newPerJson = toJson(per);

        MvcResult updatedResult = mvc.perform(put("/api/v1/participant/" + participantId)
                        .content(newPerJson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        mvc.perform(get("/api/v1/participant/" + participantId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantId", is(participantId)))
                .andExpect(jsonPath("$.fname", is(per.getFname())))
                .andExpect(jsonPath("$.lname", is(per.getLname())))
                .andExpect(jsonPath("$.mname", is(per.getMname())))
                .andExpect(jsonPath("$.dob", is(per.getDob().toString())))
                .andExpect(jsonPath("$.cellphoneno", is(per.getCellphoneno())));

        mvc.perform(delete("/api/v1/participant/" + participantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".deleted", hasItem(true)));

    }

    private String getParticipantIdFromResponse(String jsonResponse) {
        try {
            JSONObject obj = new JSONObject(jsonResponse);
            return obj.getString("participantId");
        }catch (Exception e){
            return "";
        }
    }

    private Long getAddressIdFromResponse(String jsonResponse) {
        try {
            JSONObject obj = new JSONObject(jsonResponse).getJSONObject("address");
            return obj.getLong("addressId");
        }catch (Exception e){
            return null;
        }
    }


    private Participant getParticipant(String prefix) {
        Participant per = new Participant();
        Address addr = new Address();

        per.setFname(prefix + "_fname");
        per.setMname(prefix + "_mname");
        per.setLname(prefix + "_lname");
        per.setDob(LocalDate.now());
        per.setCellphoneno("123-456-7890");

        addr.setAddrLine1(prefix + "_addrline1");
        addr.setAddrLine2(prefix + "_addrline2");
        addr.setCity(prefix + "_city");
        addr.setState(prefix + "_state");
        addr.setCountry("USA");
        addr.setZipCode(prefix + "_zip");

        per.setAddress(addr);

        return per;
    }

    private byte[] toJson(Object r) throws Exception {
        ObjectMapper map = new ObjectMapper();
        map.registerModule(new JavaTimeModule());
        return map.writeValueAsString(r).getBytes();
    }

}
