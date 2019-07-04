package com.cloudcoding.DataClasses;

import org.joda.time.DateTime;

/**
 * ====================================
 * Created by michael.carr on 13/05/2014.
 * ====================================
 */
public class DateTimeHolder {

    private DateTime dateTime;

    public DateTimeHolder(DateTime dateTime){
        this.dateTime = dateTime;
    }

    public void setDateTime(DateTime dateTime){
        this.dateTime = dateTime;
    }

    public DateTime getDateTime(){
        return dateTime;
    }

}