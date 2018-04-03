package com.kjmaster.inventorygenerators;

import com.kjmaster.inventorygenerators.common.CommonProxy;
import com.kjmaster.inventorygenerators.common.init.InitModGenerators;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = InventoryGenerators.MODID, name = InventoryGenerators.NAME, version = InventoryGenerators.VERSION, dependencies = InventoryGenerators.DEPENDENCIES)
public class InventoryGenerators
{
    public static final String MODID = "inventorygenerators";
    public static final String NAME = "Inventory Generators";
    public static final String VERSION = "1.0.0";
    public static final String DEPENDENCIES = "required-after:tconstruct;required-after:actuallyadditions;required-after:kjlib";
    public static Logger LOGGER;

    @SidedProxy(clientSide = "com.kjmaster.inventorygenerators.client.ClientProxy", serverSide = "com.kjmaster.inventorygenerators.common.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs invGenTab = new CreativeTabs("invgens") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(InitModGenerators.invFurnaceGen);
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerPackets();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        CommonProxy.findPinkThings();
        CommonProxy.findSlimeThings();
    }
}
