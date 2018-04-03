package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInvPotionGen extends ItemInventoryGenerator {

    public ItemInvPotionGen() {
        super("inv_potion_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.potion"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        Item item = stack.getItem();
        return !stack.isEmpty() && item instanceof ItemPotion;
    }

    @Override
    public int calculateTime(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (item.equals(Items.POTIONITEM)) {
                return 80;
            } else if (item.equals(Items.SPLASH_POTION)) {
                return 160;
            } else if (item.equals(Items.LINGERING_POTION)) {
                return 320;
            }
        }
        return 0;
    }

    @Override
    public int calculatePower(ItemStack stack) {
        int minSend = 0;
        Item fuel = getFuel(stack).getItem();
        if (fuel.equals(Items.POTIONITEM)) {
            minSend = 80;
        } else if (fuel.equals(Items.SPLASH_POTION)) {
            minSend = 160;
        } else if (fuel.equals(Items.LINGERING_POTION)) {
            minSend = 320;
        }
        return Math.min(getMaxEnergyStored(stack) - getInternalEnergyStored(stack),  minSend);
    }

    @Override
    public int getSend() {
        return 320;
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
