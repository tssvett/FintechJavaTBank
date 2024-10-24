package org.example.task5.utils.mapping;


import lombok.NoArgsConstructor;
import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.model.Category;
import org.example.task5.model.ApiLocation;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Mapper {
    public static Category toCategory(int id, CategoryCreateDto categoryCreateDto) {
        return new Category(id, categoryCreateDto.slug(), categoryCreateDto.name());
    }

    public static Category toCategory(int id, CategoryUpdateDto categoryUpdateDto) {
        return new Category(id, categoryUpdateDto.slug(), categoryUpdateDto.name());
    }

    public static CategoryCreateDto toCategoryCreateDto(Category category) {
        return new CategoryCreateDto(category.slug(), category.name());
    }

    public static CategoryUpdateDto toCategoryUpdateDto(Category category) {
        return new CategoryUpdateDto(category.slug(), category.name());
    }

    public static ApiLocation toLocation(LocationCreateDto locationCreateDto) {
        return new ApiLocation(locationCreateDto.slug(), locationCreateDto.name());
    }

    public static ApiLocation toLocation(LocationUpdateDto locationUpdateDto) {
        return new ApiLocation(locationUpdateDto.slug(), locationUpdateDto.name());
    }

    public static LocationCreateDto toLocationCreateDto(ApiLocation location) {
        return new LocationCreateDto(location.slug(), location.name());
    }

    public static LocationUpdateDto toLocationUpdateDto(ApiLocation location) {
        return new LocationUpdateDto(location.slug(), location.name());
    }
}
