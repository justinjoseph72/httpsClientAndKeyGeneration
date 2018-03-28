package com.yoti.app.content_cloud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.yoti.app.UrlConstants.ApiConstants;
import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.controllers.model.ContentCloudModel;
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
@ActiveProfiles("mock")
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
    public void testInValidDataInsertDataEndpoint() {
        try {
            String jsonPayLoad = mapper.writeValueAsString(getInvalidInsertMessageRequestForStringInput());
            log.info("payload is {}", jsonPayLoad);
            mockMvc.perform(MockMvcRequestBuilders.post(getInviteUrl()).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonPayLoad))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        } catch (Exception e) {
            log.warn("{} {}", e.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void shouldReturnOkStatusForInsertingValidContentCloudDataHavingStringData() {
        try {
            String jsonPayLoad = mapper.writeValueAsString(getContentCloudModelForInsert());
            log.info("payload is {}", jsonPayLoad);
            mockMvc.perform(MockMvcRequestBuilders.post(gettestUrl()).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonPayLoad))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            log.warn("{} {}", e.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void shouldReturnOkStatusForInsertingValidContentCloudDataHavingPersonData(){
        try{
            String jsonPayload = mapper.writeValueAsString(getContentCloudModelForInsertWithPerson());
            log.info("payload is {}",jsonPayload);
            mockMvc.perform(MockMvcRequestBuilders.post(gettestUrl()).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonPayload))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }catch (Exception e){
            log.warn(" {} {}",e.getClass().getName(),e.getMessage());
        }
    }


    private ContentCloudModel<InsertMessageRequest> getContentCloudModelForInsert() {
        InsertMessageRequest<String> insertMessageRequest = getInsertMessageRequestForStringInput();
        ContentCloudModel model = ContentCloudModel.builder().data(insertMessageRequest).privateKey("private-key".getBytes()).build();
        return model;
    }

    private ContentCloudModel<InsertMessageRequest<Person>> getContentCloudModelForInsertWithPerson(){
        InsertMessageRequest<Person> insertMessageRequest = getInsertMessageRequestForPersonObject();
        ContentCloudModel model = ContentCloudModel.builder().data(insertMessageRequest).privateKey("private-key".getBytes()).build();
        return model;
    }


    private InsertMessageRequest<String> getInsertMessageRequestForStringInput() {
        return RequestHelper.getInsertMessagRequest("common-aes-key", "public-key", "cloudId", Arrays.asList("key1", "key2"), "eee");
    }

    private InsertMessageRequest<Person> getInsertMessageRequestForPersonObject() {
        Person person = new Person(1, "givenName", "lastName", "email@email.com");
        return RequestHelper.getInsertMessagRequest(person, "public-key", "cloudId", Arrays.asList("key1", "key2"), "eee");
    }

    private InsertMessageRequest<String> getInvalidInsertMessageRequestForStringInput() {
        return RequestHelper.getInsertMessagRequest("common-aes-key", "public-key", "cloudId", Arrays.asList("key1", "key2"), null);
    }

    private String getInviteUrl() {
        return ApiConstants.API_BASE.concat(ApiConstants.INSERT_RECORD);
    }

    private String gettestUrl() {
        return ApiConstants.API_BASE.concat(ApiConstants.NEW_PATH);
    }
}
