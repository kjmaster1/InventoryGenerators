package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.kjlib.common.energy.IItemEnergy;
import com.kjmaster.kjlib.common.energy.InvEnergyStorage;
import com.kjmaster.kjlib.common.items.ItemBase;
import com.kjmaster.kjlib.utils.CapabilityUtils;
import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemInventoryGenerator extends ItemBase implements IInventoryGenerator, IItemEnergy {

    public ItemInventoryGenerator(String name) {
        super(name, InventoryGenerators.invGenTab, 1);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityPlayer && !world.isRemote && stack.getItem() instanceof IInventoryGenerator) {
            IInventoryGenerator inventoryGenerator = (IInventoryGenerator) stack.getItem();
            if (!stack.hasTagCompound()) {
                inventoryGenerator.giveTagCompound(stack);
            }
            if (inventoryGenerator.isOn(stack)) {
                EntityPlayer player = (EntityPlayer) entity;
                if (inventoryGenerator.getBurnTime(stack) <= 0) {
                    FuelWithSlot fuelWithSlot = inventoryGenerator.getFuelWithSlot(player);
                    ItemStack fuel = fuelWithSlot.getFuel();
                    int slot = fuelWithSlot.getSlot();
                    inventoryGenerator.setBurnTime(stack, inventoryGenerator.calculateTime(fuel));
                    inventoryGenerator.setSlot(stack, slot);
                } else {
                    inventoryGenerator.setBurnTime(stack, inventoryGenerator.getBurnTime(stack) - 1);
                    int rfToGive = inventoryGenerator.calculatePower(stack);
                    inventoryGenerator.receiveInternalEnergy(stack, rfToGive);

                }
                if (inventoryGenerator.isInChargingMode(stack)) {
                    ArrayList<ItemStack> chargeables = inventoryGenerator.getChargeables(player);
                    inventoryGenerator.giveEnergyToChargeables(chargeables, stack);
                }
                inventoryGenerator.consumeItem(stack, player);
            }
        }
    }

    @Override
    public void giveTagCompound(ItemStack stack) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        NBTTagCompound fuelTagCompound = ItemStack.EMPTY.writeToNBT(new NBTTagCompound());
        nbtTagCompound.setTag("Fuel", fuelTagCompound);
        nbtTagCompound.setBoolean("On", false);
        nbtTagCompound.setBoolean("Charging", false);
        nbtTagCompound.setInteger("Energy", 0);
        nbtTagCompound.setInteger("BurnTime", 0);
        nbtTagCompound.setInteger("Slot", 0);
        stack.setTagCompound(nbtTagCompound);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) {
            turnOn(stack);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new InvEnergyStorage(getMaxEnergyStored(stack), getReceive(), getSend(), stack, this);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        tooltip.add(StringHelper.getInfoText("info.invgens.0"));

        if (isInChargingMode(stack)) {
            tooltip.add(StringHelper.getNoticeText("info.invgens.1"));
            tooltip.add(StringHelper.getDeactivationText("info.invgens.2"));
        } else {
            tooltip.add(StringHelper.getActivationText("info.invgens.3"));
        }

        tooltip.add(StringHelper.localize("info.invgens.charge") + ": " + StringHelper.getScaledNumber(getInternalEnergyStored(stack)) + " / " + StringHelper.getScaledNumber(getMaxEnergyStored(stack)) + " RF");
        tooltip.add(StringHelper.localize("info.invgens.send") + "/" + StringHelper.localize("info.invgens.receive") + ": " + StringHelper.formatNumber(getSend()) + "/" + StringHelper.formatNumber(getReceive()) + " RF/t");
        tooltip.add(StringHelper.localize("info.invgens.burnTimeLeft") + ": " + StringHelper.formatNumber(getBurnTime(stack)));
    }

    @Override
    public void consumeItem(ItemStack stack, EntityPlayer player) {
        ItemStack fuel = getFuel(stack);
        if (getBurnTime(stack) == 0 && !fuel.isEmpty() && getInternalEnergyStored(stack) < getMaxEnergyStored(stack)) {
            addFuel(stack, fuel);
            int slot = getSlot(stack);
            ItemStack fuel2 = player.inventory.getStackInSlot(slot);
            if (ItemStack.areItemStacksEqual(fuel, fuel2)) {
                fuel2.shrink(1);
                fuel.shrink(1);
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getChargeables(EntityPlayer player) {
        ArrayList<ItemStack> chargeables = new ArrayList<>();
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack invStack = player.inventory.getStackInSlot(i);
                IEnergyStorage energyStorage = CapabilityUtils.getCapability(invStack, CapabilityEnergy.ENERGY, null);
                if (energyStorage != null && energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                    chargeables.add(invStack);
                }
        }
        return chargeables;
    }

    @Override
    public void giveEnergyToChargeables(ArrayList<ItemStack> chargeables, ItemStack stack) {
        for (ItemStack chargeableStack : chargeables) {
            IEnergyStorage storage = CapabilityUtils.getCapability(chargeableStack, CapabilityEnergy.ENERGY, null);
            int energySent = storage.receiveEnergy(getInternalEnergyStored(stack) / chargeables.size(), false);
            extractEnergy(stack, energySent, false);
        }
        chargeables.clear();
    }

    @Override
    public void receiveInternalEnergy(ItemStack stack, int energy) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("Energy")) {
                int stored = getInternalEnergyStored(stack);
                int newEnergy = stored + energy;
                nbtTagCompound.setInteger("Energy", newEnergy);
            }
        }
    }

    @Override
    public int getInternalEnergyStored(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if (tagCompound.hasKey("Energy")) {
                return tagCompound.getInteger("Energy");
            }
        }
        return 0;
    }

    @Override
    public FuelWithSlot getFuelWithSlot(EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (isItemValid(stack)) {
                return new FuelWithSlot(stack, i);
            }
        }
        return new FuelWithSlot(ItemStack.EMPTY, 0);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public int calculateTime(ItemStack stack) {
        return 0;
    }

    @Override
    public int calculatePower(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isInChargingMode(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("Charging")) {
                return nbtTagCompound.getBoolean("Charging");
            }
        }
        return false;
    }

    @Override
    public void changeMode(ItemStack stack) {
        if (isInChargingMode(stack)) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            nbtTagCompound.setBoolean("Charging", false);
        } else {
            if (stack.hasTagCompound()) {
                NBTTagCompound tagCompound = stack.getTagCompound();
                tagCompound.setBoolean("Charging", true);
            }
        }
    }

    @Override
    public boolean isOn(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("On")) {
                return nbtTagCompound.getBoolean("On");
            }
        }
        return false;
    }

    @Override
    public void turnOn(ItemStack stack) {
        if (isOn(stack)) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            nbtTagCompound.setBoolean("On", false);
        } else {
            if (stack.hasTagCompound()) {
                NBTTagCompound tagCompound = stack.getTagCompound();
                tagCompound.setBoolean("On", true);
            }
        }
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("BurnTime")) {
                return nbtTagCompound.getInteger("BurnTime");
            }
        }
        return 0;
    }

    @Override
    public void setBurnTime(ItemStack stack, int burnTime) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("BurnTime")) {
                nbtTagCompound.setInteger("BurnTime", burnTime);
            }
        }
    }

    @Override
    public ItemStack getFuel(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("Fuel")) {
                NBTTagCompound fuelTagCompound = nbtTagCompound.getCompoundTag("Fuel");
                return new ItemStack(fuelTagCompound);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void addFuel(ItemStack stack, ItemStack fuel) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("Fuel")) {
                NBTTagCompound fuelTagCompound = fuel.writeToNBT(new NBTTagCompound());
                nbtTagCompound.setTag("Fuel", fuelTagCompound);
            }
        }
    }

    @Override
    public int getSlot(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("Slot")) {
                return nbtTagCompound.getInteger("Slot");
            }
        }
        return 0;
    }

    @Override
    public void setSlot(ItemStack stack, int slot) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("Slot")) {
                nbtTagCompound.setInteger("Slot", slot);
            }
        }
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return 10000;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.getTagCompound() == null) {
            NBTTagCompound newTagCompound = new NBTTagCompound();
            newTagCompound.setInteger("Energy", 0);
            container.setTagCompound(newTagCompound);
        }
        int energyExtracted = Math.min(this.getInternalEnergyStored(container), Math.min(this.getMaxEnergyStored(container), maxExtract));
        if (!simulate) {
            int stored = this.getInternalEnergyStored(container);
            stored -= energyExtracted;
            container.getTagCompound().setInteger("Energy", stored);
        }

        return energyExtracted;
    }

    public int getSend() {
        return 40;
    }

    public int getReceive() {
        return 0;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && (slotChanged
                || getInternalEnergyStored(oldStack) > 0 != getInternalEnergyStored(newStack) > 0);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        super.hasEffect(stack);
        return isOn(stack);
    }
}