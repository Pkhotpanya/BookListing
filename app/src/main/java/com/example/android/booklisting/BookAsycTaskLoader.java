package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class BookAsycTaskLoader extends AsyncTaskLoader<List<Book>> {

    String mUrl;

    public BookAsycTaskLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {
        return QueryUtils.extractBooks(mUrl);
    }

}
