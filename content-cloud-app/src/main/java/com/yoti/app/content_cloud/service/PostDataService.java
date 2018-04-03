package com.yoti.app.content_cloud.service;

import com.yoti.app.content_cloud.model.PostDataModel;
import org.springframework.http.ResponseEntity;


public interface PostDataService {

    ResponseEntity<?> postData(final PostDataModel postDataModel);

}
