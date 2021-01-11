package com.pm.api.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pm.entities.Project
import com.pm.entities.ProjectState
import com.pm.entities.ProjectsRepo
import com.pm.exception.ApiErrorResponse
import feign.FeignException

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class CreateProjectApiTest {

    @MockBean
    lateinit var projectsRepo: ProjectsRepo

    @MockBean
    lateinit var employeesApiProxy: EmployeesApiProxy

    @Autowired
    lateinit var mockMvc: MockMvc

    val projectUrl = "/projects/"

    @Test
    fun createProject_success() {

        val createProjectRequest = CreateProjectRequest()
        createProjectRequest.ownerId = "12"
        createProjectRequest.projectName = "test"

        val employee = Employee()
        employee.id = "12"
        employee.role = Roles.manager

        val project = Project()
        project.name = createProjectRequest.projectName
        project.state = ProjectState.NOT_STARTED
        project.ownerId = createProjectRequest.ownerId

        val projectId = 1L

        val newProject = Project()
        newProject.id = projectId
        newProject.name = createProjectRequest.projectName
        newProject.state = ProjectState.NOT_STARTED
        newProject.ownerId = createProjectRequest.ownerId

        `when`(employeesApiProxy.getEmployee(employee.id)).thenReturn(employee)
        `when`(projectsRepo.save(project)).thenReturn(newProject)

        val result = mockMvc.perform(
            post(projectUrl)
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(status().isCreated).andReturn().response.contentAsString

        assertFalse(result.isEmpty())
        assertEquals("{\"id\":${projectId}}", result)
        verify(employeesApiProxy, times(1)).getEmployee(employee.id)
        verifyNoMoreInteractions(employeesApiProxy)
        verify(projectsRepo, times(1)).save(project)
        verify(projectsRepo, times(1)).findProjectByNameEquals(project.name!!)
        verifyNoMoreInteractions(projectsRepo)
    }

    @Test
    fun createProject_missingOwnerId() {
        val createProjectRequest = CreateProjectRequest()
        createProjectRequest.ownerId = "test"

        val result = mockMvc.perform(
            post(projectUrl)
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(status().isBadRequest).andReturn().response.contentAsString

        assertFalse(result.isEmpty())
        assertEquals(
            "{\"detail\":\"projectName is missing\",\"status\":400,\"title\":\"Bad Request\"}", result
        )
        verifyNoInteractions(employeesApiProxy)
        verifyNoInteractions(projectsRepo);
    }

    @Test
    fun createProject_missingProjectName() {
        val createProjectRequest = CreateProjectRequest()
        createProjectRequest.projectName = "test"

        val result = mockMvc.perform(
            post(projectUrl)
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(status().isBadRequest).andReturn().response.contentAsString

        assertFalse(result.isEmpty())
        assertEquals(
            "{\"detail\":\"ownerId is missing\",\"status\":400,\"title\":\"Bad Request\"}", result
        )
        verifyNoInteractions(employeesApiProxy)
        verifyNoInteractions(projectsRepo);
    }

    @Test
    fun createProject_employeeNotFound() {

        val createProjectRequest = CreateProjectRequest()
        createProjectRequest.ownerId = "12"
        createProjectRequest.projectName = "test"

        val employee = Employee()
        employee.id = "12"
        employee.role = Roles.manager

        val project = Project()
        project.name = createProjectRequest.projectName
        project.state = ProjectState.NOT_STARTED
        project.ownerId = createProjectRequest.ownerId

        val projectId = 1L

        val newProject = Project()
        newProject.id = projectId
        newProject.name = createProjectRequest.projectName
        newProject.state = ProjectState.NOT_STARTED
        newProject.ownerId = createProjectRequest.ownerId

        val feignException = mock(FeignException::class.java)

        val apiErrorResponse = ApiErrorResponse()
        apiErrorResponse.detail = "Not Found"
        apiErrorResponse.status = HttpStatus.NOT_FOUND.value()
        apiErrorResponse.title = "Employee not found"

        val apiErrorResponseStr = jacksonObjectMapper()
            .writeValueAsString(apiErrorResponse)

        `when`(feignException.contentUTF8()).thenReturn(apiErrorResponseStr)
        `when`(employeesApiProxy.getEmployee(employee.id)).thenThrow(feignException)

        val result = mockMvc.perform(
            post(projectUrl)
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(status().isNotFound).andReturn().response.contentAsString

        assertFalse(result.isEmpty())
        assertEquals(
            "{\"detail\":\"${apiErrorResponse.detail}\",\"status\":${apiErrorResponse.status},\"title\":\"${apiErrorResponse.title}\"}",
            result
        )
        verify(employeesApiProxy, times(1)).getEmployee(employee.id)
        verifyNoMoreInteractions(employeesApiProxy)
        verifyNoInteractions(projectsRepo)
    }

    @Test
    fun createProject_projectNameAlreadyExist() {

        val createProjectRequest = CreateProjectRequest()
        createProjectRequest.ownerId = "12"
        createProjectRequest.projectName = "test"

        val employee = Employee()
        employee.id = "12"
        employee.role = Roles.manager

        val project = Project()
        project.name = createProjectRequest.projectName
        project.state = ProjectState.NOT_STARTED
        project.ownerId = createProjectRequest.ownerId

        val projectId = 1L

        val newProject = Project()
        newProject.id = projectId
        newProject.name = createProjectRequest.projectName
        newProject.state = ProjectState.NOT_STARTED
        newProject.ownerId = createProjectRequest.ownerId

        `when`(employeesApiProxy.getEmployee(employee.id)).thenReturn(employee)
        `when`(projectsRepo.findProjectByNameEquals(project.name!!)).thenReturn(Optional.of(project))

        val result = mockMvc.perform(
            post(projectUrl)
                .contentType(APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(status().isBadRequest).andReturn().response.contentAsString

        assertFalse(result.isEmpty())
        assertEquals("{\"detail\":\"Project name is already exist\",\"status\":400,\"title\":\"Bad Request\"}", result)
        verify(projectsRepo, times(1)).findProjectByNameEquals(project.name!!)
        verifyNoMoreInteractions(projectsRepo)
    }


}
