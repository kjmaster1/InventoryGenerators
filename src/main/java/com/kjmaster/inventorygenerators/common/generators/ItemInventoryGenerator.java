package com.kjmaster.inventorygenerators.common.generators;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.client.IHasModel;
import com.kjmaster.inventorygenerators.client.KeyHandler;
import com.kjmaster.inventorygenerators.common.init.InitModGenerators;
import com.kjmaster.kjlib.common.energy.IItemEnergy;
import com.kjmaster.kjlib.common.energy.InvEnergyStorage;
import com.kjmaster.kjlib.common.items.ItemBase;
import com.kjmaster.kjlib.utils.CapabilityUtils;
import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemInventoryGenerator extends ItemBase implements IInventoryGenerator, IItemEnergy, IHasModel {

    public ItemInventoryGenerator(String name) {
        super(name, InventoryGenerators.invGenTab, 1);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelResourceLocation onModel = new ModelResourceLocation(getRegistryName() + "_on", "inventory");
        ModelResourceLocation offModel = new ModelResourceLocation(getRegistryName() + "_off", "inventory");

        ModelBakery.registerItemVariants(this, onModel, offModel);

        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                if (isOn(stack) && getBurnTime(stack) > 0) {
                    return onModel;
                } else {
                    return offModel;
                }
            }
        });
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
    }


    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof EntityPlayer && !world.isRemote) {
            IInventoryGenerator inventoryGenerator = (IInventoryGenerator) stack.getItem();
            if (!stack.hasTagCompound()) {
                giveTagCompound(stack);
            }
            if (inventoryGenerator.getBurnTime(stack) < 0) {
                inventoryGenerator.setBurnTime(stack, 0);
            }
            if (inventoryGenerator.isOn(stack)) {
                EntityPlayer player = (EntityPlayer) entity;
                IItemHandler inv = CapabilityUtils.getCapability(stack, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (inv == null) {
                    return;
                }
                ItemStack speedUpgradeStack = inv.getStackInSlot(1);
                int numSpeedUpgrades = speedUpgradeStack.getCount();
                for (int i = 0; i <= numSpeedUpgrades; i++) {
                    if (inventoryGenerator.getBurnTime(stack) <= 0 && !inventoryGenerator.getFuel(stack).isEmpty()
                            && !(getInternalEnergyStored(stack) == getMaxEnergyStored(stack))) {
                        ItemStack fuel = getFuel(stack);
                        inventoryGenerator.setBurnTime(stack, inventoryGenerator.calculateTime(fuel));
                        fuel.shrink(1);
                    } else if (!(getInternalEnergyStored(stack) == getMaxEnergyStored(stack)) && inventoryGenerator.getBurnTime(stack) > 0) {
                        inventoryGenerator.setBurnTime(stack, inventoryGenerator.getBurnTime(stack) - 1);
                        int rfToGive = inventoryGenerator.calculatePower(stack);
                        inventoryGenerator.receiveInternalEnergy(stack, rfToGive);

                    }
                    if (inventoryGenerator.isInChargingMode(stack)) {
                        ArrayList<ItemStack> chargeables = inventoryGenerator.getChargeables(player);
                        inventoryGenerator.giveEnergyToChargeables(chargeables, stack);
                    }
                }
                if (inv.getStackInSlot(3).isEmpty() && hasSideEffect() && inventoryGenerator.getBurnTime(stack) > 0) {
                    giveSideEffect(player);
                }
            }
        }
    }

    public void giveSideEffect(EntityPlayer player) {
    }

    public boolean hasSideEffect() {
        return false;
    }

    @Override
    public void giveTagCompound(ItemStack stack) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setBoolean("On", false);
        nbtTagCompound.setBoolean("Charging", false);
        nbtTagCompound.setInteger("Energy", 0);
        nbtTagCompound.setInteger("BurnTime", 0);
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
        return new InvEnergyProvider(stack, this);
    }

    private class InvEnergyProvider implements ICapabilitySerializable<NBTBase> {

        private final IItemHandler inv;
        private final InvEnergyStorage energyStorage;

        InvEnergyProvider(ItemStack stack, IItemEnergy iItemEnergy) {
            this.energyStorage = new InvEnergyStorage(getMaxEnergyStored(stack), getReceive(), getSend(), stack, iItemEnergy);
            this.inv = new ItemStackHandler(4) {

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                    Item item = stack.getItem();
                    boolean isSpeedUpgrade = item.equals(InitModGenerators.speedUpgrade);
                    boolean isAutoPullUpgrade = item.equals(InitModGenerators.autoPullUpgrade);
                    boolean isNoEffectUpgrade = item.equals(InitModGenerators.noEffectUpgrade);
                    if (slot == 0 && !stack.isEmpty() && ItemInventoryGenerator.this.isItemValid(stack)) {
                        return super.insertItem(slot, stack, simulate);
                    }
                    if (slot == 1 && !stack.isEmpty() && isSpeedUpgrade) {
                        return super.insertItem(slot, stack, simulate);
                    }
                    if (slot == 2 && !stack.isEmpty() && isAutoPullUpgrade) {
                        return super.insertItem(slot, stack, simulate);
                    }
                    if (slot == 3 && !stack.isEmpty() && isNoEffectUpgrade) {
                        return super.insertItem(slot, stack, simulate);
                    }
                    return stack;
                }
            };
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || energyStorage.hasCapability(capability, facing);
        }

        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inv);
            if (capability == CapabilityEnergy.ENERGY) {
                return CapabilityEnergy.ENERGY.cast(energyStorage);
            } else return null;
        }

        @Override
        public NBTBase serializeNBT() {
            NBTBase nbtBase = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
            NBTBase nbtBase1 = energyStorage.serializeNBT();
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            if (nbtBase1 != null) {
                nbtTagCompound.setTag("EnergyStorage", nbtBase1);
            }
            if (nbtBase != null) {
                nbtTagCompound.setTag("Inv", nbtBase);
            }
            return nbtTagCompound;
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            NBTTagCompound nbtTagCompound = (NBTTagCompound) nbt;
            if (nbtTagCompound.hasKey("EnergyStorage")) {
                NBTBase nbtBase1 = nbtTagCompound.getTag("EnergyStorage");
                energyStorage.deserializeNBT(nbtBase1);
            }
            if (nbtTagCompound.hasKey("Inv")) {
                NBTBase nbtBase = nbtTagCompound.getTag("Inv");
                CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbtBase);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            tooltip.add(StringHelper.shiftForDetails());
        }
    }

    public void addMoreInformation(ItemStack stack, List<String> tooltip) {
        if (isOn(stack)) {
            tooltip.add(StringHelper.getNoticeText("info.invgens.1"));
            tooltip.add(StringHelper.getDeactivationText("info.invgens.2"));
        } else {
            tooltip.add(StringHelper.getActivationText("info.invgens.3"));
        }

        if (isInChargingMode(stack)) {
            tooltip.add(StringHelper.getNoticeText("info.invgens.modeOn"));
        } else {
            tooltip.add(StringHelper.getNoticeText("info.invgens.modeOff"));
        }

        IItemHandler inv = CapabilityUtils.getCapability(stack, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int numSpeedUpgrades;
        if (inv == null) {
            numSpeedUpgrades = 0;
        } else {
            ItemStack speedUpgradesStack = inv.getStackInSlot(1);
            numSpeedUpgrades = speedUpgradesStack.getCount();
        }
        tooltip.add(StringHelper.localizeFormat("info.invgens.mode", StringHelper.getKeyName(KeyHandler.MODE_KEY.getKeyCode())));
        tooltip.add(StringHelper.localize("info.invgens.charge") + ": " + StringHelper.getScaledNumber(getInternalEnergyStored(stack))
                + " / " + StringHelper.getScaledNumber(getMaxEnergyStored(stack)) + " RF");
        tooltip.add(StringHelper.localize("info.invgens.send") + "/" + StringHelper.localize("info.invgens.receive")
                + ": " + StringHelper.formatNumber(getSend()) + "/" + StringHelper.formatNumber(getReceive()) + " RF/t" + " * " + StringHelper.formatNumber(numSpeedUpgrades + 1));
        tooltip.add(StringHelper.localize("info.invgens.burnTimeLeft") + ": " + StringHelper.formatNumber(getBurnTime(stack)));
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
            } else {
                nbtTagCompound.setInteger("Energy", 0);
            }
        } else {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setInteger("Energy", 0);
            stack.writeToNBT(nbtTagCompound);
        }
    }

    @Override
    public int getInternalEnergyStored(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if (tagCompound.hasKey("Energy")) {
                return tagCompound.getInteger("Energy");
            } else {
                tagCompound.setInteger("Energy", 0);
            }
        } else {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setInteger("Energy", 0);
            stack.writeToNBT(nbtTagCompound);
        }
        return 0;
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
        return Math.min(getMaxEnergyStored(stack) - getInternalEnergyStored(stack), getSend());
    }

    @Override
    public boolean isInChargingMode(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("Charging")) {
                return nbtTagCompound.getBoolean("Charging");
            } else {
                nbtTagCompound.setBoolean("Charging", false);
            }
        } else {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setBoolean("Charging", false);
            stack.writeToNBT(nbtTagCompound);
        }
        return false;
    }

    @Override
    public void changeMode(ItemStack stack) {
        if (isInChargingMode(stack) && stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            nbtTagCompound.setBoolean("Charging", false);
        } else {
            if (stack.hasTagCompound()) {
                NBTTagCompound tagCompound = stack.getTagCompound();
                tagCompound.setBoolean("Charging", true);
            } else {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setBoolean("Charging", false);
                stack.writeToNBT(nbtTagCompound);
            }
        }
    }

    @Override
    public boolean isOn(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("On")) {
                return nbtTagCompound.getBoolean("On");
            } else {
                nbtTagCompound.setBoolean("On", false);
            }
        } else {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setBoolean("On", false);
            stack.writeToNBT(nbtTagCompound);
        }
        return false;
    }

    @Override
    public void turnOn(ItemStack stack) {
        if (isOn(stack) && stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            nbtTagCompound.setBoolean("On", false);
        } else {
            if (stack.hasTagCompound()) {
                NBTTagCompound tagCompound = stack.getTagCompound();
                tagCompound.setBoolean("On", true);
            } else {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setBoolean("On", false);
                stack.writeToNBT(nbtTagCompound);
            }
        }
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("BurnTime")) {
                return nbtTagCompound.getInteger("BurnTime");
            } else {
                nbtTagCompound.setInteger("BurnTime", 0);
            }
        } else {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setInteger("BurnTime", 0);
            stack.writeToNBT(nbtTagCompound);
        }
        return 0;
    }

    @Override
    public void setBurnTime(ItemStack stack, int burnTime) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            if (nbtTagCompound.hasKey("BurnTime")) {
                nbtTagCompound.setInteger("BurnTime", burnTime);
            } else {
                nbtTagCompound.setInteger("BurnTime", 0);
            }
        } else {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setInteger("BurnTime", 0);
            stack.writeToNBT(nbtTagCompound);
        }
    }

    @Override
    public ItemStack getFuel(ItemStack stack) {
        IItemHandler inv = CapabilityUtils.getCapability(stack, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        return inv.getStackInSlot(0);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return 100000;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (!container.hasTagCompound()) {
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