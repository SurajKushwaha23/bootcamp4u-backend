package com.bootcamp4u.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public final class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    // Private constructor to prevent instantiation of utility class
    private SlugUtil() {}

    public static String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty to generate a slug");
        }

        // 1. Replace all whitespace with hyphens
        String noWhitespace = WHITESPACE.matcher(input.trim()).replaceAll("-");

        // 2. Normalize characters (e.g., convert "é" to "e")
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);

        // 3. Remove all non-alphanumeric characters (except hyphens)
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        // 4. Convert to lowercase and replace multiple consecutive hyphens with a single one
        return slug.toLowerCase(Locale.ENGLISH)
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", ""); // Remove leading or trailing hyphens
    }
}