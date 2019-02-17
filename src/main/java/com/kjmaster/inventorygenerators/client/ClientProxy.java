package com.kjmaster.inventorygenerators.client;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.common.CommonProxy;
import com.kjmaster.inventorygenerators.common.init.InitModGenerators;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = InventoryGenerators.MODID)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void onModelEvent(ModelRegistryEvent event) {
        InitModGenerators.registerModels();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player == null) {
            return;
        }
        UUID uniqueID = player.getUniqueID();
        if (uniqueID.toString().equals("d51754f4-b279-4b18-a536-264bf3b02440") || player.getName().toLowerCase().equals("sjin")) {
            if (player.world.rand.nextInt(100001) == 0) {
                player.world.playSound(player, player.posX - 1, player.posY - 1, player.posZ -1, SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1, 1);
            }
        }
    }
}
