package com.sunflow.tile;

import com.sunflow.data.SunData;

public class TileState {
	private Tile tile = Tiles.DEFAULT;
	private SunData data;

	public Tile getTile() { return tile; }

	public void setTile(Tile tile) { this.tile = tile; }

	public SunData getOrCreateData() { return data == null ? new SunData() : data; }

	public SunData getData() { return data; }

	public void setData(SunData data) { this.data = data; }
}
