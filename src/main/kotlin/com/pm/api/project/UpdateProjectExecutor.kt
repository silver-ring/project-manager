package com.pm.api.project

import com.pm.api.ApiExecutor
import com.pm.api.RequestObject
import com.pm.api.ResponseObject
import com.pm.entities.Project
import com.pm.entities.ProjectState
import com.pm.entities.ProjectsRepo
import com.pm.validations.*
import org.springframework.stereotype.Service


class UpdateProjectRequest : RequestObject {
    var id = 0L
    var projectName = ""
    var ownerId = ""
    var state = ProjectState.NOT_STARTED
}

class UpdateProjectResponse : ResponseObject {
    var id = 0L
    var name = ""
    var ownerId = ""
    var state = ProjectState.NOT_STARTED
}

@Service
class UpdateProjectExecutor(
    private val validationContext: ValidationContext,
    private val projectsRepo: ProjectsRepo,
    private val employeesApiProxy: EmployeesApiProxy
) :
    ApiExecutor<UpdateProjectRequest, UpdateProjectResponse>() {

    override fun validateInputs(requestObject: UpdateProjectRequest): ValidationContext {
        validationContext.addValidation(ProjectOwnerValidation(requestObject.ownerId))
        validationContext.addValidation(ProjectNameValidation(requestObject.projectName))
        return validationContext
    }

    override fun validateRules(requestObject: UpdateProjectRequest): ValidationContext {
        validationContext.addValidation(ProjectExistValidation(projectsRepo, requestObject.id))
        validationContext.addValidation(ProjectNameDuplicationValidation(projectsRepo, requestObject.id, requestObject.projectName))
        validationContext.addValidation(ProjectOwnerRoleValidation(employeesApiProxy, requestObject.ownerId))
        return validationContext
    }

    override fun performTransaction(requestObject: UpdateProjectRequest): UpdateProjectResponse {
        val updateProjectResponse = UpdateProjectResponse()
        val project = Project()
        project.id = updateProjectResponse.id
        project.name = updateProjectResponse.name
        project.ownerId = updateProjectResponse.ownerId
        projectsRepo.save(project)
        return updateProjectResponse
    }

}
