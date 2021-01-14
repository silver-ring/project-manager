package com.pm.exception

import org.springframework.http.HttpStatus

class ProjectNotFound: ProjectManagerException("Project not found", HttpStatus.NOT_FOUND)