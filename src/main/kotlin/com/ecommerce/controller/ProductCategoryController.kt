package com.ecommerce.controller

import com.ecommerce.dbhelper.query
import com.ecommerce.entities.product.category.ProductCategoryEntity
import com.ecommerce.entities.product.category.ProductCategoryTable
import com.ecommerce.models.PagingData
import com.ecommerce.models.category.AddProductCategory
import com.ecommerce.models.category.DeleteProductCategory
import com.ecommerce.models.category.UpdateProductCategory
import com.ecommerce.utils.extension.alreadyExistException
import com.ecommerce.utils.extension.isNotExistException

class ProductCategoryController {
   suspend fun createProductCategory(addProductCategory: AddProductCategory) = query {
        val categoryExist =
            ProductCategoryEntity.find { ProductCategoryTable.categoryName eq addProductCategory.categoryName }.toList().singleOrNull()

        if (categoryExist == null) {
            ProductCategoryEntity.new {
                categoryName = addProductCategory.categoryName
            }.response()
        } else {
            addProductCategory.categoryName.alreadyExistException()
        }
    }

    suspend fun getProductCategory(paging: PagingData) = query {
        val categories = ProductCategoryEntity.all().limit(paging.limit, paging.offset)
        categories.map {
            it.response()
        }
    }

   suspend fun updateProductCategory(updateProductCategory: UpdateProductCategory) = query {
        val categoryExist =
            ProductCategoryEntity.find { ProductCategoryTable.id eq updateProductCategory.categoryId }.toList().singleOrNull()
        categoryExist?.let {
            it.categoryName = updateProductCategory.categoryName
            // return category response
            it.response()
        } ?: run {
            updateProductCategory.categoryId.isNotExistException()
        }
    }

   suspend fun deleteProductCategory(deleteProductCategory: DeleteProductCategory) = query {
        val categoryExist =
            ProductCategoryEntity.find { ProductCategoryTable.id eq deleteProductCategory.categoryId }.toList().singleOrNull()
        categoryExist?.let {
            categoryExist.delete()
            deleteProductCategory.categoryId
        }
    }
}