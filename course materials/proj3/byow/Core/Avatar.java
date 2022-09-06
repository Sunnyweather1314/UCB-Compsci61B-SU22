package byow.Core;

import java.io.Serializable;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
// import java.util.Random;

public class Avatar implements Serializable {
    private int x;
    private int y;
    private boolean renderMode;

    public Avatar(int x, int y) {
        this.x = x;
        this.y = y;
        this.renderMode = false;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void show(TERenderer ter, TETile[][] worldFrame) {
        worldFrame[x][y] = (Tileset.AVATAR);
        ter.renderFrame(worldFrame);
    }
        
    public void render() {
        renderMode = !renderMode;
    }

    public void move(TERenderer ter, TETile[][] worldFrame, int X, int Y) {
        if (canMove(worldFrame, X, Y)) {
            TETile temp = worldFrame[X][Y];
            worldFrame[X][Y] = Tileset.AVATAR;
            worldFrame[x][y] = Tileset.FLOOR;
            this.x = X;
            this.y = Y;
            if (renderMode) {
                marginalShow(ter, worldFrame, X, Y);
            } else {
                ter.renderFrame(worldFrame);
                int k = 0;
            }
        }
        
    }


    public void marginalShow(TERenderer ter, TETile[][] worldFrame, int X, int Y) {
        TETile[][] randeredFrame = Engine.createNewWorld();
        int randerSize = 3;
        for (int c = X - randerSize; c < X + randerSize; c++) {
            if (!(c > worldFrame.length) && !(c < 0)) {
                for (int r = Y - randerSize; r < Y + randerSize; r++) {
                    if (!(r > worldFrame[0].length) && !(r < 0)) {
                        randeredFrame[c][r] = worldFrame[c][r];
                    }
                }
            }
        }
        ter.renderFrame(randeredFrame);
    }

    public boolean canMove(TETile[][] worldFrame, int X, int Y) {
        return !worldFrame[X][Y].getDescription().equals("wall");
    }


}


