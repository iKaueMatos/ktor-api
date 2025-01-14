package com.ecommerce.route

import com.papsign.ktor.openapigen.route.path.auth.*
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.ecommerce.controller.ShippingController
import com.ecommerce.models.shipping.AddShipping
import com.ecommerce.models.shipping.OrderId
import com.ecommerce.models.shipping.UpdateShipping
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import io.ktor.http.*

fun NormalOpenAPIRoute.shippingRoute(shippingController: ShippingController) {
    route("shipping") {
        authenticateWithJwt(RoleManagement.CUSTOMER.role) {
            post<Unit, Response, AddShipping, JwtTokenBody> { _, requestBody ->
                requestBody.validation()
                respond(
                    ApiResponse.success(
                        shippingController.addShipping(principal().userId, requestBody), HttpStatusCode.OK
                    )
                )
            }
            get<OrderId, Response, JwtTokenBody> { params ->
                respond(
                    ApiResponse.success(
                        shippingController.getShipping(principal().userId, params.orderId), HttpStatusCode.OK
                    )
                )
            }
            route("/{orderId}").put<UpdateShipping, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                shippingController.updateShipping(
                    principal().userId, params
                ).let {
                    respond(ApiResponse.success(it, HttpStatusCode.OK))
                }
            }
            delete<OrderId, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        shippingController.deleteShipping(principal().userId, params.orderId), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}