package com.ecommerce.route

import com.ecommerce.controller.ShopController
import com.ecommerce.models.shop.*
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import com.papsign.ktor.openapigen.route.path.auth.*
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.ecommerce.models.PagingData
import io.ktor.http.*

fun NormalOpenAPIRoute.shopRoute(shopController: ShopController) {
    route("shop/") {
        authenticateWithJwt(RoleManagement.ADMIN.role) {
            route("category").post<AddShopCategory, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        shopController.createShopCategory(params.shopCategoryName), HttpStatusCode.OK
                    )
                )
            }
            route("category").get<PagingData, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        shopController.getShopCategories(
                            params.limit, params.offset
                        ), HttpStatusCode.OK
                    )
                )
            }
            route("category").delete<DeleteShopCategory, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        shopController.deleteShopCategory(params.shopCategoryId), HttpStatusCode.OK
                    )
                )
            }
            route("category").put<UpdateShopCategory, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                shopController.updateShopCategory(
                    params.shopCategoryId, params.shopCategoryName
                ).let {
                    respond(ApiResponse.success(it, HttpStatusCode.OK))
                }
            }
        }
        authenticateWithJwt(RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            route("add-shop").post<AddShop, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                shopController.createShop(
                    principal().userId, params.shopCategoryId, params.shopName
                ).let {
                    respond(ApiResponse.success(it, HttpStatusCode.OK))
                }
            }
        }
    }
}