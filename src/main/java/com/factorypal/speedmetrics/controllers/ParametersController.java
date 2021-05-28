package com.factorypal.speedmetrics.controllers;

import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.schemas.ParametersRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ParametersController {

    @GetMapping("/parameters")
    public List<Machine> getParameters() {
        return List.of(
                new Machine("alas","Machine Alas"),
                new Machine("dfdag","Machine Dfdag")
        );
    }

    @PostMapping("/parameters")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ParametersRequest addParameters(@RequestBody ParametersRequest parameters) {
        return parameters;
    }
}
