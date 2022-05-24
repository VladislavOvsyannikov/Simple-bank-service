package com.river.bank.support.swagger

import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Target(AnnotationTarget.FUNCTION)
@Parameter(
    name = "page",
    schema = Schema(type = "integer", defaultValue = "0"),
    `in` = ParameterIn.QUERY,
    description = "Zero-based page index (0..N)"
)
@Parameter(
    name = "size",
    schema = Schema(type = "integer", defaultValue = "20"),
    `in` = ParameterIn.QUERY,
    description = "Number of records per page"
)
@Parameter(
    name = "sort",
    array = ArraySchema(schema = Schema(type = "string")),
    `in` = ParameterIn.QUERY,
    description = "Sorting criteria in the format: property(,asc|desc). " +
            "Default sort order is ascending. Multiple sort criteria are supported"
)
annotation class PageableApi
