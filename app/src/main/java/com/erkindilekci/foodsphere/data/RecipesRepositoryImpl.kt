package com.erkindilekci.foodsphere.data

import com.erkindilekci.foodsphere.domain.RecipesPaginationModel
import com.erkindilekci.foodsphere.domain.RecipesRepository
import com.erkindilekci.foodsphere.domain.toModel
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val recipesApiService: RecipesApiService
) : RecipesRepository {

    override suspend fun getRecipes(from: Int, size: Int): RecipesPaginationModel {
        return recipesApiService.getRecipes(from = from, size = size).toModel()
    }
}
