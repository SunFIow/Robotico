package com.sunflow.tile;

import com.sunflow.data.SunData;

public class TileState {
	private Tile tile = Tiles.DEFAULT;
	private SunData data;

	public Tile getTile() { return this.tile; }

	public void setTile(Tile tile) { this.tile = tile; }

	public SunData getOrCreateData() { return this.data == null ? new SunData() : this.data; }

	public SunData getData() { return this.data; }

	public void setData(SunData data) { this.data = data; }
}
