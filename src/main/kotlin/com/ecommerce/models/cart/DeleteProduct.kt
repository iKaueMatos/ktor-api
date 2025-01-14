package com.ecommerce.models.cart

import com.papsign.ktor.openapigen.annotations.parameters.PathParam
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class DeleteProduct(@PathParam("productId") val productId:String){
    fun validation() {
        validate(this) {
            validate(DeleteProduct::productId).isNotNull().isNotEmpty()
        }
    }
}
