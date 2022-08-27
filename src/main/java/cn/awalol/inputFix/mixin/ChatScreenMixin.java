package cn.awalol.inputFix.mixin;

import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static cn.awalol.inputFix.InputFix.inputValue;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Inject(method = "keyPressed",at = @At("HEAD"),cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir){
        if((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) && inputValue > 0){
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
