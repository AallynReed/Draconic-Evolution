package com.brandon3055.draconicevolution.client;

import codechicken.lib.gui.modular.SpriteSupplier;
import com.brandon3055.draconicevolution.api.modules.Module;
import com.brandon3055.draconicevolution.init.DEModules;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterTextureAtlasesEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.brandon3055.draconicevolution.DraconicEvolution.MODID;

public class ModuleTextures {
   public static final Identifier ATLAS = Identifier.fromNamespaceAndPath(MODID, "module");
   public static final Identifier ATLAS_TEXTURE = Identifier.fromNamespaceAndPath(MODID, "textures/atlas/module.png");
   private static final Map<String, SpriteSupplier> MATERIAL_CACHE = new HashMap<>();

   public static void init(IEventBus modBus) {
      modBus.addListener(ModuleTextures::registerAtlas);
   }

   private static void registerAtlas(RegisterTextureAtlasesEvent event) {
      event.register(new AtlasManager.AtlasConfig(ATLAS_TEXTURE, ATLAS, false));
   }

   protected static SpriteSupplier get(Identifier texture) {
      return MATERIAL_CACHE.computeIfAbsent(texture.getNamespace() + ":" + texture.getPath(), e -> getUncached(texture));
   }

   protected static SpriteSupplier getUncached(Identifier texture) {
      return SpriteSupplier.of(() -> new SpriteId(ATLAS, Identifier.fromNamespaceAndPath(texture.getNamespace(), "module/" + texture.getPath())));
   }

   public static SpriteSupplier get(Module<?> module) {
      return get(Objects.requireNonNull(DEModules.REGISTRY.getKey(module)));
   }
}
