package com.maya.core.util;

import com.maya.core.pojo.FileAnnomizationMetadat;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UtilService {
    static String alphabets = "abcdefghijklmnopqrstuvwxyz";
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
                format.parse(data);
                return true;
            }
            catch (Exception e)
            {

            }
        }
        return false;
    }
    public static String gateDatePattern(String data){
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
                Date date = format.parse(data);
                return format.toPattern().toString();
            }
            catch (Exception e)
            {

            }
        }
        return null;
    }




    public static String findDataType(String data){
        if( isNumber( data) ){
            return AnnomizationEnums.AnnomizationMethod.NUMBER.name();
        }else if( isDate( data) ){
            return AnnomizationEnums.AnnomizationMethod.DATE.name();
        }else if( isEmail( data) ){
            return AnnomizationEnums.AnnomizationMethod.EMAIL.name();
        }else{
            return AnnomizationEnums.AnnomizationMethod.UNDEFINED.name();
        }
    }

    public static String annonamize(String data, FileAnnomizationMetadat metadata){

        if(metadata.getRequireAnnomization() ){
            if( AnnomizationEnums.AnnomizationMethod.DATE.name().equals  (  metadata.getAnnomizationMethod()) ){
               return  annonamizeDate(data,metadata);
            }else if( AnnomizationEnums.AnnomizationMethod.NUMBER.name().equals(  metadata.getAnnomizationMethod()) ){
                return  annonamizeNumber(data);
            }else if( AnnomizationEnums.AnnomizationMethod.EMAIL.name().equals(  metadata.getAnnomizationMethod()) ){
                return  annonamizeEmail(data);
            }else{
                return data;
            }
        }
        return data;
    }

    public static String annonamizeDate(String data, FileAnnomizationMetadat metadata){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(metadata.getDateFormat());
            Date dt = sdf.parse(data);
            Random random = new Random();
            int sum = random.nextInt(0, 60) - 30;
            Calendar c = Calendar.getInstance();
            c.setTime(dt); // Using today's date
            c.add(Calendar.DATE, sum); // Adding 5 days
            return sdf.format(c.getTime());
        }catch (Exception e){
            return "---ERROR---";
        }
    }
    public static String annonamizeNumber(String data){
        Random random = new Random();
        int sum = random.nextInt(0, 1111) -500;
        if( data.contains(".") ){
                Double d =  Double.valueOf(data.trim()) +sum;
                return d.toString();
        }else{
            Long l =  Long.valueOf(data)+sum;
            return l.toString();
        }
    }

    public static  String annonamizeEmail(String email){
        String[] emailArray = email.split("@");
        String randomemail = getRandomAlphaNumericString(emailArray[0].length())+"@"+emailArray[1];
        return randomemail;
    }



    private static String getRandomAlphaNumericString(int targetStringLength) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }


    public static  String annonamizeEmail(String data, int p){
        char dataSequence[] = data.toCharArray();
        Random random = new Random();
        String mail = data.split("@")[0];
        int sz =  mail.length()/3+1;
        int index =  0;
        while( sz > 0 ){
            index =  index+random.nextInt(0,2);
            char nc =  alphabets.charAt(random.nextInt(0,25));
            if( nc != dataSequence[index] ){
                dataSequence[index] = nc;
                sz--;
            }

        }
        return new String(dataSequence);

    }



}
