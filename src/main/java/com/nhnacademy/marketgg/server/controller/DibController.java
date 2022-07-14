package com.nhnacademy.marketgg.server.controller;

import com.nhnacademy.marketgg.server.dto.request.DibCreateRequest;
import com.nhnacademy.marketgg.server.dto.request.DibDeleteRequest;
import com.nhnacademy.marketgg.server.dto.response.DibRetrieveResponse;
import com.nhnacademy.marketgg.server.service.DibService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/shop/v1/dibs")
@RequiredArgsConstructor
public class DibController {

    private final DibService dibService;

    @PostMapping
    ResponseEntity<Void> createDib(@RequestBody final DibCreateRequest dibCreateRequest) {
        dibService.createDib(dibCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .location(URI.create("/shop/v1/dibs"))
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }

    @GetMapping
    ResponseEntity<List<DibRetrieveResponse>> retrieveDibs(@PathVariable Long memberNo) {
        List<DibRetrieveResponse> dibResponses = dibService.retrieveDibs(memberNo);

        return ResponseEntity.status(HttpStatus.OK)
                             .location(URI.create("/shop/v1/dibs"))
                             .body(dibResponses);
    }

    @DeleteMapping
    ResponseEntity<Void> deleteDib(@RequestBody final DibDeleteRequest dibDeleteRequest) {
        dibService.deleteDib(dibDeleteRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .location(URI.create("/shop/v1/dibs"))
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

}
