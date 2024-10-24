package org.example.task5.controller.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.task5.controller.CrudController;
import org.example.task5.dto.location.LocationCreateDto;
import org.example.task5.dto.location.LocationUpdateDto;
import org.example.task5.model.ApiLocation;
import org.example.task5.service.KudaGoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
public class LocationCrudController implements CrudController<String, ApiLocation, LocationCreateDto, LocationUpdateDto> {
    private final KudaGoService<String, ApiLocation, LocationCreateDto, LocationUpdateDto> locationService;

    @Override

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ApiLocation> getAll() {
        return locationService.getAll();
    }

    @Override

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiLocation getById(@PathVariable("id") String id) {
        return locationService.getById(id);
    }

    @Override

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiLocation create(@RequestBody LocationCreateDto location) {
        return locationService.create(location);
    }


    @Override

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiLocation update(@PathVariable("id") String id, @RequestBody LocationUpdateDto location) {
        return locationService.update(id, location);
    }

    @Override

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        locationService.delete(id);
    }
}
