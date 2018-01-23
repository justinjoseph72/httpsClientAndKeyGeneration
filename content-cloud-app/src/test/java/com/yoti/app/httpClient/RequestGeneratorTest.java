package com.yoti.app.httpClient;

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

public class RequestGeneratorTest {

    private RequestGenereator requestGenereator;
    private String url = null;

    @Before
    public void init(){
        requestGenereator = new RequestGenereator();
        url = "https://localhost:8087/";
    }


    @Test
    public void testHttpsClientWithGet() throws IOException {
        CloseableHttpClient httpClient = requestGenereator.httpsClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        Assert.assertThat(httpResponse.getStatusLine().getStatusCode(), CoreMatchers.equalTo(200));
        String responseText = getContentFromResponse(httpResponse);
        Assert.assertThat(responseText,CoreMatchers.is("not a post"));
    }

    private String getContentFromResponse(final HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        Assert.assertNotNull(inputStream);
        return IOUtils.toString(inputStream);
    }

    @Test
    public void testHttpsClientPostData() throws IOException {
        CloseableHttpClient httpClient = requestGenereator.httpsClient();
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity("Justin Boss");
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        Assert.assertThat(httpResponse.getStatusLine().getStatusCode(),CoreMatchers.equalTo(200));
        String responseText = getContentFromResponse(httpResponse);
        Assert.assertThat(responseText,CoreMatchers.is("post successfull!!!"));
    }
}
