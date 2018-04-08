package com.yoti.app.content_cloud.adapter;

import com.yoti.app.content_cloud.adpaters.InsertProtoAdapter;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
@Slf4j
public class InsertDataAdapterTest {

    @Autowired
    private InsertProtoAdapter adapter;


    @Test
    public void shouldDeserializeTheResponseToInputMessageResponse(){
         String responseStr = "{\"recordId\":\"record15\"}";
        InsertMessageResponse insertMessageResponse = adapter.getInsertMessageResponseFromJson(responseStr);
        Assert.assertNotNull(insertMessageResponse);
        Assert.assertEquals("record15",insertMessageResponse.getRecordId());
    }
}
