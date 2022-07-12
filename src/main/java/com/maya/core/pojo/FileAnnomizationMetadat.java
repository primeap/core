package com.maya.core.pojo;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileAnnomizationMetadat {
    private String columnHeader;
    private Integer headerIndex;
    private Boolean requireAnnomization=false;
    private String annomizationMethod;
    private String dateFormat;
}
