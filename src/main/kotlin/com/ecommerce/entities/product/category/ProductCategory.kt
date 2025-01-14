package com.ecommerce.entities.product.category

import com.ecommerce.entities.base.BaseIntEntity
import com.ecommerce.entities.base.BaseIntEntityClass
import com.ecommerce.entities.base.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object ProductCategoryTable : BaseIntIdTable("category") {
    val categoryName = text("category_name")
    val image = text("image").nullable()
}

class ProductCategoryEntity(id: EntityID<String>) : BaseIntEntity(id, ProductCategoryTable) {
    companion object : BaseIntEntityClass<ProductCategoryEntity>(ProductCategoryTable)

    var categoryName by ProductCategoryTable.categoryName
    private val subCategories by ProductSubCategoryEntity referrersOn ProductSubCategoryTable.categoryId
    var image by ProductCategoryTable.image
    fun response() =
        ProductCategoryResponse(id.value, categoryName, subCategories.map { it.response() }, image)
}

data class ProductCategoryResponse(
    val id: String,
    val categoryName: String,
    val subCategories: List<ProductSubCategoryResponse>,
    val image: String?
)