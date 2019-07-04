package com.cloudcoding.DataClasses;

/**
 * Created by steveyang on 24/2/17.
 */

public class CompletedItemsListItem {

    public int numberCompleted;
    public int numberTotal;
    public String name;

    public CompletedItemsListItem(String name, int numberCompleted, int numberTotal){
        this.name = name;
        this.numberCompleted = numberCompleted;
        this.numberTotal = numberTotal;
    }
}