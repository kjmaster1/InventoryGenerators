package com.kjmaster.inventorygenerators.common.upgrades;

import com.kjmaster.inventorygenerators.common.init.InitModGenerators;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotSpeedUpgrade extends SlotItemHandler {

    public SlotSpeedUpgrade(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return super.isItemValid(stack) && stack.getItem().equals(InitModGenerators.speedUpgrade);
    }
}
