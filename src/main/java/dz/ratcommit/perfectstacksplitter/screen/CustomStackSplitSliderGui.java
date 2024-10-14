package dz.ratcommit.perfectstacksplitter.screen;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class CustomStackSplitSliderGui extends ForgeSlider {

    private final EditBox amountField;

    public CustomStackSplitSliderGui(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString, EditBox amountField) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, drawString);
        this.amountField = amountField;
    }

    @Override
    protected void applyValue() {
        this.amountField.setValue(String.valueOf((int) this.getValue()));
    }
}
