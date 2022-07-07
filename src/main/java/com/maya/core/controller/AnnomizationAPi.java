package com.maya.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class AnnomizationAPi {

    @RequestMapping(path = "/")
    public String sayHello(){
        return "Hell0";
    }

}
