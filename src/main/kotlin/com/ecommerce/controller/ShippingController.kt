package com.ecommerce.controller

import com.ecommerce.dbhelper.query
import com.ecommerce.entities.ShippingEntity
import com.ecommerce.entities.ShippingTable
import com.ecommerce.entities.orders.OrdersTable
import com.ecommerce.entities.user.UserTable
import com.ecommerce.models.shipping.AddShipping
import com.ecommerce.models.shipping.UpdateShipping
import com.ecommerce.utils.extension.alreadyExistException
import com.ecommerce.utils.extension.isNotExistException
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and

class ShippingController {
   suspend fun addShipping(userId: String, addShipping: AddShipping) = query {
        val isExist = ShippingEntity.find {
            UserTable.id eq userId and (OrdersTable.id eq addShipping.orderId)
        }.toList().singleOrNull()
        isExist?.let {
            addShipping.orderId.alreadyExistException()
        } ?: run {
            ShippingEntity.new {
                this.userId = EntityID(userId, ShippingTable)
                this.orderId = EntityID(userId, ShippingTable)
                shippingAddress = addShipping.shipAddress
                shippingCity = addShipping.shipCity
                shippingPhone = addShipping.shipPhone
                shippingName = addShipping.shipName
                shippingEmail = addShipping.shipEmail
                shippingCountry = addShipping.shipCountry

            }
        }
    }

    suspend fun getShipping(userId: String, orderId: String) = query {
        val isExist = ShippingEntity.find {
            UserTable.id eq userId and (OrdersTable.id eq orderId)
        }.toList().singleOrNull()
        isExist?.response() ?: run {
            orderId.isNotExistException()
        }
    }

    suspend fun updateShipping(userId: String, updateShipping: UpdateShipping) = query {
        val isExist = ShippingEntity.find {
            UserTable.id eq userId and (OrdersTable.id eq updateShipping.orderId)
        }.toList().singleOrNull()

        isExist?.let {
            it.shippingAddress = updateShipping.shipAddress ?: it.shippingAddress
            it.shippingCity = updateShipping.shipCity ?: it.shippingCity
            it.shippingPhone = updateShipping.shipPhone ?: it.shippingPhone
            it.shippingName = updateShipping.shipName ?: it.shippingName
            it.shippingEmail = updateShipping.shipEmail ?: it.shippingEmail
            it.shippingCountry = updateShipping.shipCountry ?: it.shippingCountry
        } ?: run {
            updateShipping.orderId.isNotExistException()
        }
    }

    suspend fun deleteShipping(userId: String, orderId: String) = query {
        val isExist = ShippingEntity.find {
            UserTable.id eq userId and (OrdersTable.id eq orderId)
        }.toList().singleOrNull()
        isExist?.delete() ?: orderId.isNotExistException()
    }
}