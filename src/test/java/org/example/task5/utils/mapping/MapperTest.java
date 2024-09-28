package org.example.task5.utils.mapping;

import org.example.task5.dto.category.CategoryCreateDto;
import org.example.task5.dto.category.CategoryUpdateDto;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.model.Category;
import org.example.task5.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MapperTest {

    Category category;
    CategoryCreateDto categoryCreateDto;
    CategoryUpdateDto categoryUpdateDto;

    Location location;
    LocationCreateDto locationCreateDto;
    LocationUpdateDto locationUpdateDto;

    @BeforeEach
    void setUp() {
        category = new Category(1, "category-1", "Category 1");
        categoryCreateDto = new CategoryCreateDto("category-1", "Category 1");
        categoryUpdateDto = new CategoryUpdateDto("category-1-updated", "Category 1 Updated");

        location = new Location("location-1", "Location 1");
        locationCreateDto = new LocationCreateDto("location-1", "Location 1");
        locationUpdateDto = new LocationUpdateDto("location-1-updated", "Location 1 Updated");
    }

    @Test
    void toCategory_fromCreateDto_shouldReturnMappedCategory() {
        // Arrange
        // Act
        Category mappedCategory = Mapper.toCategory(1, categoryCreateDto);
        // Assert
        assertEquals(1, mappedCategory.id());
        assertEquals("category-1", mappedCategory.slug());
        assertEquals("Category 1", mappedCategory.name());
    }

    @Test
    void toCategory_fromUpdateDto_shouldReturnMappedCategory() {
        // Arrange
        // Act
        Category mappedCategory = Mapper.toCategory(1, categoryUpdateDto);
        // Assert
        assertEquals(1, mappedCategory.id());
        assertEquals("category-1-updated", mappedCategory.slug());
        assertEquals("Category 1 Updated", mappedCategory.name());
    }

    @Test
    void toCategoryCreateDto_fromCategory_shouldReturnMappedCategory() {
        // Arrange
        // Act
        CategoryCreateDto mappedCategoryCreateDto = Mapper.toCategoryCreateDto(category);
        // Assert
        assertEquals("category-1", mappedCategoryCreateDto.slug());
        assertEquals("Category 1", mappedCategoryCreateDto.name());
    }

    @Test
    void toCategoryUpdateDto_fromCategory_shouldReturnMappedCategory() {
        // Arrange
        // Act
        CategoryUpdateDto mappedCategoryUpdateDto = Mapper.toCategoryUpdateDto(category);
        // Assert
        assertEquals("category-1", mappedCategoryUpdateDto.slug());
        assertEquals("Category 1", mappedCategoryUpdateDto.name());
    }

    @Test
    void toLocation_fromCreateDto_shouldReturnMappedLocation() {
        // Arrange
        // Act
        Location mappedLocation = Mapper.toLocation(locationCreateDto);
        // Assert
        assertEquals("location-1", mappedLocation.slug());
        assertEquals("Location 1", mappedLocation.name());
    }

    @Test
    void testToLocation_fromUpdateDto_shouldReturnMappedLocation() {
        // Arrange
        // Act
        Location mappedLocation = Mapper.toLocation(locationUpdateDto);
        // Assert
        assertEquals("location-1-updated", mappedLocation.slug());
        assertEquals("Location 1 Updated", mappedLocation.name());
    }

    @Test
    void toLocationCreateDto_fromLocation_shouldReturnMappedLocation() {
        // Arrange
        // Act
        LocationCreateDto mappedLocationCreateDto = Mapper.toLocationCreateDto(location);
        // Assert
        assertEquals("location-1", mappedLocationCreateDto.slug());
        assertEquals("Location 1", mappedLocationCreateDto.name());
    }

    @Test
    void toLocationUpdateDto_fromLocation_shouldReturnMappedLocation() {
        // Arrange
        // Act
        LocationUpdateDto mappedLocationUpdateDto = Mapper.toLocationUpdateDto(location);
        // Assert
        assertEquals("location-1", mappedLocationUpdateDto.slug());
        assertEquals("Location 1", mappedLocationUpdateDto.name());
    }
}