package com.pm.api

import com.pm.validations.ValidationContext

interface RequestObject {

}

interface ResponseObject {

}

class NoRequestObject : RequestObject {

}

class NoResponseObject : ResponseObject {

}

abstract class ApiExecutor<ReqObj:RequestObject, ResObj:ResponseObject> {

    fun execute(requestObject: ReqObj): ResObj {
        val inputValidationContext = validateInputs(requestObject)
        inputValidationContext.validate()
        val rulesValidationContext = validateRules(requestObject)
        rulesValidationContext.validate()
        return performTransaction(requestObject)
    }

    abstract fun validateInputs(requestObject: ReqObj): ValidationContext

    abstract fun validateRules(requestObject: ReqObj): ValidationContext

    abstract fun performTransaction(requestObject: ReqObj): ResObj

}
