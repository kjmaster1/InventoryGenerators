package com.kjmaster.inventorygenerators;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = InventoryGenerators.MODID, name = InventoryGenerators.NAME, version = InventoryGenerators.VERSION)
public class InventoryGenerators
{
    public static final String MODID = "inventorygenerators";
    public static final String NAME = "Inventory Generators";
    public static final String VERSION = "1.0.0";

    public static Logger LOGGER;

    public static CreativeTabs invGenTab = new CreativeTabs("invgens") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.COAL);
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
