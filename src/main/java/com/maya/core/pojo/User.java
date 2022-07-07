package com.maya.core.pojo;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Getter
    @Setter
    private String userId;
    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    @Getter
    @Setter
    private String email;
}
