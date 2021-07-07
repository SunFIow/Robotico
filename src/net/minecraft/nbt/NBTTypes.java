package net.minecraft.nbt;

import net.minecraft.nbt.types.ByteArrayNBT;
import net.minecraft.nbt.types.ByteNBT;
import net.minecraft.nbt.types.CompoundNBT;
import net.minecraft.nbt.types.DoubleNBT;
import net.minecraft.nbt.types.EndNBT;
import net.minecraft.nbt.types.FloatNBT;
import net.minecraft.nbt.types.IntArrayNBT;
import net.minecraft.nbt.types.IntNBT;
import net.minecraft.nbt.types.ListNBT;
import net.minecraft.nbt.types.LongArrayNBT;
import net.minecraft.nbt.types.LongNBT;
import net.minecraft.nbt.types.ShortNBT;
import net.minecraft.nbt.types.StringNBT;

public class NBTTypes {
	private static final INBTType<?>[] TYPES = new INBTType[] {
			EndNBT.TYPE,
			ByteNBT.TYPE,
			ShortNBT.TYPE,
			IntNBT.TYPE,
			LongNBT.TYPE,
			FloatNBT.TYPE,
			DoubleNBT.TYPE,
			ByteArrayNBT.TYPE,
			StringNBT.TYPE,
			ListNBT.TYPE,
			CompoundNBT.TYPE,
			IntArrayNBT.TYPE,
			LongArrayNBT.TYPE };

	public static INBTType<?> getGetTypeByID(int id) {
		return id >= 0 && id < TYPES.length
				? TYPES[id]
				: INBTType.getEndNBT(id);
	}
}
