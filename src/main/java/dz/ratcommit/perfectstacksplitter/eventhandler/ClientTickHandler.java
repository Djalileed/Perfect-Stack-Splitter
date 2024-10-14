package dz.ratcommit.perfectstacksplitter.eventhandler;

import dz.ratcommit.perfectstacksplitter.PerfectStackSplitter;
import dz.ratcommit.perfectstacksplitter.screen.CustomStackSplitGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PerfectStackSplitter.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientTickHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (InventoryClickEventHandler.isRightClickHeld()) {
            long currentTime = System.currentTimeMillis();
            if (InventoryClickEventHandler.getRightClickStartTime() != -1) {
                long heldDuration = currentTime - InventoryClickEventHandler.getRightClickStartTime();

                if (heldDuration >= 1000) {
                    Minecraft mc = Minecraft.getInstance();
                    if (mc.screen instanceof AbstractContainerScreen<?> containerScreen) {
                        Slot slot = containerScreen.getSlotUnderMouse();
                        if (slot != null && !slot.getItem().isEmpty()) {
                            ItemStack itemStackSlot = slot.getItem();
                            ItemStack itemStackCarried = mc.player.containerMenu.getCarried();

                            Screen currentScreen = mc.screen;
                            Minecraft.getInstance().setScreen(new CustomStackSplitGui(slot, itemStackSlot, itemStackCarried, currentScreen));
                            InventoryClickEventHandler.rightClickHandlerReset();
                        }
                    }
                }
            }
        }
    }
}

