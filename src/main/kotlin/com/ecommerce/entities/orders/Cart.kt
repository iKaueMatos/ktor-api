package com.ecommerce.entities.orders

import com.ecommerce.entities.base.BaseIntEntity
import com.ecommerce.entities.base.BaseIntEntityClass
import com.ecommerce.entities.base.BaseIntIdTable
import com.ecommerce.entities.product.Product
import com.ecommerce.entities.product.ProductTable
import com.ecommerce.entities.user.UserTable
import org.jetbrains.exposed.dao.id.EntityID

object CartItemTable : BaseIntIdTable("cart_items") {
    val userId = reference("user_id", UserTable.id)
    val productId = reference("product_id", ProductTable.id)
    val quantity = integer("quantity")
}

class CartItemEntity(id: EntityID<String>) : BaseIntEntity(id, CartItemTable) {
    companion object : BaseIntEntityClass<CartItemEntity>(CartItemTable)

    var userId by CartItemTable.userId
    var productId by CartItemTable.productId
    var quantity by CartItemTable.quantity
    fun cartResponse(product: Product? = null) = Cart(productId.value, quantity, product)
}

data class Cart(
    val productId: String,
    val quantity: Int,
    val product: Product?
)