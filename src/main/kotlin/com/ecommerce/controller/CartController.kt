package com.ecommerce.controller

import com.ecommerce.dbhelper.query
import com.ecommerce.entities.orders.CartItemEntity
import com.ecommerce.entities.orders.CartItemTable
import com.ecommerce.entities.product.ProductEntity
import com.ecommerce.entities.product.ProductTable
import com.ecommerce.models.PagingData
import com.ecommerce.models.cart.*
import com.ecommerce.utils.extension.alreadyExistException
import com.ecommerce.utils.extension.isNotExistException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and

class CartController {
   suspend fun addToCart(userId: String, addCart: AddCart) = query {
        val isProductExist =
            CartItemEntity.find { CartItemTable.userId eq userId and (CartItemTable.productId eq addCart.productId) }
                .toList().singleOrNull()
        isProductExist?.let {
            addCart.productId.alreadyExistException()
        } ?: CartItemEntity.new {
            this.userId = EntityID(userId, CartItemTable)
            productId = EntityID(addCart.productId, CartItemTable)
            quantity = addCart.quantity
        }.cartResponse()
    }

    suspend fun getCartItems(userId: String, pagingData: PagingData) = query {
        CartItemEntity.find { CartItemTable.userId eq userId }.limit(pagingData.limit, pagingData.offset).map {
            it.cartResponse(ProductEntity.find { ProductTable.id eq it.productId }.first().response())
        }
    }

    suspend fun updateCartQuantity(userId: String, updateCart: UpdateCart) = query {
        val productExist =
            CartItemEntity.find { CartItemTable.userId eq userId and (CartItemTable.productId eq updateCart.productId) }
                .toList().singleOrNull()

        productExist?.let {
            it.quantity = it.quantity + updateCart.quantity
            it.cartResponse(ProductEntity.find { ProductTable.id eq it.productId }.first().response())
        }
    }

    suspend fun removeCartItem(userId: String, deleteProduct: DeleteProduct) = query {
        val productExist =
            CartItemEntity.find { CartItemTable.userId eq userId and (CartItemTable.productId eq deleteProduct.productId) }
                .toList().singleOrNull()
        productExist?.let {
            it.delete()
            ProductEntity.find { ProductTable.id eq it.productId }.first().response()
        }
    }

    suspend fun deleteAllFromCart(userId: String) = query {
        val isEmpty = CartItemEntity.find { CartItemTable.userId eq userId }.toList()
        if (isEmpty.isEmpty()) {
            "".isNotExistException()
        } else {
            isEmpty.forEach {
                it.delete()
            }
            true
        }
    }
}