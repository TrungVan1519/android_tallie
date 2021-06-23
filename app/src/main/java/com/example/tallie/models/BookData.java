package com.example.tallie.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookData {
    public static ArrayList<Book> allBooks() {
        return new ArrayList<>(Arrays.asList(
                new Book(1, "1", "1", 1.1, 1, "1", null),
                new Book(1, "1", "1", 1.1, 1, "1", null),
                new Book(1, "1", "1", 1.1, 1, "1", null),
                new Book(1, "1", "1", 1.1, 1, "1", null),
                new Book(1, "1", "1", 1.1, 1, "1", null)
        ));
    }

    public static ArrayList<Book> favBooks() {
        return new ArrayList<>(Arrays.asList(
                new Book(2, "2", "2", 2.2, 2, "2", null),
                new Book(2, "2", "2", 2.2, 2, "2", null)
        ));
    }

    public static ArrayList<Book> seenBooks() {
        return new ArrayList<>(Arrays.asList(
                new Book(3, "3", "3", 3.3, 3, "3", null),
                new Book(3, "3", "3", 3.3, 3, "3", null)
        ));
    }
}
