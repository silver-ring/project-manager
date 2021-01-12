package com.pm.api.project

import com.pm.api.ResponseObject

class GetAllProjectsResponse : ResponseObject {
    var data = listOf<GetAllProjectsResponseData>()
}

class GetAllProjectsResponseData {
    var projectId = 0L
    var projectName = ""
    var ownerId = ""
}
