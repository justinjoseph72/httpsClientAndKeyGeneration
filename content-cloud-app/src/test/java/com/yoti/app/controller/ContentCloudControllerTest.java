package com.yoti.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.yoti.app.UrlConstants.ApiConstants;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Slf4j
public class ContentCloudControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void testInsertStringDataEndpoint() {
        try {
            String jsonPayLoad = mapper.writeValueAsString(getInsertMessageRequestForStringInput());
            log.info("payload is {}", jsonPayLoad);
            mockMvc.perform(MockMvcRequestBuilders.post(getInviteUrl()).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonPayLoad))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
        }
    }

    @Test
    public void testInsertPersonDataEndpoint() {
        try {
            String jsonPayLoad = mapper.writeValueAsString(getInsertMessageRequestForPersonObject());
            log.info("payload is {}", jsonPayLoad);
            mockMvc.perform(MockMvcRequestBuilders.post(getInviteUrl()).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonPayLoad))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
        }
    }

    @Test
    public void testInValidDataInsertDataEndpoint() {
        try {
            String jsonPayLoad = mapper.writeValueAsString(getInvalidInsertMessageRequestForStringInput());
            log.info("payload is {}", jsonPayLoad);
            mockMvc.perform(MockMvcRequestBuilders.post(getInviteUrl()).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonPayLoad))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
        }
    }


    private InsertMessageRequest<String> getInsertMessageRequestForStringInput() {
        InsertMessageRequest insertMessageRequest = InsertMessageRequest.builder()
                .cloudId("cloudId")
                .tag(ImmutableList.copyOf(Arrays.asList("key1", "key2")))
                .encryptionKeyId("eee")
                .dataObj("common-aes-key")
                .dataGroup("Conn")
                .requesterPublicKey("public-key")
                .build();
        return insertMessageRequest;
    }

    private InsertMessageRequest<Person> getInsertMessageRequestForPersonObject() {
        Person person = new Person(1, "givenName", "lastName", "email@email.com");
        InsertMessageRequest insertMessageRequest = InsertMessageRequest.builder()
                .cloudId("cloudId")
                .tag(ImmutableList.copyOf(Arrays.asList("key1", "key2")))
                .encryptionKeyId("eee")
                .dataObj(person)
                .dataGroup("Conn")
                .requesterPublicKey("public-key")
                .build();
        return insertMessageRequest;
    }

    private InsertMessageRequest<String> getInvalidInsertMessageRequestForStringInput() {
        InsertMessageRequest insertMessageRequest = InsertMessageRequest.builder()
                .cloudId("cloudId")
                .tag(ImmutableList.copyOf(Arrays.asList("key1", "key2")))
                .encryptionKeyId(null)
                .dataObj("common-aes-key")
                .dataGroup("Conn")
                .requesterPublicKey("public-key")
                .build();
        return insertMessageRequest;
    }

    private String getInviteUrl() {
        return ApiConstants.API_BASE.concat(ApiConstants.INSERT_RECORD);
    }
}
