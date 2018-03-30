package com.kjmaster.inventorygenerators.common.generators;

import net.minecraft.item.ItemStack;

public class ItemInvCulinaryGen extends ItemInventoryGenerator {

    public ItemInvCulinaryGen() {
        super("inv_culinary_gen");
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack);
    }

    @Override
    public int calculateTime(ItemStack stack) {
        return super.calculateTime(stack);
    }

    @Override
    public int calculatePower(ItemStack stack) {
        return super.calculatePower(stack);
    }
}