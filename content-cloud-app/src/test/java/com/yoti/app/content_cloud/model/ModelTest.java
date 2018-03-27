package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
@Slf4j
public class ModelTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Validator validator;


    @Test
    public void testBinRequestSerialising() throws JsonProcessingException {
        BinRequest validBinRequest = RequestHelper.getBinRequest("1", "public", "cc");
        validateInput(validBinRequest);
        validateSerialization(validBinRequest);
    }

    @Test
    public void testBinRequestDeserializing() throws IOException {
        ObjectNode binRequestNode = mapper.createObjectNode();
        binRequestNode.put("recordId", "recordId");
        binRequestNode.put("requesterPublicKey", "publicKey");
        binRequestNode.put("cloudId", "cloudId");
        binRequestNode.put("dataGroup", "CONN");
        String jsonString = binRequestNode.toString();
        BinRequest binRequest = mapper.readValue(jsonString, BinRequest.class);
        Assert.assertThat(binRequest, Matchers.notNullValue());
        Assert.assertThat(binRequest.getRecordId(), Matchers.is("recordId"));
        Assert.assertThat(binRequest.getRequesterPublicKey(), Matchers.is("publicKey"));
        Assert.assertThat(binRequest.getCloudId(), Matchers.is("cloudId"));
        Assert.assertThat(binRequest.getDataGroup(), Matchers.is("CONN"));
    }

    @Test
    public void testInsertMessageRequestSerializing() throws JsonProcessingException {
        InsertMessageRequest insertMessageRequest = RequestHelper.getInsertMessagRequest("testData", "public-key", "cloudId", Arrays.asList("key1"), "encryptionId");
        validateInput(insertMessageRequest);
        validateSerialization(insertMessageRequest);
    }

    @Test
    public void testInsertMessageRequestDeserializing() throws IOException {
        ObjectNode binRequestNode = mapper.createObjectNode();
        binRequestNode.put("dataObj", "testData");
        binRequestNode.put("requesterPublicKey", "public-key");
        binRequestNode.put("cloudId", "cloudId");
        binRequestNode.put("dataGroup", "CONN");
        binRequestNode.put("encryptionKeyId", "encryptionId");
        ArrayNode tagNode = mapper.createArrayNode();
        tagNode.add("key1");
        binRequestNode.put("tag", tagNode);
        String jsonString = binRequestNode.toString();
        InsertMessageRequest insertMessageRequest = mapper.readValue(jsonString, InsertMessageRequest.class);
        Assert.assertThat(insertMessageRequest, Matchers.notNullValue());
        Assert.assertThat(insertMessageRequest.getDataObj(), Matchers.is("testData"));
        Assert.assertThat(insertMessageRequest.getRequesterPublicKey(), Matchers.is("public-key"));
        Assert.assertThat(insertMessageRequest.getCloudId(), Matchers.is("cloudId"));
        Assert.assertThat(insertMessageRequest.getDataGroup(), Matchers.is("CONN"));
        Assert.assertThat(insertMessageRequest.getEncryptionKeyId(), Matchers.is("encryptionId"));
        Assert.assertTrue(insertMessageRequest.getTag().contains("key1"));
    }

    @Test
    public void testRetrieveMessageRequestSerialization() throws JsonProcessingException {
        RetrieveMessageRequest retrieveMessageRequest = RequestHelper.getRetrieveMessageRequest("cloudId", "public-key", Arrays.asList("key1"),
                Date.from(Instant.now()), Date.from(Instant.now()), RetrieveProto.RetrieveRequest.SearchType.AND_TAGS_VALUE, true);
        validateInput(retrieveMessageRequest);
        validateSerialization(retrieveMessageRequest);
    }

    @Test
    public void testRetrieveMesasgeRequestDeserialization() throws IOException {
        Date currentDate = Date.from(Instant.now());
        ObjectNode binRequestNode = mapper.createObjectNode();
        binRequestNode.put("cloudId", "cloudId");
        binRequestNode.put("requesterPublicKey", "public-key");
        binRequestNode.put("startDate", currentDate.getTime());
        binRequestNode.put("endDate", currentDate.getTime());
        binRequestNode.put("dataGroup", "CONN");
        binRequestNode.put("searchType", RetrieveProto.RetrieveRequest.SearchType.OR_TAGS_VALUE);
        binRequestNode.put("retrieveBin", "true");
        ArrayNode tagNode = mapper.createArrayNode();
        tagNode.add("key1");
        binRequestNode.put("queryTags", tagNode);
        String jsonString = binRequestNode.toString();
        RetrieveMessageRequest retrieveMessageRequest = mapper.readValue(jsonString, RetrieveMessageRequest.class);
        Assert.assertThat(retrieveMessageRequest, Matchers.notNullValue());
        Assert.assertThat(retrieveMessageRequest.getSearchType(), Matchers.is(RetrieveProto.RetrieveRequest.SearchType.OR_TAGS_VALUE));
        Assert.assertThat(retrieveMessageRequest.getRequesterPublicKey(), Matchers.is("public-key"));
        Assert.assertThat(retrieveMessageRequest.getCloudId(), Matchers.is("cloudId"));
        Assert.assertThat(retrieveMessageRequest.getDataGroup(), Matchers.is("CONN"));
        Assert.assertThat(retrieveMessageRequest.getRetrieveBin(), Matchers.is(true));
        Assert.assertTrue(retrieveMessageRequest.getQueryTags().contains("key1"));
    }

    private <T> void validateInput(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        Assert.assertThat(violations, Matchers.is(IsEmptyCollection.empty()));
    }

    private <T> void validateSerialization(final T obj) throws JsonProcessingException {
        String jsonString = mapper.writeValueAsString(obj);
        Assert.assertThat(jsonString, Matchers.notNullValue());
    }
}
