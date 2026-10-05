package com.brandon3055.draconicevolution.items.equipment;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.api.IReaperItem;
import com.brandon3055.draconicevolution.api.capability.ModuleHost;
import com.brandon3055.draconicevolution.api.modules.lib.ModularOPStorage;
import com.brandon3055.draconicevolution.api.modules.lib.ModuleHostImpl;
import com.brandon3055.draconicevolution.init.EquipCfg;
import com.brandon3055.draconicevolution.init.ModuleCfg;
import com.brandon3055.draconicevolution.init.TechProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.function.Consumer;

/**
 * Created by brandon3055 on 21/5/20.
 */
public class ModularSword extends Item implements IReaperItem, IModularMelee, IModularEnergyItem {
    private final TechLevel techLevel;
    private final DETier itemTier;

    public ModularSword(DETier tier, TechProperties props) {
        super(props.sword(tier.toolMaterial(), 0, 0));
        this.techLevel = props.getTechLevel();
        this.itemTier = tier;
    }

    @Override
    public TechLevel getTechLevel() {
        return techLevel;
    }

    @Override
    public DETier getItemTier() {
        return itemTier;
    }

    @Override
    public double getSwingSpeedMultiplier() {
        return EquipCfg.swordSwingSpeedMultiplier;
    }

    @Override
    public double getDamageMultiplier() {
        return EquipCfg.swordDamageMultiplier;
    }

    @Override
    public @NotNull ModuleHostImpl instantiateHost(ItemStack stack) {
        ModuleHostImpl host = new ModuleHostImpl(techLevel, ModuleCfg.toolWidth(techLevel), ModuleCfg.toolHeight(techLevel), "sword", ModuleCfg.removeInvalidModules);
        return host;
    }

    @Override
    public @NotNull ModularOPStorage instantiateOPStorage(ItemStack stack, Supplier<ModuleHost> hostSupplier) {
        return new ModularOPStorage(hostSupplier, EquipCfg.getBaseToolEnergy(techLevel), EquipCfg.getBaseToolTransfer(techLevel));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flagIn) {
        List<Component> tooltip = new ArrayList<>();
        addModularItemInformation(stack, context, tooltip, flagIn);
        tooltip.forEach(builder);
    }

    @Override
    public int getReaperLevel(ItemStack stack) {
        return techLevel.index;
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

}
