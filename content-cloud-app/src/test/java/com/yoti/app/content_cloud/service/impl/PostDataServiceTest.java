package com.yoti.app.content_cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import com.yoti.app.config.EndpointsProperties;
import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.app.content_cloud.adpaters.InsertProtoAdapter;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.PostDataModel;
import com.yoti.app.content_cloud.service.PostDataService;
import com.yoti.ccloudpubapi_v1.InsertProto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("mock")
public class PostDataServiceTest {

    @Autowired
    private PostDataService postDataService;

    @Autowired
    private InsertProtoAdapter insertProtoAdapter;

    private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().preservingProtoFieldNames();

    @Autowired
    private EndpointsProperties endpointsProperties;

    @Test
    public void loadContexts(){
        Assert.assertNotNull(postDataService);
    }

    @Test
    public void shouldPostInsertRequestWithCorrectSignature() {
        final PostDataModel postDataModel = getPostDataModelForInsertRequest();
        postDataService.postData(postDataModel);
    }

    private PostDataModel getPostDataModelForInsertRequest(){
        return  PostDataModel.builder()
                .keyData(RequestHelper.getKeyData())
                .payload(getJsonPayload(getInsertMessageRequest()))
                .postUrl(endpointsProperties.getInsertData())
                .build();
    }

    private InsertMessageRequest getInsertMessageRequest() {
        return RequestHelper.getInsertMessagRequest("TEstStr111", "sfsdf", "ccc",
                Arrays.asList("key1", "key2"), "eee");
    }


    private  String getJsonPayload(InsertMessageRequest obj)  {
        try {
            InsertProto.InsertRequest request = insertProtoAdapter.getInsertProtoFromInsertMessageRequest(obj);
            return jsonPrinter.print(request);
        } catch (Exception e) {
            return null;
        }
    }
}
