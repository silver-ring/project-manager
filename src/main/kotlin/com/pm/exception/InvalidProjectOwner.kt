package com.pm.exception

import org.springframework.http.HttpStatus

class InvalidProjectOwner : ProjectManagerException("Owner should have a manager role", HttpStatus.BAD_REQUEST)
