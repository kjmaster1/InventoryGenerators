package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInvNetherStarGen extends ItemInventoryGenerator {

    public ItemInvNetherStarGen() {
        super("inv_nether_star_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.netherstar"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().equals(Items.NETHER_STAR);
    }

    @Override
    public int calculateTime(ItemStack stack) {
        return 2400;
    }

    @Override
    public int getSend() {
        return 4000;
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return 400000;
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
