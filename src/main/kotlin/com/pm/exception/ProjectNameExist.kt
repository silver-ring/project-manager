package com.pm.exception

import org.springframework.http.HttpStatus

class ProjectNameExist : ProjectManagerException("Project name is already exist", HttpStatus.BAD_REQUEST)