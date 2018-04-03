package com.kjmaster.inventorygenerators.client;

import com.kjmaster.inventorygenerators.InventoryGenerators;
import com.kjmaster.inventorygenerators.common.network.ChangeModePacket;
import com.kjmaster.inventorygenerators.common.network.PacketInstance;
import com.kjmaster.kjlib.utils.StringHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = InventoryGenerators.MODID)
public class KeyHandler {
    private static final String SECTION_NAME = "Inventory Generators";
    public static final KeyBinding MODE_KEY = new KeyBinding(StringHelper.localize("invgens.key.mode"), Keyboard.KEY_I, SECTION_NAME);
    static {
        ClientRegistry.registerKeyBinding(MODE_KEY);
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(receiveCanceled = false)
    public static void onKeyBoard(@Nonnull InputEvent.KeyInputEvent event) {
        if (MODE_KEY.isPressed()) {
            PacketInstance.INSTANCE.sendToServer(new ChangeModePacket());
        }
    }
}
