package net.minecraft.nbt.types;

import java.util.AbstractList;

import net.minecraft.nbt.INBT;

public abstract class CollectionNBT<T extends INBT> extends AbstractList<T> implements INBT {
	@Override
	public abstract T set(int p_set_1_, T p_set_2_);

	@Override
	public abstract void add(int p_add_1_, T p_add_2_);

	@Override
	public abstract T remove(int p_remove_1_);

	public abstract boolean setNBTByIndex(int index, INBT nbt);

	public abstract boolean addNBTByIndex(int index, INBT nbt);

	public abstract byte getTagType();
}
