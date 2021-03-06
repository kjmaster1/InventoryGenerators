package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.common.network.ModGuiHandler;
import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInvOverclockedGen extends ItemInventoryGenerator {

    public ItemInvOverclockedGen() {
        super("inv_overclocked_gen");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.overclocked"));
        addMoreInformation(stack, tooltip);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return TileEntityFurnace.getItemBurnTime(stack) > 0;
    }

    @Override
    public int calculateTime(ItemStack stack) {
        if (!stack.isEmpty()) {
            return  (int) Math.ceil(TileEntityFurnace.getItemBurnTime(stack) / 4000) + 1;
        }
        return 0;
    }

    @Override
    public int calculatePower(ItemStack stack) {
        int minSend = TileEntityFurnace.getItemBurnTime(getFuel(stack)) < 4000 ? TileEntityFurnace.getItemBurnTime(getFuel(stack)) : 4000;
        return Math.min(getMaxEnergyStored(stack) - getInternalEnergyStored(stack),  minSend);
    }

    @Override
    public int getSend() {
        return 4000;
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return 1000000;
    }

    @Override
    public void initModel() {
        super.initModel();
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        super.onItemRightClick(world, player, hand);
        if (!player.isSneaking()) {
            player.openGui(InventoryGenerators.instance, ModGuiHandler.overclocked, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        ItemStack stack = player.getHeldItem(hand);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}
