package com.kjmaster.inventorygenerators.common.network;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketInstance {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(InventoryGenerators.MODID);
}
