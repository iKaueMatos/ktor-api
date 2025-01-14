package com.ecommerce.entities.orders

import com.ecommerce.entities.base.BaseIntEntity
import com.ecommerce.entities.base.BaseIntEntityClass
import com.ecommerce.entities.base.BaseIntIdTable
import com.ecommerce.entities.product.ProductTable
import org.jetbrains.exposed.dao.id.EntityID

object OrderItemTable : BaseIntIdTable("order_items") {
    val orderId = reference("order_id", OrdersTable.id)
    val productId = reference("product_id", ProductTable.id)
    val quantity = integer("quantity")
}

class OrderItemEntity(id: EntityID<String>) : BaseIntEntity(id, OrderItemTable) {
    companion object : BaseIntEntityClass<OrderItemEntity>(OrderItemTable)

    var orderId by OrderItemTable.orderId
    var productId by OrderItemTable.productId
    var quantity by OrderItemTable.quantity
}