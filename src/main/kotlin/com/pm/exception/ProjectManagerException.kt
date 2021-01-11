package com.pm.exception

import org.springframework.http.HttpStatus

open class ProjectManagerException(message: String, val httpStatus: HttpStatus) : RuntimeException(message)