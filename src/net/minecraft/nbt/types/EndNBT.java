package net.minecraft.nbt.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.INBTType;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class EndNBT implements INBT {
	public static final INBTType<EndNBT> TYPE = new INBTType<EndNBT>() {
		@Override
		public EndNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) {
			accounter.read(64L);
			return EndNBT.INSTANCE;
		}

		@Override
		public String getName() {
			return "END";
		}

		@Override
		public String getTagName() {
			return "TAG_End";
		}

		@Override
		public boolean isPrimitive() {
			return true;
		}
	};
	public static final EndNBT INSTANCE = new EndNBT();

	private EndNBT() {
	}

	/**
	 * Write the actual data contents of the tag, implemented in NBT extension classes
	 */
	@Override
	public void write(DataOutput output) throws IOException {
	}

	/**
	 * Gets the type byte for the tag.
	 */
	@Override
	public byte getId() {
		return 0;
	}

	@Override
	public INBTType<EndNBT> getType() {
		return TYPE;
	}

	@Override
	public String toString() {
		return "END";
	}

	/**
	 * Creates a clone of the tag.
	 */
	@Override
	public EndNBT copy() {
		return this;
	}

	@Override
	public ITextComponent toFormattedComponent(String indentation, int indentDepth) {
		return StringTextComponent.EMPTY;
	}
}
