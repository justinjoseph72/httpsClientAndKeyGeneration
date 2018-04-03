package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.content_cloud.service.PostDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class PostDataServiceTest {

    @Autowired
    private PostDataService postDataService;

    @Test
    public void loadcontexts(){
        Assert.assertNotNull(postDataService);
    }
}
