package com.pm.api.project

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.pm.entities.Project
import com.pm.entities.ProjectState
import com.pm.entities.ProjectsRepo
import com.pm.exception.ApiErrorResponse
import feign.FeignException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class UpdateProjectApiTest {

    @MockBean
    lateinit var projectsRepo: ProjectsRepo

    @MockBean
    lateinit var employeesApiProxy: EmployeesApiProxy

    @Autowired
    lateinit var mockMvc: MockMvc

    val projectUrl = "/projects/"

    @Test
    fun updateProject_success() {

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

        Mockito.`when`(projectsRepo.getByName(project.name!!)).thenReturn(null)
        Mockito.`when`(employeesApiProxy.getEmployee(employee.id)).thenReturn(employee)
        Mockito.`when`(projectsRepo.save(project)).thenReturn(newProject)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(projectUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.LOCATION, "http://localhost/projects/${projectId}"))
            .andReturn().response.contentAsString

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals("{\"id\":${projectId}}", result)
        Mockito.verify(employeesApiProxy, Mockito.times(1)).getEmployee(employee.id)
        Mockito.verifyNoMoreInteractions(employeesApiProxy)
        Mockito.verify(projectsRepo, Mockito.times(1)).save(project)
        Mockito.verify(projectsRepo, Mockito.times(1)).existsProjectByName(project.name!!)
        Mockito.verifyNoMoreInteractions(projectsRepo)
    }

    @Test
    fun createProject_missingOwnerId() {
        val createProjectRequest = CreateProjectRequest()
        createProjectRequest.ownerId = "test"

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(projectUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest).andReturn().response.contentAsString

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(
            "{\"detail\":\"projectName is missing\",\"status\":400,\"title\":\"Bad Request\"}", result
        )
        Mockito.verifyNoInteractions(employeesApiProxy)
        Mockito.verifyNoInteractions(projectsRepo);
    }

    @Test
    fun createProject_missingProjectName() {
        val createProjectRequest = CreateProjectRequest()
        createProjectRequest.projectName = "test"

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(projectUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest).andReturn().response.contentAsString

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(
            "{\"detail\":\"ownerId is missing\",\"status\":400,\"title\":\"Bad Request\"}", result
        )
        Mockito.verifyNoInteractions(employeesApiProxy)
        Mockito.verifyNoInteractions(projectsRepo);
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

        val feignException = Mockito.mock(FeignException::class.java)

        val apiErrorResponse = ApiErrorResponse()
        apiErrorResponse.detail = "Not Found"
        apiErrorResponse.status = HttpStatus.NOT_FOUND.value()
        apiErrorResponse.title = "Employee not found"

        val apiErrorResponseStr = jacksonObjectMapper()
            .writeValueAsString(apiErrorResponse)

        Mockito.`when`(projectsRepo.existsProjectByName(project.name!!)).thenReturn(false)
        Mockito.`when`(feignException.contentUTF8()).thenReturn(apiErrorResponseStr)
        Mockito.`when`(employeesApiProxy.getEmployee(employee.id)).thenThrow(feignException)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(projectUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(MockMvcResultMatchers.status().isNotFound).andReturn().response.contentAsString

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(
            "{\"detail\":\"${apiErrorResponse.detail}\",\"status\":${apiErrorResponse.status},\"title\":\"${apiErrorResponse.title}\"}",
            result
        )
        Mockito.verify(projectsRepo, Mockito.times(1)).existsProjectByName(project.name!!)
        Mockito.verify(employeesApiProxy, Mockito.times(1)).getEmployee(employee.id)
        Mockito.verifyNoMoreInteractions(employeesApiProxy)
        Mockito.verifyNoMoreInteractions(projectsRepo)
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

        Mockito.`when`(employeesApiProxy.getEmployee(employee.id)).thenReturn(employee)
        Mockito.`when`(projectsRepo.existsProjectByName(project.name!!)).thenReturn(true)

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(projectUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createProjectRequest))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest).andReturn().response.contentAsString

        Assertions.assertFalse(result.isEmpty())
        Assertions.assertEquals(
            "{\"detail\":\"Project name is already exist\",\"status\":400,\"title\":\"Bad Request\"}",
            result
        )
        Mockito.verify(projectsRepo, Mockito.times(1)).existsProjectByName(project.name!!)
        Mockito.verifyNoMoreInteractions(projectsRepo)
    }

}
