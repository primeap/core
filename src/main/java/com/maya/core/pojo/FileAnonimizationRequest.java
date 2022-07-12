package com.maya.core.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class FileAnonimizationRequest extends AnonimizationRequest{
    private MultipartFile requestedFile;
    private List<String>columnHeaderName;


}
