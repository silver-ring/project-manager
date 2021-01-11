package com.pm.api.project

import com.pm.api.ApiExecutor
import com.pm.validations.*
import com.pm.entities.Project
import com.pm.entities.ProjectState
import com.pm.entities.ProjectsRepo
import org.springframework.stereotype.Service

@Service
class CreateProjectExecutor(
    private val validationContext: ValidationContext,
    private val employeesApiProxy: EmployeesApiProxy,
    private val projectsRepo: ProjectsRepo
) : ApiExecutor<CreateProjectRequest, CreateProjectResponse>() {

    override fun validateInputs(createProjectRequest: CreateProjectRequest): ValidationContext {
        validationContext.addValidation(ProjectOwnerValidation(createProjectRequest.ownerId))
        validationContext.addValidation(ProjectNameValidation(createProjectRequest.projectName))
        return validationContext
    }

    override fun validateRules(createProjectRequest: CreateProjectRequest): ValidationContext {
        validationContext.addValidation(ProjectOwnerRoleValidation(employeesApiProxy, createProjectRequest.ownerId))
        validationContext.addValidation(ProjectNameExistValidation(projectsRepo, createProjectRequest.projectName))
        return validationContext
    }

    override fun performTransaction(createProjectRequest: CreateProjectRequest): CreateProjectResponse {
        val project = Project()
        project.state = ProjectState.NOT_STARTED
        project.name = createProjectRequest.projectName
        project.ownerId = createProjectRequest.ownerId
        val newProject = projectsRepo.save(project)
        val createProjectResponse = CreateProjectResponse()
        createProjectResponse.id = newProject.id!!
        return createProjectResponse
    }

}
