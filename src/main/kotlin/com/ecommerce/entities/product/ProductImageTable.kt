package com.ecommerce.entities.product

import com.ecommerce.entities.base.BaseIntEntity
import com.ecommerce.entities.base.BaseIntEntityClass
import com.ecommerce.entities.base.BaseIntIdTable
import com.ecommerce.entities.user.UserTable
import org.jetbrains.exposed.dao.id.EntityID

object ProductImageTable : BaseIntIdTable("product_image") {
    val userId = reference("user_id", UserTable.id)
    val productId = reference("product_id", ProductTable.id)
    val imageUrl = text("image_url") // multiple image will be saved comma seperated string
}

class ProductImageEntity(id: EntityID<String>) : BaseIntEntity(id, ProductImageTable) {
    companion object : BaseIntEntityClass<ProductImageEntity>(ProductImageTable)

    var userId by ProductImageTable.userId
    var productId by ProductImageTable.productId
    var imageUrl by ProductImageTable.imageUrl
    fun response() = ImageUrl(id.value, imageUrl)
}

data class ImageUrl(
    val id: String,
    val imageUrl: String,
)