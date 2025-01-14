package com.ecommerce.controller

import com.ecommerce.dbhelper.query
import com.ecommerce.entities.orders.*
import com.ecommerce.models.PagingData
import com.ecommerce.models.order.AddOrder
import com.ecommerce.models.order.OrderId
import com.ecommerce.utils.extension.OrderStatus
import com.ecommerce.utils.extension.isNotExistException
import com.ecommerce.utils.extension.orderStatusCode
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and

class OrderController {
    suspend fun createOrder(userId: String, addOrder: AddOrder) = query {
        val order = OrderEntity.new {
            this.userId = EntityID(userId, OrdersTable)
            this.quantity = addOrder.quantity
            this.shippingCharge = addOrder.shippingCharge
            this.subTotal = addOrder.subTotal
            this.total = addOrder.total
        }
        addOrder.orderItems.forEach {
            OrderItemEntity.new {
                orderId = EntityID(order.id.value, OrderItemTable)
                productId = EntityID(it.productId, OrderItemTable)
                quantity = it.quantity
            }
        }
        addOrder.orderItems.forEach {
            val productExist =
                CartItemEntity.find { CartItemTable.userId eq userId and (CartItemTable.productId eq it.productId) }
                    .toList().singleOrNull()
            productExist?.delete()

        }
        order.orderCreatedResponse()
    }

   suspend fun getOrders(userId: String, pagingData: PagingData) = query {
        OrderEntity.find { OrdersTable.userId eq userId }.limit(pagingData.limit, pagingData.offset).map {
            it.response()
        }
    }

    suspend fun updateOrder(userId: String, orderId: OrderId, orderStatus: OrderStatus) = query {
        val orderExist =
            OrderEntity.find { OrdersTable.userId eq userId and (OrdersTable.id eq orderId.orderId) }.toList()
                .singleOrNull()
        orderExist?.let {
            it.status = orderStatus.name.lowercase()
            it.statusCode = orderStatus.name.lowercase().orderStatusCode()
            it.response()
        }?: "".isNotExistException()
    }
}