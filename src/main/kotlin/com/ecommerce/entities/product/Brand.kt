package com.ecommerce.entities.product

import com.ecommerce.entities.base.BaseIntEntity
import com.ecommerce.entities.base.BaseIntEntityClass
import com.ecommerce.entities.base.BaseIntIdTable
import org.jetbrains.exposed.dao.id.EntityID

object BrandTable : BaseIntIdTable("brand") {
    val brandName = text("brand_name")
    val brandLogo = text("brand_log").nullable()
}

class BrandEntity(id: EntityID<String>) : BaseIntEntity(id, BrandTable) {
    companion object : BaseIntEntityClass<BrandEntity>(BrandTable)

    var brandName by BrandTable.brandName
    var brandLogo by BrandTable.brandLogo
    fun brandResponse() = Brand(id.value, brandName, brandLogo)
}

data class Brand(val id: String, val brandName: String, val brandLogo: String?)