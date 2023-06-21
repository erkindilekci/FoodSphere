package com.erkindilekci.foodsphere.domain

import com.erkindilekci.foodsphere.domain.RecipesPaginationModel

interface RecipesRepository {

    suspend fun getRecipes(from: Int, size: Int): RecipesPaginationModel
}
