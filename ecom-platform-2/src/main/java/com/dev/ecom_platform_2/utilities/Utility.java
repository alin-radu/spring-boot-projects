package com.dev.ecom_platform_2.utilities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Utility {

    public static Pageable createPageableWithValidation(
            Class<?> clazz, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {

        if (!isValidField(clazz, sortBy)) {
            sortBy = "id";
        }

        var sort = sortDirection.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private static boolean isValidField(Class<?> clazz, String fieldName) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .anyMatch(name -> name.equals(fieldName));
    }
}