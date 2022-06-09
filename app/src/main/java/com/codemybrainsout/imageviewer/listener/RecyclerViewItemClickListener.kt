package com.codemybrainsout.imageviewer.listener

import com.codemybrainsout.imageviewer.model.BaseModel
import com.codemybrainsout.imageviewer.model.User

/**
 * Created by ahulr on 10-06-2017.
 */
interface RecyclerViewItemClickListener {
    fun onItemClick(baseModel: BaseModel?)
    fun onUserClick(user: User?)
}