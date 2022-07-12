package com.maya.core.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UtilService {
    private static String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static String numberRegex = "^\\d*\\.\\d+|\\d+\\.\\d*$";


    public static boolean isEmail(String email){
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isNumber(String data){
        Pattern pattern = Pattern.compile(numberRegex);
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }
    public static boolean isDate(String data){
        List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>();

        dateFormats.add(new SimpleDateFormat("M/dd/yyyy"));
        dateFormats.add(new SimpleDateFormat("dd.M.yyyy"));
        dateFormats.add(new SimpleDateFormat("M/dd/yyyy hh:mm:ss"));
        dateFormats.add(new SimpleDateFormat("dd.M.yyyy hh:mm:ss"));
        dateFormats.add(new SimpleDateFormat("dd-M-yyyy hh:mm:ss"));
        dateFormats.add(new SimpleDateFormat("M-dd-yyyy hh:mm:ss"));
        dateFormats.add(new SimpleDateFormat("yyyy-M-dd hh:mm:ss"));
        dateFormats.add(new SimpleDateFormat("dd.MMM.yyyy"));
        dateFormats.add(new SimpleDateFormat("dd-MMM-yyyy"));
        dateFormats.add(new SimpleDateFormat("dd-MM-yyyy"));
        dateFormats.add(new SimpleDateFormat("dd-M-yyyy"));
        dateFormats.add(new SimpleDateFormat("M/dd"));
        dateFormats.add(new SimpleDateFormat("M dd"));
        dateFormats.add(new SimpleDateFormat("M y"));
        for (SimpleDateFormat format : dateFormats)
        {
            try
            {
                format.setLenient(false);
                Date date = format.parse(dateString);
                return true;
            }
            catch (Exception e)
            {

            }
        }
        return false;
    }


}
