package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.sunflow.util.math.MathHelper;

import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class FloatNBT extends NumberNBT {
	public static final FloatNBT ZERO = new FloatNBT(0.0F);
	public static final INBTType<FloatNBT> TYPE = new INBTType<FloatNBT>() {
		@Override
		public FloatNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
			accounter.read(96L);
			return FloatNBT.valueOf(input.readFloat());
		}

		@Override
		public String getName() {
			return "FLOAT";
		}

		@Override
		public String getTagName() {
			return "TAG_Float";
		}

		@Override
		public boolean isPrimitive() {
			return true;
		}
	};
	private final float data;

	private FloatNBT(float data) {
		this.data = data;
	}

	public static FloatNBT valueOf(float value) {
		return value == 0.0F ? ZERO : new FloatNBT(value);
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeFloat(this.data);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 5;
	}

	@Override
	public INBTType<FloatNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return this.data + "f";
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public FloatNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else {
			return p_equals_1_ instanceof FloatNBT && this.data == ((FloatNBT) p_equals_1_).data;
		}
	}

	@Override
	public int hashCode() {
		return Float.floatToIntBits(this.data);
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		ITextComponent itextcomponent = (new StringTextComponent("f")).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
		return (new StringTextComponent(String.valueOf(this.data))).append(itextcomponent).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER);
	}

	@Override
	public long getLong() {
		return (long) this.data;
	}

	@Override
	public int getInt() {
		return MathHelper.floor(this.data);
	}

	@Override
	public short getShort() {
		return (short) (MathHelper.floor(this.data) & '\uffff');
	}

	@Override
	public byte getByte() {
		return (byte) (MathHelper.floor(this.data) & 255);
	}

	@Override
	public double getDouble() {
		return this.data;
	}

	@Override
	public float getFloat() {
		return this.data;
	}

	@Override
	public Number getAsNumber() {
		return this.data;
	}
}
