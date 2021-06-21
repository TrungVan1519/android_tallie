package com.example.tallie.models;

import com.example.tallie.R;

import java.util.Arrays;
import java.util.List;

public class BookData {
    public static List<Book> allBooks() {
        return Arrays.asList(
                new Book(1, "1", "1", 1.1, 1, "1", R.drawable.ic_launcher_background),
                new Book(1, "1", "1", 1.1, 1, "1", R.drawable.ic_launcher_background),
                new Book(1, "1", "1", 1.1, 1, "1", R.drawable.ic_launcher_background),
                new Book(1, "1", "1", 1.1, 1, "1", R.drawable.ic_launcher_background),
                new Book(1, "1", "1", 1.1, 1, "1", R.drawable.ic_launcher_background)
        );
    }

    public static List<Book> favBooks() {
        return Arrays.asList(
                new Book(2, "2", "2", 2.2, 2, "2", R.drawable.ic_launcher_background),
                new Book(2, "2", "2", 2.2, 2, "2", R.drawable.ic_launcher_background)
        );
    }

    public static List<Book> seenBooks() {
        return Arrays.asList(
                new Book(3, "3", "3", 3.3, 3, "3", R.drawable.ic_launcher_background),
                new Book(3, "3", "3", 3.3, 3, "3", R.drawable.ic_launcher_background)
        );
    }
}
