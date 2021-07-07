package net.minecraft.nbt.types;

import net.minecraft.nbt.INBT;

public abstract class NumberNBT implements INBT {
	protected NumberNBT() {
	}

	public abstract long getLong();

	public abstract int getInt();

	public abstract short getShort();

	public abstract byte getByte();

	public abstract double getDouble();

	public abstract float getFloat();

	public abstract Number getAsNumber();
}
