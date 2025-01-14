package com.ecommerce.route

import com.papsign.ktor.openapigen.route.path.auth.*
import com.ecommerce.controller.CartController
import com.ecommerce.models.PagingData
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.ecommerce.models.cart.*
import io.ktor.http.*

fun NormalOpenAPIRoute.cartRoute(cartController: CartController) {
    route("cart") {
        authenticateWithJwt(RoleManagement.CUSTOMER.role) {
            post<AddCart, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(ApiResponse.success(cartController.addToCart(principal().userId, params), HttpStatusCode.OK))
            }
            route("/{productId}").delete<DeleteProduct, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        cartController.removeCartItem(principal().userId, params), HttpStatusCode.OK
                    )
                )
            }
            route("/{productId}").put<UpdateCart, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        cartController.updateCartQuantity(principal().userId, params), HttpStatusCode.OK
                    )
                )
            }
            get<PagingData, Response, JwtTokenBody> { pagingData ->
                pagingData.validation()
                respond(
                    ApiResponse.success(
                        cartController.getCartItems(principal().userId, pagingData),
                        HttpStatusCode.OK
                    )
                )
            }
            route("/all").delete<Unit, Response, JwtTokenBody> { _ ->
                respond(
                    ApiResponse.success(
                        cartController.deleteAllFromCart(principal().userId), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}