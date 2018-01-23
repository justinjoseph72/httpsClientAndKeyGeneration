package com.yoti.app.content_Cloud.model;

import com.yoti.app.content_Cloud.annotations.CloudBody;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@CloudBody
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class Person {

    private long id;
    private String givenName;
    private String lastName;
    private String email;


}
