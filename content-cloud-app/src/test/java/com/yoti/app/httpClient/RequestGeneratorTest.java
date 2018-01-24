package com.yoti.app.httpClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoti.app.domain.Person;
import com.yoti.app.httpClient.impl.RequestClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class RequestGeneratorTest {

    private RequestClientImpl requestClientImpl;
    private String url = null;
    private ObjectMapper mapper;

    @Before
    public void init(){
        requestClientImpl = new RequestClientImpl();
        url = "https://localhost:8087/";
        mapper = new ObjectMapper();
    }


    @Test
    public void testHttpsClientWithGet() throws IOException {
        CloseableHttpClient httpClient = requestClientImpl.getCloudContentInvoker();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        Assert.assertThat(httpResponse.getStatusLine().getStatusCode(), CoreMatchers.equalTo(200));
        String responseText = getContentFromResponse(httpResponse);
        log.info(responseText);
        Person person = mapper.readValue(responseText,Person.class);
        Assert.assertNotNull(person);
    }

    private String getContentFromResponse(final HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        Assert.assertNotNull(inputStream);
        return IOUtils.toString(inputStream);
    }

    @Test
    public void testHttpsClientPostData() throws IOException {
        CloseableHttpClient httpClient = requestClientImpl.getCloudContentInvoker();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity("Justin Boss");
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        Assert.assertThat(httpResponse.getStatusLine().getStatusCode(),CoreMatchers.equalTo(200));
        String responseText = getContentFromResponse(httpResponse);
        Assert.assertThat(responseText,CoreMatchers.is("post successfull!!!"));
    }
}
