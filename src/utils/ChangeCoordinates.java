package utils;

import org.jbox2d.common.Vec2;

public class ChangeCoordinates {
	
	public static final int TILES_PER_METER = 1;
	public static final int TILE_SIZE = 32;
//	private static final Vec2 vetorAuxiliar = new Vec2();
	
	public static Vec2 physicsToScreen (Vec2 coordinates)
	{
//		coordinates.mulLocal(TILES_PER_METER * TILE_SIZE);
//		vetorAuxiliar.x = 0;
//		vetorAuxiliar.y = 2 * coordinates.y;
//		coordinates.subLocal(vetorAuxiliar);
		return new Vec2 (coordinates.x * TILES_PER_METER * TILE_SIZE, - coordinates.y * TILES_PER_METER * TILE_SIZE);
	}

	public static float physicsToScreen (float x)
	{
		return x * TILES_PER_METER * TILE_SIZE;
	}

	public static Vec2 screenToPhysics (Vec2 coordinates)
	{
//		coordinates.mulLocal(TILE_SIZE / TILES_PER_METER);
//		vetorAuxiliar.x = 0;
//		vetorAuxiliar.y = 2 * coordinates.y;
//		coordinates.subLocal(vetorAuxiliar);
		return new Vec2 (coordinates.x / TILES_PER_METER / TILE_SIZE, - coordinates.y / TILES_PER_METER / TILE_SIZE);
	}

	public static float screenToPhysics (float x)
	{
		return x / TILES_PER_METER / TILE_SIZE;
	}

}
