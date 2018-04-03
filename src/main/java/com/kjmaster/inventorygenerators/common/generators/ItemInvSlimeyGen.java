package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.inventorygenerators.actuallyadditions.RiceBallCheck;
import com.kjmaster.inventorygenerators.common.CommonProxy;
import com.kjmaster.inventorygenerators.tinkers.SlimeballChecks;
import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInvSlimeyGen extends ItemInventoryGenerator {

    public ItemInvSlimeyGen() {
        super("inv_slimey_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.slimey"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        Item item = stack.getItem();
        return !stack.isEmpty() && CommonProxy.slimeThings.contains(item);
    }

    @Override
    public int calculateTime(ItemStack stack) {
        return !stack.isEmpty() ? 20 : 0;
    }

    @Override
    public int calculatePower(ItemStack stack) {
        int minSend = 40;
        Item fuel = getFuel(stack).getItem();
        if (Loader.isModLoaded("tconstruct")) {
            if (SlimeballChecks.isBlueOrOrange(fuel)) {
                minSend = 40;
            } else if (SlimeballChecks.isBlood(fuel)) {
                minSend = 120;
            } else if (SlimeballChecks.isPurple(fuel)) {
                minSend = 120;
            }
        }
        if (Loader.isModLoaded("actuallyadditions")) {
            if (RiceBallCheck.isRiceball(fuel)) {
                minSend = 40;
            }
        }
        if (fuel instanceof ItemBlock) {
            minSend = 360;
        }
        if (stack.isEmpty()) {
            minSend = 0;
        }
        return Math.min(getMaxEnergyStored(stack) - getInternalEnergyStored(stack),  minSend);
    }

    @Override
    public int getSend() {
        return 360;
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
