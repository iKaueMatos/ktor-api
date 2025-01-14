package com.ecommerce.controller

import com.ecommerce.dbhelper.query
import com.ecommerce.entities.product.BrandEntity
import com.ecommerce.entities.product.BrandTable
import com.ecommerce.models.PagingData
import com.ecommerce.models.bands.AddBrand
import com.ecommerce.models.bands.DeleteBrand
import com.ecommerce.models.bands.UpdateBrand
import com.ecommerce.utils.extension.alreadyExistException
import com.ecommerce.utils.extension.isNotExistException

class BrandController {
    suspend fun createBrand(addBand: AddBrand) = query {
        val brandExist = BrandEntity.find { BrandTable.brandName eq addBand.brandName }.toList().singleOrNull()
         if (brandExist == null) {
            BrandEntity.new {
                brandName = addBand.brandName
            }.brandResponse()
        } else {
            addBand.brandName.alreadyExistException()
        }
    }

    suspend fun getBrand(paging: PagingData) = query {
        val brands = BrandEntity.all().limit(paging.limit, paging.offset)
        brands.map {
            it.brandResponse()
        }
    }

    suspend fun updateBrand(updateBrand: UpdateBrand) = query {
        val isBrandExist = BrandEntity.find { BrandTable.id eq updateBrand.brandId }.toList().singleOrNull()
        isBrandExist?.let {
            it.brandName = updateBrand.brandName
            // return category response
            it.brandResponse()
        } ?: updateBrand.brandId.isNotExistException()

    }

   suspend fun deleteBrand(deleteBrand: DeleteBrand) = query {
        val isBrandExist = BrandEntity.find { BrandTable.id eq deleteBrand.brandId }.toList().singleOrNull()
        isBrandExist?.let {
            it.delete()
            deleteBrand.brandId
        } ?: deleteBrand.brandId.isNotExistException()
    }
}