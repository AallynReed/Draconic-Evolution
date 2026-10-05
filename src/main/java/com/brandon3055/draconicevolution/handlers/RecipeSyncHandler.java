package com.brandon3055.draconicevolution.handlers;

import com.brandon3055.draconicevolution.api.DraconicAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

public class RecipeSyncHandler {
    private static RecipeMap clientRecipes = RecipeMap.EMPTY;

    public static void init() {
        NeoForge.EVENT_BUS.addListener(RecipeSyncHandler::onDatapackSync);
        NeoForge.EVENT_BUS.addListener(RecipeSyncHandler::onRecipesReceived);
    }

    private static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(DraconicAPI.FUSION_RECIPE_TYPE.get());
    }

    private static void onRecipesReceived(RecipesReceivedEvent event) {
        clientRecipes = event.getRecipeMap();
    }

    public static RecipeMap recipes(Level level) {
        return level instanceof ServerLevel serverLevel ? serverLevel.recipeAccess().recipeMap() : clientRecipes;
    }
}
