package com.ecommerce.route

import com.ecommerce.controller.ProductCategoryController
import com.ecommerce.models.PagingData
import com.ecommerce.models.category.AddProductCategory
import com.ecommerce.models.category.DeleteProductCategory
import com.ecommerce.models.category.UpdateProductCategory
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import com.papsign.ktor.openapigen.route.path.auth.delete
import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.put
import com.papsign.ktor.openapigen.route.path.normal.*
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.http.*

fun NormalOpenAPIRoute.productCategoryRoute(productCategoryController: ProductCategoryController) {
    route("product-category") {
        authenticateWithJwt(RoleManagement.CUSTOMER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            get<PagingData, Response, JwtTokenBody> { params ->
                params.validation()
                respond(ApiResponse.success(productCategoryController.getProductCategory(params), HttpStatusCode.OK))
            }
        }
        authenticateWithJwt(RoleManagement.ADMIN.role) {
            post<AddProductCategory, Response, Unit, JwtTokenBody>{ params, _ ->
                params.validation()
                respond(ApiResponse.success(productCategoryController.createProductCategory(params), HttpStatusCode.OK))
            }
            put<UpdateProductCategory, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(ApiResponse.success(productCategoryController.updateProductCategory(params), HttpStatusCode.OK))
            }
            delete<DeleteProductCategory, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        productCategoryController.deleteProductCategory(params), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}