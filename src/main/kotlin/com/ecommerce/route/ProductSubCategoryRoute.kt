package com.ecommerce.route

import com.ecommerce.controller.ProductSubCategoryController
import com.ecommerce.models.subcategory.AddProductSubCategory
import com.ecommerce.models.subcategory.DeleteSubCategory
import com.ecommerce.models.subcategory.UpdateProductSubCategory
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
import com.ecommerce.models.subcategory.PagingDataWithCategoryId
import io.ktor.http.*

fun NormalOpenAPIRoute.productSubCategoryRoute(subCategoryController: ProductSubCategoryController) {
    route("product-sub-category") {
        authenticateWithJwt(RoleManagement.CUSTOMER.role,RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            route("/{categoryId}").get<PagingDataWithCategoryId, Response, JwtTokenBody> { params ->
                params.validation()
                respond(ApiResponse.success(subCategoryController.getProductSubCategory(params), HttpStatusCode.OK))
            }
        }

        authenticateWithJwt(RoleManagement.ADMIN.role) {
            post<AddProductSubCategory, Response, Unit, JwtTokenBody>{ params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        subCategoryController.createProductSubCategory(params), HttpStatusCode.OK
                    )
                )
            }
            put<UpdateProductSubCategory, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(ApiResponse.success(subCategoryController.updateProductSubCategory(params), HttpStatusCode.OK))
            }
            delete<DeleteSubCategory, Response, JwtTokenBody> { params ->
                params.validation()
                respond(
                    ApiResponse.success(
                        subCategoryController.deleteProductSubCategory(params.subCategoryId), HttpStatusCode.OK
                    )
                )
            }
        }
    }
}