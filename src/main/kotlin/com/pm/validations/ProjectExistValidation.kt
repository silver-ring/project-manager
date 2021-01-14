package com.pm.validations

import com.pm.entities.ProjectsRepo
import com.pm.exception.ProjectNotFound

class ProjectExistValidation(private val projectsRepo: ProjectsRepo, private val projectId: Long):Validation {
    override fun validate() {
        if (!projectsRepo.existsById(projectId)) {
            throw ProjectNotFound()
        }
    }
}
