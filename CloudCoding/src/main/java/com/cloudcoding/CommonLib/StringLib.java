package com.cloudcoding.CommonLib;

/**
 * Created by steveyang on 9/23/15.
 */
public class StringLib {

    public static String removeChar(String s, String c ){
        String rs = s.replace(c,"");
        return rs;
    }

    public static String[] splitBySpace(String s){
        return s.split("\\s+");
    }


    public static int compareString(String s1, String s2){

        String ss1 = removeChar(s1," ");
        String ss2 = removeChar(s2," ");

        ss1 = removeChar(ss1,"-");
        ss2 = removeChar(ss2,"-");


        if(ss1.length()==0 && ss2.length()>0){
            return 1;
        }

        if(ss1.length()>0 && ss2.length()==0){
            return -1;
        }

        return ss1.toLowerCase().compareTo(ss2.toLowerCase());
    }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
