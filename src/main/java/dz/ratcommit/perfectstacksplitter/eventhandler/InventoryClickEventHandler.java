package dz.ratcommit.perfectstacksplitter.eventhandler;

import dz.ratcommit.perfectstacksplitter.PerfectStackSplitter;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = PerfectStackSplitter.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventoryClickEventHandler {

    private static long rightClickStartTime = -1;
    private static boolean isRightClickHeld = false;

    @SubscribeEvent
    public static void onRightClickInventory(ScreenEvent.MouseButtonPressed.Pre event) {
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            if (rightClickStartTime == -1) {
                rightClickStartTime = System.currentTimeMillis();
                isRightClickHeld = true;
            }
        }
    }

    @SubscribeEvent
    public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Post event) {
        if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            isRightClickHeld = false;
            rightClickStartTime = -1;
        }
    }

    public static boolean isRightClickHeld() {
        return isRightClickHeld;
    }

    public static long getRightClickStartTime() {
        return rightClickStartTime;
    }

    public static void rightClickHandlerReset() {
        isRightClickHeld = false;
        rightClickStartTime = -1;
    }
}

