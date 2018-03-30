package com.kjmaster.inventorygenerators.client;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.common.CommonProxy;
import com.kjmaster.inventorygenerators.common.init.InitModGenerators;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = InventoryGenerators.MODID)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void onModelEvent(ModelRegistryEvent event) {
        InitModGenerators.registerModels();
    }
}
