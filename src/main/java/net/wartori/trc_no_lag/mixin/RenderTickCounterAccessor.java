package net.wartori.trc_no_lag.mixin;

import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderTickCounter.class)
public interface RenderTickCounterAccessor {
    @Accessor
    float getTickTime();
    @Mutable
    @Accessor
    void setTickTime(float tickTime);
}
