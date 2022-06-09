package com.codemybrainsout.imageviewer.model

/**
 * Created by ahulr on 10-06-2017.
 */
class Footer : BaseModel() {
    var type: Type? = null

    enum class Type {
        Collection, Photo
    }
}