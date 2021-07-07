package net.minecraft.util;

import com.sunflow.api.distmarker.Dist;
import com.sunflow.api.distmarker.OnlyIn;

import net.minecraft.util.text.Style;

@FunctionalInterface
public interface ICharacterConsumer {
	@OnlyIn(Dist.CLIENT)
	boolean accept(int p_accept_1_, Style p_accept_2_, int p_accept_3_);
}
