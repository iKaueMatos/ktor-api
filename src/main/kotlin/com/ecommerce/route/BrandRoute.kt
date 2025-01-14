package com.ecommerce.route

import com.ecommerce.controller.BrandController
import com.ecommerce.models.PagingData
import com.ecommerce.models.bands.AddBrand
import com.ecommerce.models.bands.DeleteBrand
import com.ecommerce.models.bands.UpdateBrand
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import com.papsign.ktor.openapigen.route.path.auth.delete
import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.put
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import io.ktor.http.*

fun NormalOpenAPIRoute.brandRoute(brandController: BrandController) {
    route("brand") {
        authenticateWithJwt(RoleManagement.CUSTOMER.role, RoleManagement.SELLER.role, RoleManagement.ADMIN.role) {
            get<PagingData, Response, JwtTokenBody> { params ->
                params.validation()
                respond(ApiResponse.success(brandController.getBrand(params), HttpStatusCode.OK))
            }
        }
        authenticateWithJwt(RoleManagement.ADMIN.role) {
            post<AddBrand, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        brandController.createBrand(params), HttpStatusCode.OK
                    )
                )
            }
            put<UpdateBrand, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(ApiResponse.success(brandController.updateBrand(params), HttpStatusCode.OK))
            }
            delete<DeleteBrand, Response, JwtTokenBody> { params ->
                params.validation()
                respond(ApiResponse.success(brandController.deleteBrand(params), HttpStatusCode.OK))
            }
        }
    }
}