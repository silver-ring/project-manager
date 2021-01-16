package com.pm.exception

import org.springframework.http.HttpStatus

class ProjectNameDuplicated : ProjectManagerException("Project name duplicated", HttpStatus.BAD_REQUEST)