package com.brandon3055.draconicevolution.items.equipment;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.brandonscore.client.model.DummyHumanoidModel;
import com.brandon3055.brandonscore.client.render.EquippedItemModel;
import com.brandon3055.brandonscore.items.EquippedModelItem;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.config.ConfigProperty;
import com.brandon3055.draconicevolution.api.config.DecimalProperty;
import com.brandon3055.draconicevolution.api.modules.ModuleCategory;
import com.brandon3055.draconicevolution.api.modules.ModuleTypes;
import com.brandon3055.draconicevolution.api.modules.data.JumpData;
import com.brandon3055.draconicevolution.api.modules.data.SpeedData;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.client.model.ModularChestpieceModel;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.ModuleCfg;
import com.brandon3055.draconicevolution.init.TechProperties;
import com.brandon3055.draconicevolution.integration.equipment.EquipmentManager;
import com.brandon3055.draconicevolution.integration.equipment.IDEEquipment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by brandon3055 on 21/5/20.
 */
public class ModularChestpiece extends Item implements IModularArmor, IDEEquipment, EquippedModelItem, IModularEnergyItem {
    private final TechLevel techLevel;

    public ModularChestpiece(TechProperties props) {
        super(props.humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE));
        this.techLevel = props.getTechLevel();
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        if (entity instanceof LivingEntity && !EquipmentManager.findItem(e -> e.getItem() instanceof ModularChestpiece, (LivingEntity) entity).isEmpty()) {
            return false;
        }
        return entity.getEquipmentSlotForItem(stack) == armorType;
    }

    @Override
    public boolean canEquip(ItemStack stack, LivingEntity livingEntity, String slotID) {
        if (!slotID.equals("body") || !EquipmentManager.findItem(e -> e.getItem() instanceof ModularChestpiece, livingEntity).isEmpty()) {
            return false;
        }

        return !(livingEntity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ModularChestpiece);
    }

    @Override
    public TechLevel getTechLevel() {
        return techLevel;
    }

    @Override
    public @NotNull ModuleHostImpl instantiateHost(ItemStack stack) {
        ModuleHostImpl host = new ModuleHostImpl(techLevel, ModuleCfg.chestpieceWidth(techLevel), ModuleCfg.chestpieceHeight(techLevel), "chestpiece", ModuleCfg.removeInvalidModules);
        host.addCategories(ModuleCategory.CHESTPIECE);
        host.addPropertyBuilder(props -> {
            SpeedData speed = host.getModuleData(ModuleTypes.SPEED);
            if (speed != null) {
                SpeedData data = host.getModuleData(ModuleTypes.SPEED);
                double maxSpeed = data == null ? 0 : data.speedMultiplier();
                if (DEConfig.armorSpeedLimit != -1) {
                    maxSpeed = Math.min(maxSpeed, DEConfig.armorSpeedLimit);
                }

                //TODO Do these still work properly now that we are not using dynamic getters? Props are regenerated on data load so it should be fine?
                props.add(new DecimalProperty("walk_speed", 0).min(0).max(maxSpeed).setFormatter(ConfigProperty.DecimalFormatter.PLUS_PERCENT_0));
                props.add(new DecimalProperty("run_speed", maxSpeed).min(0).max(maxSpeed).setFormatter(ConfigProperty.DecimalFormatter.PLUS_PERCENT_0));
            }

            JumpData jump = host.getModuleData(ModuleTypes.JUMP_BOOST);
            if (jump != null) {
                JumpData data = host.getModuleData(ModuleTypes.JUMP_BOOST);
                double jumpSetting =  data == null ? 0 : data.multiplier();

                //TODO Do these still work properly now that we are not using dynamic getters? Props are regenerated on data load so it should be fine?
                props.add(new DecimalProperty("jump_boost_run", jumpSetting).min(0).max(jumpSetting).setFormatter(ConfigProperty.DecimalFormatter.PLUS_PERCENT_0));
                props.add(new DecimalProperty("jump_boost", jumpSetting).min(0).max(jumpSetting).setFormatter(ConfigProperty.DecimalFormatter.PLUS_PERCENT_0));
            }
        });
        return host;
    }

    @Override
    public @NotNull ModularOPStorage instantiateOPStorage(ItemStack stack, Supplier<ModuleHost> hostSupplier) {
        return new ModularOPStorage(hostSupplier, EquipCfg.getBaseChestpieceEnergy(techLevel), EquipCfg.getBaseChestpieceTransfer(techLevel));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flagIn) {
        List<Component> tooltip = new ArrayList<>();
        addModularItemInformation(stack, context, tooltip, flagIn);
        tooltip.forEach(builder);
    }

    @OnlyIn(Dist.CLIENT)
    private ModularChestpieceModel<?> model;

    @OnlyIn(Dist.CLIENT)
    private ModularChestpieceModel<?> model_on_armor;

    @Override
    @OnlyIn(Dist.CLIENT)
    public EquippedItemModel getExtendedModel(LivingEntity entity, ItemStack stack, @Nullable EquipmentSlot slot, HumanoidModel<?> parentModel, boolean slim) {
        return Client.getExtendedModel(this, entity, slot, parentModel);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return ArmorMaterials.DIAMOND.createAttributes(ArmorType.CHESTPLATE);
    }

    @Override
    public boolean canBeHurtBy(ItemStack stack, DamageSource source) {
        return source.is(DamageTypes.FELL_OUT_OF_WORLD);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (entity.getAge() >= 0 && entity.pickupDelay != 32767) {
            entity.setExtendedLifetime();
        }
        return super.onEntityItemUpdate(stack, entity);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return damageBarVisible(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return damageBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return damageBarColour(stack);
    }

    private static class Client {
        private static EquippedItemModel getExtendedModel(ModularChestpiece item, LivingEntity entity, @Nullable EquipmentSlot slot, HumanoidModel<?> parentModel) {
            ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
            boolean onArmor = slot == null && !chest.isEmpty() && HumanoidArmorLayer.shouldRender(chest, EquipmentSlot.CHEST) && !chest.has(DataComponents.GLIDER);
            if (item.model == null || item.model_on_armor == null) {
                item.model = new ModularChestpieceModel<>(item.techLevel, false);
                item.model_on_armor = new ModularChestpieceModel<>(item.techLevel, true);
            }
            ModularChestpieceModel<?> activeModel = onArmor ? item.model_on_armor : item.model;
            ClientHooks.copyModelProperties(parentModel, activeModel);
            return activeModel;
        }
    }
}
