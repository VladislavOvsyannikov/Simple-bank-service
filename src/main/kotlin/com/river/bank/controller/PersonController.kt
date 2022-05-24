package com.river.bank.controller

import com.river.bank.dto.request.PersonRequestDto
import com.river.bank.service.PersonService
import com.river.bank.support.swagger.PageableApi
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Person")
@RequestMapping("/persons")
class PersonController(private val personService: PersonService) {

    @GetMapping
    @PageableApi
    @Operation(summary = "Return persons with pagination")
    fun find(@Parameter(hidden = true) pageable: Pageable) = personService.find(pageable)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new person")
    fun create(@RequestBody @Validated request: PersonRequestDto) = personService.create(request)

}
