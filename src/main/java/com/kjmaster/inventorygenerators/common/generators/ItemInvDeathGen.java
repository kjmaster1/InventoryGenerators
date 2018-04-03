package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemInvDeathGen extends ItemInventoryGenerator {

    private Item BONE_BLOCK = Item.getItemFromBlock(Blocks.BONE_BLOCK);

    private Item[] validItems = {Items.BONE, Items.ROTTEN_FLESH, Items.SKULL, BONE_BLOCK};
    private ArrayList<Item> validItemsArrayList = new ArrayList<>(Arrays.asList(validItems));

    public ItemInvDeathGen() {
        super("inv_death_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.death"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !stack.isEmpty() && validItemsArrayList.contains(stack.getItem());
    }

    @Override
    public int calculateTime(ItemStack stack) {
        return 400;
    }

    @Override
    public int calculatePower(ItemStack stack) {
        int minSend = 0;
        Item fuel = getFuel(stack).getItem();
        if (fuel.equals(Items.SKULL)) {
            minSend = 150;
        } else if (fuel.equals(BONE_BLOCK)) {
            minSend = 120;
        } else if (fuel.equals(Items.BONE)) {
            minSend = 40;
        } else if (fuel.equals(Items.ROTTEN_FLESH)) {
            minSend = 20;
        }
        return Math.min(getMaxEnergyStored(stack) - getInternalEnergyStored(stack),  minSend);
    }

    @Override
    public int getSend() {
        return 150;
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
