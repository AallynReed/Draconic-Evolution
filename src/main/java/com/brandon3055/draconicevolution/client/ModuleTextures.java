package com.brandon3055.draconicevolution.client;

import codechicken.lib.gui.modular.sprite.Material;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.client.atlas.MultiModAtlasHolder;
import com.brandon3055.draconicevolution.init.DEModules;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

public class ModuleTextures {
   private static final MultiModAtlasHolder ATLAS_HOLDER = new MultiModAtlasHolder(MODID, "textures/atlas/module.png", "module", DEModules.MODULE_PROVIDING_MODS);
   private static final Map<String, Material> MATERIAL_CACHE = new HashMap<>();

   public static MultiModAtlasHolder getAtlasHolder() {
      return ATLAS_HOLDER;
   }

   protected static Material get(Identifier texture) {
      return MATERIAL_CACHE.computeIfAbsent(texture.getNamespace() + ":" + texture.getPath(), e -> getUncached(texture));
   }

   protected static Material getUncached(Identifier texture) {
      return new Material(ATLAS_HOLDER.atlasLocation(), Identifier.fromNamespaceAndPath(texture.getNamespace(), "module/" + texture.getPath()), ATLAS_HOLDER::getSprite);
   }

   public static Material get(Module<?> module) {
      return get(Objects.requireNonNull(DEModules.REGISTRY.getKey(module)));
   }
}