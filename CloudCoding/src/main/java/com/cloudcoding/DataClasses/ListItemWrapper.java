package com.cloudcoding.DataClasses;

import java.io.Serializable;

/**
 * ====================================
 * Created by michael.carr on 19/03/14.
 * ====================================
 */
public class ListItemWrapper implements Serializable {

    public Object dataObject;

    private boolean selected;
    String name="";

    public ListItemWrapper(String s, Object item){

        name = s;

        dataObject = item;

        selected = false;
    }


    public boolean isSelected(){
        return selected;
    }

    public void setSelected(){
        selected = true;
    }

    public void removeSelection(){
        selected = false;
    }

    @Override
    public String toString() {
        return name;
    }

    public static class ItemNameConverter {

        public String getDesc(Object object) {
            return "";
        }

        public String getString(Object object) {
            return "";
        }
    }
}
