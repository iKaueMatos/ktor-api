package com.ecommerce.route

import com.papsign.ktor.openapigen.route.path.auth.*
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import com.ecommerce.controller.OrderController
import com.ecommerce.models.PagingData
import com.ecommerce.models.order.AddOrder
import com.ecommerce.models.order.OrderId
import com.ecommerce.models.orderitem.OrderItem
import com.ecommerce.models.user.body.JwtTokenBody
import com.ecommerce.plugins.RoleManagement
import com.ecommerce.utils.ApiResponse
import com.ecommerce.utils.Response
import com.ecommerce.utils.authenticateWithJwt
import com.ecommerce.utils.extension.OrderStatus
import io.ktor.http.*

fun NormalOpenAPIRoute.orderRoute(orderController: OrderController) {
    route("order") {
        authenticateWithJwt(RoleManagement.CUSTOMER.role) {
            post<Unit, Response, AddOrder, JwtTokenBody>(
                exampleRequest = AddOrder(
                    1,
                    100f,
                    100f,
                    2f,
                    orderStatus = "pending",
                    mutableListOf(OrderItem("productId", 1)),
                ),
            ) { _, orderBody ->
                orderBody.validation()
                respond(
                    ApiResponse.success(
                        orderController.createOrder(principal().userId, orderBody), HttpStatusCode.OK
                    )
                )
            }
            get<PagingData, Response, JwtTokenBody> { params ->
                params.validation()
                respond(ApiResponse.success(orderController.getOrders(principal().userId, params), HttpStatusCode.OK))
            }
            route("/payment").put<OrderId, Response, Unit, JwtTokenBody> { params,_  ->
                params.validation()
                respond(
                    ApiResponse.success(
                        orderController.updateOrder(principal().userId, params, OrderStatus.PAID),
                        HttpStatusCode.OK
                    )
                )
            }
            route("/cancel").put<OrderId, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        orderController.updateOrder(principal().userId, params, OrderStatus.CANCELED), HttpStatusCode.OK
                    )
                )
            }
            route("/receive").put<OrderId, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        orderController.updateOrder(principal().userId, params, OrderStatus.RECEIVED), HttpStatusCode.OK
                    )
                )
            }
        }
        authenticateWithJwt(RoleManagement.SELLER.role) {
            route("/confirm").put<OrderId, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        orderController.updateOrder(principal().userId, params, OrderStatus.CONFIRMED),
                        HttpStatusCode.OK
                    )
                )
            }
            route("/deliver").put<OrderId, Response, Unit, JwtTokenBody> { params, _ ->
                params.validation()
                respond(
                    ApiResponse.success(
                        orderController.updateOrder(principal().userId, params, OrderStatus.DELIVERED),
                        HttpStatusCode.OK
                    )
                )
            }
        }
    }
}