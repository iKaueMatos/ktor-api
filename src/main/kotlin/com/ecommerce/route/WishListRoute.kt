package com.ecommerce.route

import com.papsign.ktor.openapigen.route.path.auth.delete
import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.principal
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.ecommerce.controller.WishListController
import com.ecommerce.models.product.request.ProductId
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import io.ktor.http.*

fun NormalOpenAPIRoute.wishListRoute(wishlistController: WishListController) {
    route("wishlist") {
        authenticateWithJwt(RoleManagement.CUSTOMER.role) {
            post<ProductId, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        wishlistController.addToWishList(principal().userId, params.productId), HttpStatusCode.OK
                    )
                )
            }
            get<Unit, Response, JwtTokenBody> { _ ->
                respond(ApiResponse.success(wishlistController.getWishList(principal().userId), HttpStatusCode.OK))
            }
            delete<ProductId, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        wishlistController.deleteFromWishList(principal().userId, params.productId), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}