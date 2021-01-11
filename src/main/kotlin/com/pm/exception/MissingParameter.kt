package com.pm.exception

import org.springframework.http.HttpStatus

abstract class MissingParameter(parameterName: String) : ProjectManagerException("$parameterName is missing", HttpStatus.BAD_REQUEST)
class MissingOwnerId : MissingParameter("ownerId")
class MissingProjectName : MissingParameter("projectName")