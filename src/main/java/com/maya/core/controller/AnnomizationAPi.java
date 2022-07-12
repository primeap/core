package com.maya.core.controller;

import com.maya.core.pojo.FileAnonimizationRequest;
import com.maya.core.service.AnnomizationService;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class AnnomizationAPi {

    private static Logger logger = LoggerFactory.getLogger(AnnomizationAPi.class);

    @Autowired
    AnnomizationService service;
    @RequestMapping(path = "/")
    public String sayHello(){
        return "Hell0";
    }

    @PostMapping("/")
    public ResponseEntity<?> multiUploadFileModel(@ModelAttribute FileAnonimizationRequest model) throws Exception{
        logger.info(String.join("| ",model.getColumnHeaderName()));
        MultipartFile multipartFile = model.getRequestedFile();
        File tempFile = File.createTempFile("tmp"+System.currentTimeMillis(), ".csv");
        try (OutputStream os = new FileOutputStream(tempFile)) {
            os.write(multipartFile.getBytes());
            os.flush();
        }


        List<String> columnNames = model.getColumnHeaderName();

       List<String[]> annonamizedData = service.annomizeFileByColumnName(tempFile,columnNames);

        byte[] array = writeLineByLine(annonamizedData);


        ByteArrayResource resource = new ByteArrayResource(array);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(multipartFile.getName()+"-Annomized.csv")
                                .build().toString())
                .body(resource);

    }

    private  byte[] writeLineByLine(List<String[]> lines) throws Exception {
        File tempFile = File.createTempFile("result-tmp-" + System.currentTimeMillis(), ".csv");
        try (CSVWriter writer = new CSVWriter(new FileWriter(tempFile))) {
            for (String[] line : lines) {
                writer.writeNext(line);
            }
        }
        return readFileToByteArr(tempFile);
    }
    private static byte[] readFileToByteArr(File file)
            throws IOException
    {

        // Creating an object of FileInputStream to
        // read from a file
        FileInputStream fl = new FileInputStream(file);

        // Now creating byte array of same length as file
        byte[] arr = new byte[(int)file.length()];

        // Reading file content to byte array
        // using standard read() method
        fl.read(arr);

        // lastly closing an instance of file input stream
        // to avoid memory leakage
        fl.close();

        // Returning above byte array
        return arr;
    }

}
