package com.example.android.booklisting;

import java.util.List;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class Book {

    private String mTitle;
    private List<String> mAuthors;

    public Book(String title, List<String> authors) {
        mTitle = title;
        mAuthors = authors;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<String> getAuthors() {
        return mAuthors;
    }
}
