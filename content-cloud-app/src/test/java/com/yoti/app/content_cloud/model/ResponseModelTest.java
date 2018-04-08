package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.Clock;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
@Slf4j
public class ResponseModelTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private Clock clock;

    private String inputStr = "{\"recordId\":\"sss\",\"encryptedRecordData\":\"sss\",\"encryptionKeyId\":\"sss\",\"tag\":\"d\",\"recordTimestamp\":\"2018-04-08 05:17:44+0000\",\"binned\":true}";

    @Test
    public void shouldSerialize() throws JsonProcessingException {
        Date currentDate = Date.from(clock.instant());
        ResponseRecord responseRecord = ResponseRecord.builder()
                .encryptedRecordData("sss")
                .binned(true)
                .encryptionKeyId("sss")
                .recordId("sss")
                .tag("d")
                .recordTimestamp(currentDate)
                .build();
        String jsonStr = mapper.writeValueAsString(responseRecord);
        log.info(jsonStr);
    }

    @Test
    public void shouldDeserialize() throws IOException {
        ResponseRecord responseRecord = mapper.readValue(inputStr, ResponseRecord.class);
        Assert.assertNotNull(responseRecord);
    }


}
