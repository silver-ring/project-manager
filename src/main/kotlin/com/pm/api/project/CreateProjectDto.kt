package com.pm.api.project

import com.pm.api.RequestObject
import com.pm.api.ResponseObject

class CreateProjectRequest : RequestObject {

    var projectName = ""
    var ownerId = ""

}


class CreateProjectResponse : ResponseObject {
    var id: Long = 0
}
