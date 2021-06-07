package com.example.tallie.models;

import com.example.tallie.R;

import java.util.Arrays;
import java.util.List;

public class BookData {
    public static List<Book> allBooks() {
        return Arrays.asList(
                new Book(R.drawable.ic_launcher_background, "Three musketeers", "So much fun", "Trung Van", 100_000d, 10),
                new Book(R.drawable.ic_baseline_favorite_24, "Snow white", "So much fun", "Trung Van", 100_000d, 10),
                new Book(R.drawable.ic_baseline_home_24, "Vampire", "So much fun", "Trung Van", 100_000d, 10),
                new Book(R.drawable.ic_baseline_notifications_none_24, "Twilight", "So much fun", "Trung Van", 100_000d, 10),
                new Book(R.drawable.ic_baseline_remove_red_eye_24, "Marxist and economic policy", "So much fun", "Trung Van", 100_000d, 10),
                new Book(R.drawable.ic_baseline_search_24, "Old men and the gold fish", "So much fun", "Trung Van", 100_000d, 10),
                new Book(R.drawable.ic_baseline_shopping_cart_24, "Game of thrones", "So much fun", "Trung Van", 100_000d, 10)
        );
    }
}
