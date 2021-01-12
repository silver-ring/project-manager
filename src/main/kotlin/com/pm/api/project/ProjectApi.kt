package com.pm.api.project

import com.pm.api.NoRequestObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@ResponseBody
@RequestMapping("/projects")
class ProjectApi @Autowired constructor(
    private val createProjectExecutor: CreateProjectExecutor,
    private val getAllProjectsExecutor: GetAllProjectsExecutor
) {

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProject(@RequestBody createProjectRequest: CreateProjectRequest): CreateProjectResponse {
        return createProjectExecutor.execute(createProjectRequest)
    }

    @GetMapping("/")
    fun getAllProjects(): GetAllProjectsResponse {
        return getAllProjectsExecutor.execute(NoRequestObject())
    }

    @GetMapping("/{id}")
    fun getProject(@PathVariable id: String) {
        // TODO
    }

    @PutMapping("/{id}")
    fun updateProject(@PathVariable id: Long, @RequestBody updateProjectRequest: UpdateProjectRequest) {
        // TODO
    }

    @PatchMapping("/{id}")
    fun assignParticipant(@PathVariable id: Long, @RequestBody assignParticipantRequest: AssignParticipantRequest) {
        // TODO
    }

}
