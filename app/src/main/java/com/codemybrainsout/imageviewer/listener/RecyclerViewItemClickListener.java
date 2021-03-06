package com.codemybrainsout.imageviewer.listener;

import com.codemybrainsout.imageviewer.model.BaseModel;
import com.codemybrainsout.imageviewer.model.Collection;
import com.codemybrainsout.imageviewer.model.Photo;
import com.codemybrainsout.imageviewer.model.User;

/**
 * Created by ahulr on 10-06-2017.
 */

public interface RecyclerViewItemClickListener {

    void onItemClick(BaseModel baseModel);

    void onUserClick(User user);
}
