package com.kjmaster.inventorygenerators.client.gui.generator;

import com.kjmaster.inventorygenerators.common.upgrades.SlotAutoPullUpgrade;
import com.kjmaster.inventorygenerators.common.upgrades.SlotNoEffectUpgrade;
import com.kjmaster.inventorygenerators.common.upgrades.SlotSpeedUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerGenerator extends Container {

    private final InventoryGenerator inv;

    public ContainerGenerator(InventoryPlayer playerInv, InventoryGenerator inv) {
        this.inv = inv;

        int i;
        int j;

        this.addSlotToContainer(new SlotItemHandler(inv, 0, 80, 35));
        this.addSlotToContainer(new SlotSpeedUpgrade(inv, 1, 8, 53));
        this.addSlotToContainer(new SlotAutoPullUpgrade(inv, 2, 26, 53));
        this.addSlotToContainer(new SlotNoEffectUpgrade(inv, 3, 44, 53));

        for(i = 0; i < 3; ++i)
            for(j = 0; j < 9; ++j)
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for(i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isSpectator() && player.getHeldItemMainhand() == inv.generator;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
