package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class ItemInvFrostyGen extends ItemInventoryGenerator {

    private Item ICE = Item.getItemFromBlock(Blocks.ICE);
    private Item PACKED_ICE = Item.getItemFromBlock(Blocks.PACKED_ICE);
    private Item SNOW = Item.getItemFromBlock(Blocks.SNOW);
    private Item SNOW_LAYER = Item.getItemFromBlock(Blocks.SNOW_LAYER);

    private Item[] validItems = {ICE, PACKED_ICE, Items.SNOWBALL, SNOW, SNOW_LAYER};
    private ArrayList<Item> validItemsArrayList = new ArrayList<>(Arrays.asList(validItems));

    public ItemInvFrostyGen() {
        super("inv_frosty_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.frosty"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return validItemsArrayList.contains(stack.getItem());
    }

    @Override
    public int calculateTime(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (item.equals(SNOW_LAYER)) {
                return 500;
            } else {
                return 400;
            }
        }
        return 0;
    }

    @Override
    public int calculatePower(ItemStack stack) {
        int minSend;
        Item fuel = getFuel(stack).getItem();
        if (fuel.equals(Items.SNOWBALL)) {
            minSend = 5;
        } else if (fuel.equals(SNOW)) {
            minSend = 20;
        } else if (fuel.equals(SNOW_LAYER)) {
            minSend = 2;
        } else {
            minSend = 40;
        }
        return Math.min(getMaxEnergyStored(stack) - getInternalEnergyStored(stack),  minSend);
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
