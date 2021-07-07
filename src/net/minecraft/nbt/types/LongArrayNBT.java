package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class LongArrayNBT extends CollectionNBT<LongNBT> {
	public static final INBTType<LongArrayNBT> TYPE = new INBTType<LongArrayNBT>() {
		@Override
		public LongArrayNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
			accounter.read(192L);
			int i = input.readInt();
			accounter.read(64L * i);
			long[] along = new long[i];

			for (int j = 0; j < i; ++j) {
				along[j] = input.readLong();
			}

			return new LongArrayNBT(along);
		}

		@Override
		public String getName() {
			return "LONG[]";
		}

		@Override
		public String getTagName() {
			return "TAG_Long_Array";
		}
	};
	private long[] data;

	public LongArrayNBT(long[] data) {
		this.data = data;
	}

	public LongArrayNBT(List<Long> longs) {
		this(toArray(longs));
	}

	private static long[] toArray(List<Long> longs) {
		long[] along = new long[longs.size()];

		for (int i = 0; i < longs.size(); ++i) {
			Long olong = longs.get(i);
			along[i] = olong == null ? 0L : olong;
		}

		return along;
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeInt(this.data.length);

		for (long i : this.data) {
			output.writeLong(i);
		}

	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 12;
	}

	@Override
	public INBTType<LongArrayNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		StringBuilder stringbuilder = new StringBuilder("[L;");

		for (int i = 0; i < this.data.length; ++i) {
			if (i != 0) {
				stringbuilder.append(',');
			}

			stringbuilder.append(this.data[i]).append('L');
		}

		return stringbuilder.append(']').toString();
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public LongArrayNBT copy() {
		long[] along = new long[this.data.length];
		System.arraycopy(this.data, 0, along, 0, this.data.length);
		return new LongArrayNBT(along);
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else {
			return p_equals_1_ instanceof LongArrayNBT && Arrays.equals(this.data, ((LongArrayNBT) p_equals_1_).data);
		}
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.data);
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		ITextComponent itextcomponent = (new StringTextComponent("L")).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
		IFormattableTextComponent iformattabletextcomponent = (new StringTextComponent("[")).append(itextcomponent).appendString(";");

		for (int i = 0; i < this.data.length; ++i) {
			IFormattableTextComponent iformattabletextcomponent1 = (new StringTextComponent(String.valueOf(this.data[i]))).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER);
			iformattabletextcomponent.appendString(" ").append(iformattabletextcomponent1).append(itextcomponent);
			if (i != this.data.length - 1) {
				iformattabletextcomponent.appendString(",");
			}
		}

		iformattabletextcomponent.appendString("]");
		return iformattabletextcomponent;
	}

	public long[] getAsLongArray() {
		return this.data;
	}

	@Override
	public int size() {
		return this.data.length;
	}

	@Override
	public LongNBT get(int p_get_1_) {
		return LongNBT.valueOf(this.data[p_get_1_]);
	}

	@Override
	public LongNBT set(int p_set_1_, LongNBT p_set_2_) {
		long i = this.data[p_set_1_];
		this.data[p_set_1_] = p_set_2_.getLong();
		return LongNBT.valueOf(i);
	}

	@Override
	public void add(int p_add_1_, LongNBT p_add_2_) {
		this.data = ArrayUtils.add(this.data, p_add_1_, p_add_2_.getLong());
	}

	@Override
	public boolean setNBTByIndex(int index, INBT nbt) {
		if (nbt instanceof NumberNBT) {
			this.data[index] = ((NumberNBT) nbt).getLong();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addNBTByIndex(int index, INBT nbt) {
		if (nbt instanceof NumberNBT) {
			this.data = ArrayUtils.add(this.data, index, ((NumberNBT) nbt).getLong());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public LongNBT remove(int p_remove_1_) {
		long i = this.data[p_remove_1_];
		this.data = ArrayUtils.remove(this.data, p_remove_1_);
		return LongNBT.valueOf(i);
	}

	@Override
	public byte getTagType() {
		return 4;
	}

	@Override
	public void clear() {
		this.data = new long[0];
	}
}
