package byow.Core;


// import byow.Core.RandomUtils;
import byow.InputDemo.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.Random;

// import javax.swing.filechooser.FileNameExtensionFilter;

import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class Engine implements Serializable {
    // TERenderer ter = new TERenderer();

    private int menuWidth = 30 * 16;
    private int menuHeight = 35 * 16;
    private double xMid = menuWidth / 2.0;
    private double yMid = menuHeight / 2.0;

    Font titleFont = new Font("Monaco", Font.PLAIN, 30);
    Font optionFont = new Font("Monaco", Font.PLAIN, 20);


    /* Feel free to change the width and height. */
    private ArrayList<House> fullhouses;
    private TETile[][] worldFrame;
    private Random random;
    private Avatar me;
    private long timePassed;
    private long startTime;
    private Engine currEngine;
    private boolean light;
    
    public static final int WIDTH = 70;
    public static final int HEIGHT = 45;
    public static final File CWD = new File(System.getProperty("user.dir"));
    // private final static int upperWBound = WIDTH / 4 * 3;
    // private final static int lowerWBound = WIDTH / 4;
    // private final static int upperHBound = HEIGHT / 6 * 5;
    // private final static int lowerHBound = WIDTH / 6;
    private static final File LOCAL = Utils.join(CWD, "byow.txt");

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        currEngine = this;
        Long seed = menuPage();
        if (seed == 1L) {
            return;
        }

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        if (currEngine.equals(this)) {
            this.random = new Random(seed);
            worldFrame = createNewWorld();
            fullhouses = createHouses(currEngine.worldFrame(), ter);
            int[] startPos = 
                fullhouses.get(RandomUtils.uniform(random, fullhouses.size())).randomPosInHouse();
            me = new Avatar(startPos[0], startPos[1]);
            currEngine.me().show(ter, currEngine.worldFrame());
        }
        ter.renderFrame(currEngine.worldFrame());
        Character c = 'H';
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {

                c = Character.toUpperCase(StdDraw.nextKeyTyped());
                System.out.println(c);
                if (c.equals(':')) {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            c = Character.toUpperCase(StdDraw.nextKeyTyped());
                            if (c.equals('Q')) {
                                currEngine.setTimePassed(System.nanoTime() - startTime);
                                StdDraw.clear(Color.BLACK); 
                                StdDraw.show();
                                Utils.writeObject(LOCAL, currEngine);
                                return;
                            }
                        }
                    }
                } else {
                    moveWithInput(ter, c);

                }
            }

            // ter.renderFrame(currEngine.worldFrame());
            // if (checkFinished(currEngine.worldFrame(), currEngine.light()) == 1) {
            //     lose();
            //     break;
            // } else if (checkFinished(currEngine.worldFrame(), currEngine.light()) == 2) {
            //     win();
            //     break;
            // }
        }
    }





    // /**
    //  * Method used for autograding and testing your code. The input string will be a series
    //  * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
    //  * behave exactly as if the user typed these characters into the engine using
    //  * interactWithKeyboard.
    //  *
    //  * Recall that strings ending in ":q" should cause the game to quite save. For example,
    //  * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
    //  * 7 commands (n123sss) and then quit and save. If we then do
    //  * interactWithInputString("l"), we should be back in the exact same state.
    //  *
    //  * In other words, both of these calls:
    //  *   - interactWithInputString("n123sss:q")
    //  *   - interactWithInputString("lww")
    //  *
    //  * should yield the exact same world state as:
    //  *   - interactWithInputString("n123sssww")
    //  *
    //  * @param input the input string to feed to your program
    //  * @return the 2D TETile[][] representing the state of the world
    //  */
    // public TETile[][] interactWithInputString(String input) {
    //     // Fill out this method so that it run the engine using the input
    //     // passed in as an argument, and return a 2D tile representation of the
    //     // world that would have been drawn if the same inputs had been given
    //     // to interactWithKeyboard().
    //     //
    //     // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
    //     // that works for many different input types.
    //     int seed = 0;
    //     // String inputX;

    //     // while (inputX.length() != "S") {
    //     //     if (StdDraw.hasNextKeyTyped()) {
    //     //         if()
    //     //         inputX = Character.toString(StdDraw.nextKeyTyped());

    //     //     } 
    //     // }

        

    //     seed = Integer.valueOf(input);
    //     Random ranDom = new Random(seed);
    //     TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    //     TERenderer ter = new TERenderer();
    //     ter.initialize(WIDTH, HEIGHT);



    //     //create random numbers of houses with ramdom sizes

    //     ArrayList<House> houses = createHouses(finalWorldFrame, ter, ranDom);

    //     drawHouses(houses, finalWorldFrame);
    //     // draws the world to the screen
    //     ter.renderFrame(finalWorldFrame);
    //     return finalWorldFrame;
    // }





    public int checkFinished(TETile[][] finalWorldFrame, boolean lightT) {
        // 1 is lose, 2 is won, 0 is continue

        if (!lightT && startTime == 0L) {
            return 0;
        }
        // System.out.println(System.nanoTime() - startTime);
        if (System.nanoTime() - startTime > Math.pow(10, 9) * 60) {

            return 1;
        }

        for (int i = 0; i < finalWorldFrame.length; i++) {
            for (int r = 0; r < finalWorldFrame[0].length; r++) {
                if (light && finalWorldFrame[i][r].equals(Tileset.LIGHT0) 
                    || finalWorldFrame[i][r].equals(Tileset.LIGHT1) 
                    || finalWorldFrame[i][r].equals(Tileset.LIGHT2)) {
                    return 0;
                }
            }
        }

        if (lightT) {
            return 2;
        } else {
            return 0;
        }

    }



    public TETile[][] interactWithInputString(String input) {
        String[] processed = processInput(input);
        currEngine = this;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        if (processed[0].equals("l")) {
            currEngine = Utils.readObject(LOCAL, Engine.class);
        } else {
            Long seed = Long.parseLong(processed[0]);
            this.random = new Random(seed);
            TETile[][] finalWorldFrame = createNewWorld();
            fullhouses = createHouses(finalWorldFrame, ter);
            worldFrame = finalWorldFrame;

            int[] startPos = 
                fullhouses.get(RandomUtils.uniform(random, fullhouses.size())).randomPosInHouse();
            me = new Avatar(startPos[0], startPos[1]);
            me.show(ter, worldFrame);
        }

        // ter.renderFrame(currEngine.worldFrame());

        // lightHouses(ter, finalWorldFrame, fullhouses);

//        turnOffAll(ter, finalWorldFrame, fullhouses);
        for (int i = 0; i < processed[1].length(); i++) {
            Character c = processed[1].charAt(i);
            currEngine.moveWithInput(ter, c);
            // if (checkFinished(currEngine.worldFrame(), currEngine.light()) == 1) {
            //     lose();
            //     break;
            // } else if (checkFinished(currEngine.worldFrame(), currEngine.light()) == 2) {
            //     win();
            //     break;
            // }
        }

        if (Integer.valueOf(processed[2]) >= 0) {
            currEngine.setTimePassed(System.nanoTime() - startTime);
            Utils.writeObject(LOCAL, currEngine);
        }
        return currEngine.worldFrame();
    }



    private String[] processInput(String input) {

        char first = input.charAt(0);
        String[] toReturn = new String[3];
        if (first == 'L' || first == 'l') {
            toReturn[0] = input.substring(0, 1);
            toReturn[1] = input.substring(1);
        } else {


            if (first == 'n' || first == 'N') {
                input = input.substring(1, input.length());
            } else if (((int) first) > 57) {
                return null;
            }

            int separate = input.indexOf('s');
            if (separate < 0) {
                return null;
            }
            toReturn[0] = input.substring(0, separate);
            toReturn[1] = input.substring(separate + 1, input.length());
        }
        String quit = String.valueOf(input.indexOf(":q"));


        toReturn[2] = quit;
        return toReturn;
    

    }

    private long menuPage() {
        StdDraw.setCanvasSize(menuWidth, menuHeight);
        StdDraw.setXscale(0, menuWidth);
        StdDraw.setYscale(0, menuHeight);
        StdDraw.clear(Color.BLACK); 
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(xMid, yMid + 30, "CS61B: BYoW");
        StdDraw.setFont(optionFont);
        StdDraw.text(xMid, yMid, "New Game (N)");
        StdDraw.text(xMid, yMid - 20, "Load Game (L)");
        StdDraw.text(xMid, yMid - 40, "Player Name (P)");
        StdDraw.text(xMid, yMid - 60, "Quit (Q)");
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        Long seed = Long.valueOf(0);
        Character c = ' ';
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (c == 'L') {
                    if (LOCAL.exists()) {
                        currEngine = Utils.readObject(LOCAL, Engine.class);

                        return 0L;
                    }
                } else if (c == 'N') {
                    Character last = ' ';
                    while (!last.equals('S')) {
                        if (StdDraw.hasNextKeyTyped()) {
                            last = Character.toUpperCase(StdDraw.nextKeyTyped());
                            if (!last.equals('S')) {
                                seed = 10 * seed + (int) (last) - 48;
                                StdDraw.clear(Color.BLACK); 
                                StdDraw.text(xMid, yMid, "current seed: " + seed);
                                StdDraw.show();
                            }
                        }
                    }
                    return seed;
                } else if (c == 'Q') {
                    currEngine.setTimePassed(System.nanoTime() - startTime);
                    StdDraw.clear(Color.BLACK);
                    StdDraw.show();

                    return 1L;
                }
            }
        }
    }

    private void win() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(Color.RED);

        StdDraw.text(xMid, yMid, "You Won!");

        StdDraw.show();
    }

    private void lose() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(Color.RED);

        StdDraw.text(WIDTH / 2, HEIGHT / 2, "You Lose!");

        StdDraw.show();
    }




    // public void mouseInteraction(TERenderer ter, TETile[][] finalWorldFrame) {
    //     double[] mousePos = new double[]{StdDraw.mouseX(), StdDraw.mouseY()};
    //     // System.out.print(mousePos[0]);
    //     // System.out.println(mousePos[1]);
    //     if (mousePos[0] < WIDTH && mousePos[1] < this.HEIGHT
    //     && mousePos[0] > 0 && mousePos[1]  > 0) {
    //         String item = finalWorldFrame[(int) mousePos[0]][(int) mousePos[1]].getDescription();
    //         StdDraw.setPenColor(Color.RED);
    //         StdDraw.textLeft(5, 45, item);
    //     }

    //     // System.out.println(item);

    //     ter.renderFrame(finalWorldFrame);
    // }

    // create houses separately in random places in the space where they do not overlap
//     public ArrayList<House> createHouses (TETile[][] 
// finalWorldFrame, TERenderer ter, Random ranDom) {
//         ArrayList<House> houses = new ArrayList<>();
//         int numHouses = ranDom.nextInt(12);
//         int widthH;
//         int heightH;
//         int cornorX;
//         int cornorY;
//         House newHouse = null;
//         boolean collide = false;
//         for (int i = 0; i< numHouses; i++) {
//             while (newHouse == null || collide) {
//                 widthH = RandomUtils.uniform(ranDom, 3, 20);
//                 heightH = RandomUtils.uniform(ranDom, 3, 20);
//                 cornorX = RandomUtils.uniform(ranDom, 0, WIDTH - widthH);
//                 cornorY = RandomUtils.uniform(ranDom, 0, HEIGHT- heightH);
//                 newHouse = new House(cornorX, cornorY, widthH, heightH);
//                 for (House houseC: houses) {
//                     collide = houseC.checkCollide(newHouse);
//                     if(collide) {
//                         break;
//                     }
//                 }
//                 System.out.println(newHouse);
//             }
//             houses.add(newHouse);

// //            newHouse.drawHouse(finalWorldFrame);
//             newHouse = null;
//             collide = false;
//         }
//         ter.renderFrame(finalWorldFrame);
//         return houses;
//     }



    public void lightHouses(TERenderer ter, TETile[][] finalWorldFrame, ArrayList<House> houses) {
        this.setLight();
        for (House house: houses) {
            turnOnHouse(ter, finalWorldFrame, house);
        }

    }

    public void turnOnHouse(TERenderer ter, TETile[][] finalWorldFrame, House house) {
        int[] lightT = house.randomPosInHouse();
        int lightX = lightT[0];
        int lightY = lightT[1];
        int lightRange = 3;

        for (int r = 0; r < lightRange; r++) {
            if (lightX - r >= house.cornorX() + 1) {
                for (int k = -r; k <= r; k++) {
                    if (house.inHouse(lightX - r, lightY + k)) {
                        finalWorldFrame[lightX - r][lightY + k] = lightColor(r);
                    }
                }
            
            }
        }
        for (int r = 0; r < lightRange; r++) {
            if (lightX + r <= house.cornorX() + house.width() - 1) {
                for (int k = -r; k <= r; k++) {
                    if (house.inHouse(lightX + r, lightY + k)) {
                        finalWorldFrame[lightX + r][lightY + k] = lightColor(r);
                    }
                }
            
            }
        }

        for (int r = 0; r < lightRange; r++) {
            if (lightY - r >= house.cornorY() + 1) {
                for (int k = -r; k <= r; k++) {
                    if (house.inHouse(lightX - k, lightY - r)) {
                        finalWorldFrame[lightX - k][lightY - r] = lightColor(r);
                    }
                }
            
            }
        }
        for (int r = 0; r < lightRange; r++) {
            if (lightY - r <= house.cornorY() + house.height() - 1) {
                for (int k = -r; k <= r; k++) {
                    if (house.inHouse(lightX + k, lightY + r)) {
                        finalWorldFrame[lightX + k][lightY + r] = lightColor(r);
                    }
                }
            
            }
        }

        ter.renderFrame(finalWorldFrame);
    }



    public void turnOffAll(TERenderer ter, TETile[][] finalWorldFrame, ArrayList<House> houses) {
        this.setLight();
        drawHouses(houses, finalWorldFrame);
        ter.renderFrame(finalWorldFrame);
    }

    private TETile lightColor(int distance) {
        switch (Math.abs(distance)) {
            case 0:
                return Tileset.LIGHT0;
            case 1:
                return Tileset.LIGHT1;
            case 2:
                return Tileset.LIGHT2;
            default:
                return null;
        }


    }




    public void moveWithInput(TERenderer ter, Character c) {
        TETile last;
        switch (c) {
            case 'W':
                currEngine.me().move(ter, currEngine.worldFrame(), currEngine.me().getX(), 
                            currEngine.me().getY() + 1);
                break;
            case 'D':
                currEngine.me().move(ter, currEngine.worldFrame(), currEngine.me().getX(), 
                            currEngine.me().getY() - 1);
                break;
            case 'A':
                currEngine.me().move(ter, currEngine.worldFrame(), currEngine.me().getX() - 1, 
                            currEngine.me().getY());
                break;
            case 'S':
                currEngine.me().move(ter, currEngine.worldFrame(), currEngine.me().getX() + 1, 
                            currEngine.me().getY());

                break;
            case 'O':
                if (startTime == 0L) {
                    startTime = System.nanoTime();
                    // System.out.println("Starttime: " + startTime);

                }
                if (!currEngine.light()) {
                    lightHouses(ter, currEngine.worldFrame(), currEngine.fullHouses());
                    currEngine.me().show(ter, currEngine.worldFrame());
                    ter.renderFrame(currEngine.worldFrame());
                }


                break;
            case 'C':
                turnOffAll(ter, currEngine.worldFrame(), currEngine.fullHouses());
                currEngine.me().show(ter, currEngine.worldFrame());
                ter.renderFrame(currEngine.worldFrame());
                break;
            case 'R':
                currEngine.me().render();
                break;
                    
            default:
                break;

        }
    }

    public ArrayList<House> createHouses(TETile[][] finalWorldFrame, 
                    TERenderer ter) {
        int numHouses = 0;
        ArrayList<House> houses = new ArrayList<>();
        ArrayList<House> hallways = new ArrayList<>();
        int widthH;
        int heightH;
        int cornorX;
        int cornorY;
        int currX;
        int currY;
        widthH = RandomUtils.uniform(random, 3, 10);
        heightH = RandomUtils.uniform(random, 3, 10);
        currX = widthH;
        currY = heightH;
        cornorX = RandomUtils.uniform(random, 0, WIDTH / 5 - currX);
        cornorY = RandomUtils.uniform(random, 0, HEIGHT / 4 - currY);
        House currHouse = new House(cornorX, cornorY, widthH, heightH, numHouses % 2 == 1, null);
        houses.add(currHouse);
        numHouses++;
        while (canBuildRight(finalWorldFrame, currHouse)) {

            int[] newDoor = currHouse.openEast(1);
            currHouse.addDoor(newDoor);
            currHouse.drawHouse(finalWorldFrame);
            int[] newDoorForNewHouse = new int[]{newDoor[0] + 1, newDoor[1]};

            if (numHouses % 2 == 1) {
                currHouse = createNewHallway(currHouse, finalWorldFrame, newDoorForNewHouse);
                currHouse.drawHouse(finalWorldFrame);
                hallways.add(currHouse);
                
            } else {
                currHouse = createNewHouse(currHouse, finalWorldFrame, newDoorForNewHouse);
                houses.add(currHouse);
            }

            currHouse.drawHouse(finalWorldFrame);
            ter.renderFrame(finalWorldFrame);
            numHouses++;
        }

        ArrayList<House> forkHouses = createForks(ter, finalWorldFrame, hallways);
        houses.addAll(forkHouses);

        return houses;
    }


    public ArrayList<House> createForks(TERenderer ter, TETile[][] finalWorldFrame, 
                    ArrayList<House> hallways) {
        ArrayList<House> forkHouses = new ArrayList<House>();
        for (House hallway: hallways) {
            House forkHouse = createSingleFork(finalWorldFrame, hallway);
            if (forkHouse != null) {
                forkHouses.add(forkHouse);
                forkHouse.drawHouse(finalWorldFrame);
                ter.renderFrame(finalWorldFrame);
            }
        }
        return forkHouses;

    }





    public House createSingleFork(TETile[][] finalWorldFrame, House hallway) {


        // if (RandomUtils.uniform(random, 4) % 4 == 1) {
        //     return null;
        // }
        //hallway
        if (hallway.cornorX + 1 >=  hallway.cornorX() + hallway.width() - 1) {
            return null;
        }

        int newCornorX = RandomUtils.uniform(random, hallway.cornorX + 1,
            hallway.cornorX() + hallway.width() - 1);
        int width = RandomUtils.uniform(random, 3, 4);
        for (int i = 0; i < width; i++) {
            if (!canIntersectUp(finalWorldFrame, newCornorX + i, 
                    hallway.cornorY, hallway.height())) {
                return null;
            }
        }



        int newCornorY = hallway.cornorY;

        int height = RandomUtils.uniform(random, 2, (int) ((HEIGHT - newCornorY) * 0.4));
        House forkedHall = new House(newCornorX, newCornorY, width, height, true, null);
        int[] hallDoor = forkedHall.openNorth(1);
        forkedHall.drawHouse(finalWorldFrame);
        intersectUp(finalWorldFrame, new int[]{newCornorX, newCornorY});

        //house

        int[] newDoorForNewHouse = new int[]{hallDoor[0], hallDoor[1] + 1};
        newCornorY = newDoorForNewHouse[1];
        height = RandomUtils.uniform(random, 3, 
                Math.min(HEIGHT / 4, HEIGHT - newDoorForNewHouse[1] - 1));
        int[] boundary = checkBoundaryHorizontal(finalWorldFrame, newDoorForNewHouse[0],
                newDoorForNewHouse[1], height);
        if (newDoorForNewHouse[0] - 1 <= boundary[0]) {
            finalWorldFrame[hallDoor[0]][hallDoor[1]] = Tileset.WALL;
            return null;
        }
        newCornorX = RandomUtils.uniform(random, boundary[0], newDoorForNewHouse[0] - 1);

        if (boundary[1] - newDoorForNewHouse[0] < 2) {
            finalWorldFrame[hallDoor[0]][hallDoor[1]] = Tileset.WALL;
            return null;
        }
        width = newDoorForNewHouse[0] - newCornorX 
                + RandomUtils.uniform(random, 1, boundary[1] - newDoorForNewHouse[0]);
        House forkedHouse = new House(newCornorX, newCornorY, width, height,
                false, newDoorForNewHouse);


        forkedHouse.addDoor(newDoorForNewHouse);

        return forkedHouse;
    }



    public int[] checkBoundaryHorizontal(TETile[][] finalWorldFrame, int X, int Y, int height) {
        int currY = Y;
        int currX = X;
        int[] boundary = new int[]{currX, currX};


        while (currY < Y + height) {
            currX = X;
            while (currX > 0 && finalWorldFrame[currX][Y].equals(Tileset.NOTHING)) {
                boundary[0] = Math.min(boundary[0], currX);
                currX--;
            }

            currX = X;
            while (currX < WIDTH && finalWorldFrame[currX][Y].equals(Tileset.NOTHING)) {
                boundary[1] = Math.max(boundary[1], currX);
                currX++;
            }
            currY++;
        }
        return boundary;
    }


    public int[] checkBoundaryVertical(TETile[][] finalWorldFrame, int X, int Y, int width) {
        int currY = Y;
        int currX = X;
        int[] boundary = new int[]{currX, currX};


        while (currY < X + width) {
            currY = Y;
            while (currY > 0 && finalWorldFrame[X][currY].equals(Tileset.NOTHING)) {
                boundary[0] = Math.min(boundary[0], currY);
                currY--;
            }

            currY = Y;
            while (currY < HEIGHT && finalWorldFrame[X][currY].equals(Tileset.NOTHING)) {
                boundary[1] = Math.max(boundary[1], currY);
                currY++;
            }
            currY++;
        }
        return boundary;
    }





//              | | | |
//              | | | |
//            --|-| |-|---
//            --|-| |-|---
//            --|-| |-|---
//           --------------

    public boolean canIntersectUp(TETile[][] finalWorldFrame, int x, int y, int height) {
        boolean b = true;
        for (int s = 1; s < 5; s++) {
            if (s == height) {
                b = b && finalWorldFrame[x][y + s].equals(Tileset.WALL);

            } else if (s < height) {
                b = b && finalWorldFrame[x][y + s].equals(Tileset.FLOOR);
            } else {
                b = b && finalWorldFrame[x][y + s].equals(Tileset.NOTHING);
            }
        }
        return b;
    }

    public void intersectUp(TETile[][] finalWorldFrame, int[] position) {
        int X = position[0];
        int Y = position[1];
        finalWorldFrame[X][Y + 1] = Tileset.FLOOR;
        finalWorldFrame[X + 2][Y + 1] = Tileset.FLOOR;
        finalWorldFrame[X][Y + 2] = Tileset.FLOOR;
        finalWorldFrame[X + 3][Y + 2] = Tileset.FLOOR;
        finalWorldFrame[X + 3][Y + 1] = Tileset.FLOOR;
        finalWorldFrame[X + 1][Y + 2] = Tileset.FLOOR;
    }
    

    // public void intersectDown(TETile[][] finalWorldFrame, int[] position) {

    // }

    public House createNewHouse(House prevHouse, TETile[][] finalWorldFrame, int[] door) {
        int width = RandomUtils.uniform(random, 3, Math.min(WIDTH - door[0], WIDTH / 8));
        int height = RandomUtils.uniform(random, 3, Math.min(HEIGHT / 2, 
                        HEIGHT - door[1]));

        int cornorX = door[0];
        int cornorY = RandomUtils.uniform(random, Math.max(0, door[1] - height + 1), 
                        Math.max(1, door[1] - 1));
        House house = new House(cornorX, cornorY, width, height, false, door);
        return house;

    }
    public House createNewHallway(House prevHouse, TETile[][] finalWorldFrame, int[] door) {
        int width = RandomUtils.uniform(random, 4, Math.min(WIDTH - door[0], WIDTH / 5));
        int height = RandomUtils.uniform(random, 3, 4);
        // prevHouse.addDoor(door);
        int cornorX = door[0];
        int cornorY = door[1] - 1;

        House hallway = new House(cornorX, cornorY, width, height, true, door);
        return hallway;
    }


    public void drawHouses(ArrayList<House> houses, TETile[][] world) {
        for (House house: houses) {
            house.drawHouse(world);
        }

    }




    public boolean canGoUp(TETile[][] finalWorldFrame, int x, int y) {
        return y <= HEIGHT - 3 && finalWorldFrame[x][y + 1].equals(Tileset.NOTHING)
            && finalWorldFrame[x][y + 2].equals(Tileset.NOTHING) 
            && finalWorldFrame[x][y + 3].equals(Tileset.NOTHING);
    }

    public boolean canGoRight(TETile[][] finalWorldFrame, int x, int y) {
        return x + 5 < WIDTH && finalWorldFrame[x + 1][y].equals(Tileset.NOTHING)
            && finalWorldFrame[x + 2][y].equals(Tileset.NOTHING) 
            && finalWorldFrame[x + 3][y].equals(Tileset.NOTHING)
            && finalWorldFrame[x + 4][y].equals(Tileset.NOTHING) 
            && finalWorldFrame[x + 5][y].equals(Tileset.NOTHING);
    }

    public boolean canGoDown(TETile[][] finalWorldFrame, int x, int y) {
        return y > 3 && finalWorldFrame[x][y - 1].equals(Tileset.NOTHING)
            && finalWorldFrame[x][y - 2].equals(Tileset.NOTHING) 
            && finalWorldFrame[x][y - 3].equals(Tileset.NOTHING);
    }

    public boolean canBuildUp(TETile[][] finalWorldFrame, House house) {
        int count = 0;
        int currX = house.cornorX();
        int currY = house.cornorY() + house.height();
        while (currX < house.width() + house.cornorX()) {
            if (canGoUp(finalWorldFrame, currX, currY)) {
                count++;
            } else {
                count = 0;
            }
            currX++;
        }

        return count >= 3;
    }

    public boolean canBuildRight(TETile[][] finalWorldFrame, House house) {
        int count = 0;
        int currX = house.cornorX() + house.width();
        int currY = house.cornorY();
        while (currY < house.height() + house.cornorY()) {
            if (canGoRight(finalWorldFrame, currX, currY)) {
                count++;
            } else {
                count = 0;
            }
            currY++;
        }

        return count >= 3;
    }

    public boolean canBuildDown(TETile[][] finalWorldFrame, House house) {
        int count = 0;
        int currX = house.cornorX();
        int currY = house.cornorY();
        while (currX < house.width() + house.cornorX()) {
            if (canGoDown(finalWorldFrame, currX, currY)) {
                count++;
            } else {
                count = 0;
            }
            currX++;
        }

        return count >= 3;
    }


    private boolean isWall(TETile[][] world, int col, int row) {
        return world[col][row].equals(Tileset.WALL);
    }

    private boolean isfloor(TETile[][] world, int col, int row) {
        return world[col][row].equals(Tileset.FLOOR);
    }


    public static TETile[][] createNewWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        int x = 0;
        int y = 0;
        while (x < WIDTH) {
            while (y < HEIGHT) {
                world[x][y] = Tileset.NOTHING;
                y++;
            }
            x++;
            y = 0;
        }


        return world;
    }



    public TETile[][] worldFrame() {
        return worldFrame;
    }
    public Avatar me() {
        return me;
    }
    public ArrayList<House> fullHouses() {
        return fullhouses;
    }
    public Random random() {
        return random;
    }
    public long timePassed() {
        return timePassed;
    }
    public void setTimePassed(long time) {
        this.timePassed += time;
    }
    public boolean light() {
        return light;
    }    
    public void setLight() {
        light = !light;
    }



    public class House implements Serializable {
        private int cornorX, cornorY;
        private int width, height;
        private int[][] door;
        private boolean isHallway;
        // private boolean collide;


        public House(int X, int Y, int width, int height, boolean isHallway, int[] dooR) {
            this.cornorX = X;
            this.cornorY = Y;
            this.width = width;
            this.height = height;
            this.isHallway = isHallway;
            this.door = new int[2][2];
            this.door[0] = dooR;

                // this.collide = false;
        }
        public int width() {
            return width;
        }
        public int height() {
            return height;
        }
        public int cornorX() {
            return cornorX;
        }
        public int cornorY() {
            return cornorY;
        }
        public int[][] door() {
            return door;
        }


        public void addDoor(int[] dooR) {
            this.door[1] = dooR;
        }
        public boolean checkoutOfFrame(int frameWidth, int frameheight) {
            return cornorX + width <= frameWidth && cornorY + height <= frameheight;
        }

        public boolean checkCollide(House other) {
            return other.cornorY >= this.cornorY - other.height()
                    && other.cornorX >= this.cornorX - other.width()
                    && other.cornorX <= this.cornorX + width
                    && other.cornorY <= this.cornorY + height;
        }

        public int[] openSouth(int numDoor) {
            int doorX = RandomUtils.uniform(random, cornorX + 1, cornorX + width);
            door[numDoor] = new int[]{doorX, cornorY};
            return door[numDoor];
        }


        public int[] openNorth(int numDoor) {
            int doorX = RandomUtils.uniform(random, cornorX + 1, cornorX + width - 1);
            door[numDoor] = new int[]{doorX, cornorY + height};
            return door[numDoor];
        }

        public int[] openWest(int numDoor) {
            int doorY = RandomUtils.uniform(random, cornorY + 1, cornorY + height - 1);
            door[numDoor] = new int[]{cornorX, doorY };
            return door[numDoor];
        }

        public int[] openEast(int numDoor) {
            int doorY = RandomUtils.uniform(random, cornorY + 1, cornorY + height - 1);
            door[numDoor] = new int[]{cornorX + width, doorY};
            return door[numDoor];
        }


        public boolean inHouse(int X, int Y) {
            return X <= cornorX + width - 1 && X >= cornorX + 1 
                    && Y <= cornorY + height - 1 && Y >= cornorY + 1;
        }

        public int[] randomPosInHouse() {
            int X = RandomUtils.uniform(random, cornorX + 1,
                cornorX + width);
            int Y = RandomUtils.uniform(random, cornorY + 1, 
                cornorY + height);
            return new int[]{X, Y};
        }



        @Override
        public String toString() {
            return "cornorX: " + cornorX + ", cornorY: " + cornorY 
                    + ", width: " + width + ", height: " + height;
        }

        public void drawHouse(TETile[][] world) {
//            System.out.print(this);
            for (int col = cornorX; col <= cornorX + width; col++) {
                for (int row = cornorY; row <= cornorY + height; row++) {

                    if (col == cornorX || row == cornorY 
                            || col == cornorX + width || row == cornorY + height) {
                        world[col][row] = Tileset.WALL;
                    } else {
                        world[col][row] = Tileset.FLOOR;
                    }
                }
            }
//
//            for(int[] d: this.door) {
//                if (d != null) {
//                    world[d[0]][d[1]] = Tileset.FLOOR;
//                }
//            }
            for (int i = 0; i < this.door.length; i++) {
                if (door[i] != null) {
                    world[door[i][0]][door[i][1]] = Tileset.FLOOR;
                }
            }
        }
    }

    public Engine() {
        timePassed = 0;
        light = false;
    }

}









