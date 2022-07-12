package com.maya.core.service;

import com.maya.core.controller.AnnomizationAPi;
import com.maya.core.pojo.FileAnnomizationMetadat;
import com.maya.core.util.AnnomizationEnums;
import com.maya.core.util.UtilService;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.*;

@Service
public class AnnomizationService {
    private static Logger logger = LoggerFactory.getLogger(AnnomizationService.class);
    CSVReader reader = null;

    @Autowired
    UtilService utilService;

    public List<String[]> annomizeFileByColumnName(File csvFile, List<String> columnNames) throws Exception{
        FileReader filereader = new FileReader(csvFile);

        // create csvReader object and skip first Line
        CSVReader csvReader = new CSVReaderBuilder(filereader).build();
        List<String[]> allData = csvReader.readAll();
        String headerNames[] = allData.get(0);
        Map<Integer, FileAnnomizationMetadat>map = createMetaData(allData,columnNames);
        List<String[]> annonamizedData =  annomizeFile(allData,map);
        return annonamizedData;
    }
    private Map<Integer, FileAnnomizationMetadat> createMetaData(List<String[]> allData, List<String> columnName)throws Exception{

        String headers[] =  allData.get(0);
        Map<String,Integer> fileHeaderMap =  new HashMap();
        for( int k=0; k<headers.length; k++ ){
            fileHeaderMap.put(headers[k].trim(),k);
        }

        //List<String> headerList = Arrays.asList(headers);
        Map<Integer,FileAnnomizationMetadat>map =  new HashMap<>();
        String[] values = allData.get(1); //Fixed second row
        for( int i=0; i<columnName.size(); i++ ){
            String cName =  columnName.get(i);
            int index= fileHeaderMap.get(cName.trim());

            String value = values[index];
            String dataType = utilService.findDataType(value);
            String dateFormat = null;
            if( dataType.equals(AnnomizationEnums.AnnomizationMethod.DATE.name())){
                dateFormat = utilService.gateDatePattern(value);
            }
            FileAnnomizationMetadat metadat =  FileAnnomizationMetadat.builder()
                    .annomizationMethod(dataType)
                    .dateFormat(dateFormat)
                    .headerIndex(index).requireAnnomization(true).columnHeader(columnName.get(i)).build();

            logger.info("metadata"+metadat.toString());
            map.put(index,metadat);
        }
        return map;

    }

    public List<String[]> annomizeFile(List<String[]> allData,Map<Integer,FileAnnomizationMetadat> map){

        for( int i=1; i<allData.size(); i++ ){
            String row[] =  allData.get(i);
            for( int j=0; j<row.length; j++  ){
                if( map.containsKey(j)) {
                    FileAnnomizationMetadat metadata = map.get(j);
                    row[j] = utilService.annonamize(row[j], metadata);
                }
            }

        }
        return allData;

    }


}
