package com.pm.validations

import com.pm.entities.ProjectsRepo
import com.pm.exception.ProjectNameDuplicated

class ProjectNameDuplicationValidation(
    private val projectsRepo: ProjectsRepo,
    private val projectId: Long,
    private val projectName: String
) :

    Validation {
    override fun validate() {
        val otherProject = projectsRepo.getByName(projectName)
        if (otherProject.id == projectId) {
            throw ProjectNameDuplicated()
        }
    }

}
