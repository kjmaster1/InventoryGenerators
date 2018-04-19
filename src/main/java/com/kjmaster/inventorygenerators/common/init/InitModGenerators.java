package com.kjmaster.inventorygenerators.common.init;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.client.IHasModel;
import com.kjmaster.inventorygenerators.common.generators.*;
import com.kjmaster.inventorygenerators.common.upgrades.ItemAutoPullUpgrade;
import com.kjmaster.inventorygenerators.common.upgrades.ItemNoEffectUpgrade;
import com.kjmaster.inventorygenerators.common.upgrades.ItemSpeedUpgrade;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

public class InitModGenerators {

    public static final Item invCulinaryGen = new ItemInvCulinaryGen();

    public static final Item invDeathGen = new ItemInvDeathGen();

    public static final Item invEndGen = new ItemInvEndGen();

    public static final Item invExplosiveGen = new ItemInvExplosiveGen();

    public static final Item invFurnaceGen = new ItemInvFurnaceGen();

    public static final Item invHalitosisGen = new ItemInvHalitosisGen();

    public static final Item invFrostyGen = new ItemInvFrostyGen();

    public static final Item invMagmaticGen = new ItemInvMagmaticGen();

    public static final Item invNetherStarGen = new ItemInvNetherStarGen();

    public static final Item invOverclockedGen = new ItemInvOverclockedGen();

    public static final Item invPinkGen = new ItemInvPinkGen();

    public static final Item invPotionGen = new ItemInvPotionGen();

    public static final Item invSlimeyGen = new ItemInvSlimeyGen();

    public static final Item invSurvivalistGen = new ItemInvSurvivalistGen();

    public static final Item autoPullUpgrade = new ItemAutoPullUpgrade();

    public static final Item noEffectUpgrade = new ItemNoEffectUpgrade();

    public static final Item speedUpgrade = new ItemSpeedUpgrade();

    @Mod.EventBusSubscriber(modid = InventoryGenerators.MODID)
    public static class RegistrationHandler {
        public static final Set<Item> ITEMS = new HashSet<>();

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final Item[] items = {
                    invCulinaryGen,
                    invDeathGen,
                    invEndGen,
                    invExplosiveGen,
                    invFurnaceGen,
                    invHalitosisGen,
                    invFrostyGen,
                    invMagmaticGen,
                    invNetherStarGen,
                    invOverclockedGen,
                    invPinkGen,
                    invPotionGen,
                    invSlimeyGen,
                    invSurvivalistGen,
                    autoPullUpgrade,
                    noEffectUpgrade,
                    speedUpgrade,
            };
            final IForgeRegistry<Item> registry = event.getRegistry();
            for (final Item item : items) {
                registry.register(item);
                ITEMS.add(item);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModels() {
        final Item[] items = {
                invCulinaryGen,
                invDeathGen,
                invEndGen,
                invExplosiveGen,
                invFurnaceGen,
                invHalitosisGen,
                invFrostyGen,
                invMagmaticGen,
                invNetherStarGen,
                invOverclockedGen,
                invPinkGen,
                invPotionGen,
                invSlimeyGen,
                invSurvivalistGen,
                autoPullUpgrade,
                noEffectUpgrade,
                speedUpgrade,
        };

        for (Item item : items) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).initModel();
            }
        }
    }
}
