package team.creative.creativecore.common.gui.controls.simple;

import java.util.function.Consumer;

import net.fabricmc.moved.api.EnvType;
import net.fabricmc.moved.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.gui.GuiChildControl;
import team.creative.creativecore.common.gui.style.ControlFormatting;
import team.creative.creativecore.common.util.math.geo.Rect;

public class GuiButtonHoldSlim extends GuiButtonHold {
    
    public GuiButtonHoldSlim(String name, Consumer<Integer> pressed) {
        super(name, pressed);
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    protected void renderContent(GuiGraphics graphics, GuiChildControl control, Rect rect, int mouseX, int mouseY) {
        if (rect.inside(mouseX, mouseY))
            text.setDefaultColor(getStyle().fontColorHighlight.toInt());
        else
            text.setDefaultColor(getStyle().fontColor.toInt());
        super.renderContent(graphics, control, rect, mouseX, mouseY);
    }
    
    @Override
    public ControlFormatting getControlFormatting() {
        return ControlFormatting.TRANSPARENT;
    }
    
    @Override
    protected int minWidth(int availableWidth) {
        return -1;
    }
}