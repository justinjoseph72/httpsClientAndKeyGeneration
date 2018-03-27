package com.yoti.app.content_cloud.adapter;

import com.yoti.app.content_cloud.adpaters.BinOpsProtoAdapter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
@Slf4j
public class BinAdapterTest {

    @Autowired
    private BinOpsProtoAdapter binAdapter;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void loadContext() {
        Assert.assertNotNull(binAdapter);
    }

    @Test
    public void testNullBinRequestForEmptyBinRequestShouldFail() {
        expectedException.expect(NullPointerException.class);
        binAdapter.getEmptyBinRequestProto(null);
    }

    @Test
    public void testNullBinRequestForMoveToBinRequestShouldFail() {
        expectedException.expect(NullPointerException.class);
        binAdapter.getMoveToBinRequestProto(null);
    }

    @Test
    public void testNullBinRequestForRemovedBinnedRequestShouldFail() {
        expectedException.expect(NullPointerException.class);
        binAdapter.getRemoveBinnedRequestProto(null);
    }

    @Test
    public void testNullBinRequestRestoreFromBinRequestShouldFail() {
        expectedException.expect(NullPointerException.class);
        binAdapter.getRestoreFromBinRequestProto(null);
    }

    @Test
    public void testValidBinRequestForEmptyBinRequestShouldPass() {

    }
}
