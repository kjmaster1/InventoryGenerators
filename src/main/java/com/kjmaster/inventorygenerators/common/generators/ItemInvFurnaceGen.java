package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInvFurnaceGen extends ItemInventoryGenerator {

    public ItemInvFurnaceGen() {
        super("inv_furnace_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.0"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return TileEntityFurnace.getItemBurnTime(stack) > 0;
    }

    @Override
    public int calculateTime(ItemStack stack) {
        return (int) Math.ceil(TileEntityFurnace.getItemBurnTime(stack) / 4);
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
