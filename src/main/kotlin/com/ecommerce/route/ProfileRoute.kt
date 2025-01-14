package com.ecommerce.route

import com.papsign.ktor.openapigen.route.path.auth.get
import com.papsign.ktor.openapigen.route.path.auth.post
import com.papsign.ktor.openapigen.route.path.auth.principal
import com.papsign.ktor.openapigen.route.path.auth.put
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.ecommerce.controller.ProfileController
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.models.user.body.MultipartImage
import com.ecommerce.models.user.body.UserProfileBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.AppConstants
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import com.ecommerce.utils.extension.fileExtension
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

fun NormalOpenAPIRoute.profileRouting(profileController: ProfileController) {
    authenticateWithJwt(RoleManagement.ADMIN.role, RoleManagement.SELLER.role, RoleManagement.CUSTOMER.role) {
        route("profile").get<Unit, Response, JwtTokenBody> { _ ->
            respond(
                ApiResponse.success(
                    profileController.getProfile(principal().userId), HttpStatusCode.OK
                )
            )
        }
        route("profile").put<Unit, Response, UserProfileBody, JwtTokenBody> { _, requestBody ->
            respond(
                ApiResponse.success(
                    profileController.updateProfile(principal().userId, requestBody), HttpStatusCode.OK
                )
            )
        }

        route("profile-photo-upload").post<Unit, Response, MultipartImage, JwtTokenBody> { _, multipartData ->
            multipartData.validation()

            UUID.randomUUID()?.let { imageId ->
                val fileLocation = multipartData.file.name?.let {
                    "${AppConstants.Image.PROFILE_IMAGE_LOCATION}$imageId${it.fileExtension()}"
                }
                fileLocation?.let {
                    File(it).writeBytes(withContext(Dispatchers.IO) {
                        multipartData.file.readAllBytes()
                    })
                }
                val fileNameInServer = imageId.toString().plus(fileLocation?.fileExtension())
                profileController.updateProfileImage(principal().userId, fileNameInServer)?.let {
                    respond(
                        ApiResponse.success(fileNameInServer, HttpStatusCode.OK)
                    )
                }
            }
        }
    }
}