package net.minecraft.client.util;

import com.sunflow.api.distmarker.Dist;
import com.sunflow.api.distmarker.OnlyIn;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface ICharacterPredicate {
	boolean test(char p_test_1_);
}
