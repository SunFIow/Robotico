package com.sunflow;

import com.sunflow.game.GameBase;
import com.sunflow.tile.ImageTile;
import com.sunflow.tile.Tile;
import com.sunflow.tile.Tiles;
import com.sunflow.world.World;
import com.sunflow.world.gen.SingleTileGenerator;

public class Robotico extends GameBase {
	public static void main(String[] args) { settings.autostart(false); new Robotico().start(); }

	World world;
	int w, h;

	@Override
	public void setup() {
		createCanvas(480, 480);
		syncMode(ASYNC);
		tickRate(20);

		w = 8;
		h = 8;
		ImageTile.init(this::loadImage);

//		world = new World((holder, status) -> {
//			Chunk chunk = null;
//			return chunk;
//		});

		world = new World(new SingleTileGenerator(Tiles.DEFAULT));
	}

	@Override
	public void update() {
	}

	@Override
	public void draw() {
		scale(width / w, height / h);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				Tile tile = world.getTileState(x, y).getTile();
				tile.show(this, x, y);
				noFill();
				stroke(255, 0, 0);
				strokeWeight(0.05f);
				rect(x, y, 1, 1);
			}
		}
	}
}
