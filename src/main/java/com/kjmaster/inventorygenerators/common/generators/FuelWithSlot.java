package com.kjmaster.inventorygenerators.common.generators;

import net.minecraft.item.ItemStack;

public class FuelWithSlot {

    private final ItemStack fuel;
    private final int slot;

    public FuelWithSlot(ItemStack fuel, int slot) {
        this.fuel = fuel;
        this.slot = slot;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public int getSlot() {
        return slot;
    }
}
