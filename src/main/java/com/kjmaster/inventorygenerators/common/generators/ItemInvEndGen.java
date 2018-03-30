package com.kjmaster.inventorygenerators.common.generators;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemInvEndGen extends ItemInventoryGenerator {

    public ItemInvEndGen() {
        super("inv_end_gen");
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        Item item = stack.getItem();
        return item.equals(Items.ENDER_PEARL) || item.equals(Items.ENDER_EYE);
    }

    @Override
    public int calculateTime(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (item.equals(Items.ENDER_PEARL)) {
                return 1600;
            } else if (item.equals(Items.ENDER_EYE)) {
                return 3200;
            }
        }
        return 0;
    }

    @Override
    public int calculatePower(ItemStack stack) {
        return Math.min(getMaxEnergyStored(stack) - getInternalEnergyStored(stack),  calculateTime(getFuel(stack)) / 40);
    }

    @Override
    public void initModel() {
        super.initModel();
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }
}
