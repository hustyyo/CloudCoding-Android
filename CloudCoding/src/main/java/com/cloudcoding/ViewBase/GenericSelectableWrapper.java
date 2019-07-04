package com.cloudcoding.ViewBase;

/**
 * ==================================
 * Created by michael.carr on 8/07/2014.
 * ==================================
 */
public class GenericSelectableWrapper<T> {

    private T object;
    private boolean selected;

    public GenericSelectableWrapper(T object, boolean selected){
        this.object = object;
        this.selected = selected;
    }

    public GenericSelectableWrapper(T object){
        this(object, false);
    }

    public void setSelected(boolean isSelected){
        selected = isSelected;
    }

    public boolean isSelected(){
        return selected;
    }

    public T getObject(){
        return object;
    }

}
