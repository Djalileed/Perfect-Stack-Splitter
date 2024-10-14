package dz.ratcommit.perfectstacksplitter.screen;

import dz.ratcommit.perfectstacksplitter.network.NetworkHandler;
import dz.ratcommit.perfectstacksplitter.network.SplitStackPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class CustomStackSplitGui extends Screen {
    private final Slot slot;
    private final ItemStack stack;
    private final ItemStack carriedStack;
    private EditBox amountField;
    private Button confirmButton;
    private Button cancelButton;
    private ForgeSlider stackSlider;
    private final Screen currentScreen;

    public CustomStackSplitGui(Slot slot, ItemStack stack, ItemStack carriedStack, Screen currentScreen) {
        super(Component.literal("Split Stack"));
        this.slot = slot;
        this.stack = stack;
        this.carriedStack = carriedStack;
        this.currentScreen = currentScreen;
    }

    @Override
    protected void init() {
        int maxStackAmount = stack.getCount() + carriedStack.getCount();

        this.amountField = new EditBox(this.font, this.width / 2 - 50, this.height / 2 - 75, 100, 20, Component.literal("Amount"));
        this.amountField.setValue("1");
        this.amountField.setResponder(text -> {
            try {
                int enteredValue = Integer.parseInt(text);
                if (enteredValue >= 1 && enteredValue <= maxStackAmount) {
                    this.stackSlider.setValue(enteredValue);
                }
            } catch (NumberFormatException e) {
                this.stackSlider.setValue(1);
            }
        });
        this.stackSlider = new CustomStackSplitSliderGui(
                this.width / 2 - 100,
                this.height / 2 - 50,
                200,
                20,
                Component.literal("Split Amount: "),
                Component.literal(""),
                1.0D,
                maxStackAmount,
                1.0D,
                true,
                this.amountField
        );

        this.confirmButton = Button.builder(Component.literal("Confirm"), (button) -> {
            int splitAmount = Integer.parseInt(this.amountField.getValue());
            this.splitStack(slot, splitAmount);
        }).bounds(this.width / 2 - 50, this.height / 2 + 10, 100, 20).build();

        this.cancelButton = Button.builder(Component.literal("Cancel"), (button) -> Minecraft.getInstance().setScreen(null)).bounds(this.width / 2 - 50, this.height / 2 + 40, 100, 20).build();

        this.addRenderableWidget(this.stackSlider);
        this.addRenderableWidget(this.confirmButton);
        this.addRenderableWidget(this.cancelButton);
        this.addRenderableWidget(this.amountField);
    }

    private void splitStack(Slot slot, int splitAmount) {
        ItemStack originalCarriedStack = Minecraft.getInstance().player.containerMenu.getCarried();
        ItemStack originalSlotStack = slot.getItem();
        if (splitAmount > 0 && splitAmount <= originalCarriedStack.getCount() + originalSlotStack.getCount()) {
            int slotId = slot.index;
            NetworkHandler.CHANNEL.sendToServer(new SplitStackPacket(slotId, splitAmount, originalCarriedStack, originalSlotStack));
            Minecraft.getInstance().setScreen(currentScreen);
        } else {
            Minecraft.getInstance().setScreen(null);
            Minecraft.getInstance().player.displayClientMessage(Component.literal("Invalid amount!"), true);
        }

    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render background and other components
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        // Render the slider
        this.stackSlider.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        // Render the text field and buttons
        this.amountField.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.confirmButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.cancelButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
