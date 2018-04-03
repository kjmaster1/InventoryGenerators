package com.kjmaster.inventorygenerators.common.generators;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public interface IInventoryGenerator {

    void giveTagCompound(ItemStack stack);

    FuelWithSlot getFuelWithSlot(EntityPlayer player);

    boolean isItemValid(ItemStack stack);

    int calculatePower(ItemStack stack);

    int calculateTime(ItemStack stack);

    boolean isInChargingMode(ItemStack stack);

    void changeMode(ItemStack stack);

    boolean isOn(ItemStack stack);

    void turnOn(ItemStack stack);

    int getBurnTime(ItemStack stack);

    void setBurnTime(ItemStack stack, int burnTime);

    ItemStack getFuel(ItemStack stack);

    void addFuel(ItemStack stack, ItemStack fuel);

    int getSlot(ItemStack stack);

    void setSlot(ItemStack stack, int slot);

    void receiveInternalEnergy(ItemStack stack, int energy);

    int getInternalEnergyStored(ItemStack stack);

    ArrayList<ItemStack> getChargeables(EntityPlayer player);

    void giveEnergyToChargeables(ArrayList<ItemStack> chargeables, ItemStack stack);

}
