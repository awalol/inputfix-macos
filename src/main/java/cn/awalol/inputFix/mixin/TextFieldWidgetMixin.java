package cn.awalol.inputFix.mixin;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static cn.awalol.inputFix.InputFix.inputValue;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
    @Shadow
    private boolean editable = true;
    @Shadow
    public boolean isActive(){return false;}
    @Shadow
    private boolean selecting;
    @Shadow
    private void erase(int offset){}

    @Inject(method = "charTyped",at = @At("HEAD"))
    private void charTyped(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir){
        if(this.isActive() && SharedConstants.isValidChar(chr) && this.editable){
            inputValue = 0;
        }
    }

    @Inject(method = "keyPressed",at = @At("HEAD"),cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir){
        if(this.isActive()){
            if(keyCode != GLFW.GLFW_KEY_BACKSPACE){
                if(GLFW.GLFW_KEY_A <= keyCode && keyCode <= GLFW.GLFW_KEY_Z){
                    inputValue++;
                }
            } else {
                if(inputValue == 0){
                    if (this.editable) {
                        this.selecting = false;
                        this.erase(-1);
                        this.selecting = Screen.hasShiftDown();
                    }
                    cir.setReturnValue(true);
                    cir.cancel();
                }else if(inputValue > 0){
                    inputValue--;
                    cir.setReturnValue(false);
                    cir.cancel();
                }
            }
        }
    }

    @Inject(method = "<init>*",at = @At("RETURN"))
    private void init(CallbackInfo callbackInfo){
        inputValue = 0;
    }

}
