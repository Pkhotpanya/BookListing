package com.example.android.booklisting;

import java.util.List;

/**
 * Created by pkhotpanya on 6/23/17.
 */

public class Book {

    private String bookTitle;
    private List<String> bookAuthors;

    public Book(String title, List<String> authors) {
        bookTitle = title;
        bookAuthors = authors;
    }

    public String getTitle() {
        return bookTitle;
    }

    public List<String> getAuthors() {
        return bookAuthors;
    }
}
