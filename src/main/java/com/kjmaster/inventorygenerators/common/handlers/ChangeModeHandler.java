package com.kjmaster.inventorygenerators.common.handlers;

import com.kjmaster.inventorygenerators.common.generators.IInventoryGenerator;
import com.kjmaster.inventorygenerators.common.network.ChangeModePacket;
import com.kjmaster.kjlib.KJLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ChangeModeHandler implements IMessageHandler<ChangeModePacket, IMessage> {

    @Override
    public IMessage onMessage(ChangeModePacket message, MessageContext ctx) {
        KJLib.proxy.getThreadFromContext(ctx).addScheduledTask(new Runnable() {
            @Override
            public void run() {
                processMessage(message, ctx);
            }
        });
        return null;
    }

    private void processMessage(ChangeModePacket message, MessageContext ctx) {
        EntityPlayer player = KJLib.proxy.getPlayerEntity(ctx);
        ItemStack stack = player.getHeldItemMainhand();
        Item item = stack.getItem();
        if (item instanceof IInventoryGenerator) {
            IInventoryGenerator inventoryGenerator = (IInventoryGenerator) item;
            inventoryGenerator.changeMode(stack);
        }
    }
}
