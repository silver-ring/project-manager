package com.pm.api.project

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@ResponseBody
@RequestMapping("/projects")
class ProjectApi @Autowired constructor(
    private val createProjectExecutor: CreateProjectExecutor
) {

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createProject(@RequestBody createProjectRequest: CreateProjectRequest): CreateProjectResponse {
        return createProjectExecutor.execute(createProjectRequest)
    }

    @GetMapping("/")
    fun getAllProjects() {
        // TODO
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
