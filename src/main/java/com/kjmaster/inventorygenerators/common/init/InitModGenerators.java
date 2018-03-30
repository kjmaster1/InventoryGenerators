package com.kjmaster.inventorygenerators.common.init;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.client.IHasModel;
import com.kjmaster.inventorygenerators.common.generators.ItemInvCulinaryGen;
import com.kjmaster.inventorygenerators.common.generators.ItemInvEndGen;
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

    public static final Item invEndGen = new ItemInvEndGen();

    @Mod.EventBusSubscriber(modid = InventoryGenerators.MODID)
    public static class RegistrationHandler {
        public static final Set<Item> ITEMS = new HashSet<>();

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final Item[] items = {
                    invCulinaryGen,
                    invEndGen,
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
                invEndGen,
        };

        for (Item item : items) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).initModel();
            }
        }
    }
}
