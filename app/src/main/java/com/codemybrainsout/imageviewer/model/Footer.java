package com.codemybrainsout.imageviewer.model;

import java.lang.reflect.Type;

/**
 * Created by ahulr on 10-06-2017.
 */

public class Footer extends BaseModel{


    public Type type;

    public enum Type {
        Collection, Photo
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
