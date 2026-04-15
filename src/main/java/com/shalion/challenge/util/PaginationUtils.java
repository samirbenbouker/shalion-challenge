package com.shalion.challenge.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PaginationUtils {

    private PaginationUtils() {
    }

    public static Pageable toPageable(int page, int size, String sortExpression, String defaultField) {
        String sortValue = normalizeSort(sortExpression);
        String[] parts = sortValue.split(",");

        String field = parts[0].trim().isEmpty() ? defaultField : parts[0].trim();
        Sort.Direction direction = Sort.Direction.ASC;

        if (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim())) {
            direction = Sort.Direction.DESC;
        }

        return PageRequest.of(page, size, Sort.by(direction, field));
    }

    private static String normalizeSort(String rawSort) {
        if (rawSort == null || rawSort.trim().isEmpty()) {
            return "id,asc";
        }

        String sanitized = rawSort.trim()
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .replace("'", "");

        if (sanitized.isEmpty()) {
            return "id,asc";
        }

        return sanitized;
    }
}
