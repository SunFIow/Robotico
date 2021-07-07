package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.sunflow.util.math.MathHelper;

import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class DoubleNBT extends NumberNBT {
	public static final DoubleNBT ZERO = new DoubleNBT(0.0D);
	public static final INBTType<DoubleNBT> TYPE = new INBTType<DoubleNBT>() {
		@Override
		public DoubleNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException {
			accounter.read(128L);
			return DoubleNBT.valueOf(input.readDouble());
		}

		@Override
		public String getName() {
			return "DOUBLE";
		}

		@Override
		public String getTagName() {
			return "TAG_Double";
		}

		@Override
		public boolean isPrimitive() {
			return true;
		}
	};
	private final double data;

	private DoubleNBT(double data) {
		this.data = data;
	}

	public static DoubleNBT valueOf(double value) {
		return value == 0.0D ? ZERO : new DoubleNBT(value);
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
		output.writeDouble(this.data);
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 6;
	}

	@Override
	public INBTType<DoubleNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return this.data + "d";
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public DoubleNBT copy() {
		return this;
	}

	@Override
	public boolean equals(Object p_equals_1_) {
		if (this == p_equals_1_) {
			return true;
		} else {
			return p_equals_1_ instanceof DoubleNBT && this.data == ((DoubleNBT) p_equals_1_).data;
		}
	}

	@Override
	public int hashCode() {
		long i = Double.doubleToLongBits(this.data);
		return (int) (i ^ i >>> 32);
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		ITextComponent itextcomponent = (new StringTextComponent("d")).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
		return (new StringTextComponent(String.valueOf(this.data))).append(itextcomponent).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER);
	}

	@Override
	public long getLong() {
		return (long) Math.floor(this.data);
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
		return (float) this.data;
	}

	@Override
	public Number getAsNumber() {
		return this.data;
	}
}
