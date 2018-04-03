package com.yoti.app.content_cloud.httpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yoti.app.config.EndpointsProperties;
import com.yoti.app.content_cloud.service.PostDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("dev")
public class RestTemplateTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    PostDataService service;

    @Autowired
    EndpointsProperties properties;

    @Test
    public void loadContexts() {
        Assert.assertNotNull(restTemplate);
        Assert.assertNotNull(mapper);
        Assert.assertNotNull(service);

    }

    @Test
    public void testPostData() {
        ObjectNode node = mapper.createObjectNode();
        node.put("name", "justin");
        log.info(node.toString());
        //service.postData(properties.getRetrieveData(), node.toString());
    }
}
