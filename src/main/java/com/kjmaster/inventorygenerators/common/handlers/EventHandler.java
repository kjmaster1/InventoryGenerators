package com.kjmaster.inventorygenerators.common.handlers;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.common.generators.IInventoryGenerator;
import com.kjmaster.kjlib.common.container.InventoryUtils;
import com.kjmaster.kjlib.utils.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber(modid = InventoryGenerators.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.world.isRemote) {
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                Item item = stack.getItem();
                if (item instanceof IInventoryGenerator) {
                    IInventoryGenerator generator = (IInventoryGenerator) item;
                    IItemHandler inv = CapabilityUtils.getCapability(stack, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                    ItemStack autoPullStack = inv.getStackInSlot(2);
                    if (!autoPullStack.isEmpty() && generator.isItemValid(event.getItem().getItem())) {
                        ItemStack result = inv.insertItem(0, event.getItem().getItem(), false);
                        int numPickedUp = event.getItem().getItem().getCount() - result.getCount();

                        event.getItem().setItem(result);
                        if (numPickedUp > 0) {
                            event.setCanceled(true);
                            if (!event.getItem().isSilent()) {
                                event.getItem().world.playSound(null, event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ,
                                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F,
                                        ((event.getItem().world.rand.nextFloat() - event.getItem().world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                            }
                            ((EntityPlayerMP) event.getEntityPlayer()).connection.sendPacket(new SPacketCollectItem(event.getItem().getEntityId(), event.getEntityPlayer().getEntityId(), numPickedUp));
                            event.getEntityPlayer().openContainer.detectAndSendChanges();
                            return;
                        }
                    }
                }
            }
        }
    }
}
