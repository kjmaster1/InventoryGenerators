package com.kjmaster.inventorygenerators.client.gui.generator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class InventoryGenerator implements IItemHandlerModifiable {

    private final IItemHandlerModifiable generatorInv;
    final ItemStack generator;

    public InventoryGenerator(ItemStack generator) {
        this.generator = generator;
        this.generatorInv = (IItemHandlerModifiable) generator.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        generatorInv.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return generatorInv.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return generatorInv.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return generatorInv.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return generatorInv.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return generatorInv.getSlotLimit(slot);
    }
}
