package projMAZE;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class Maze {

    static int WIDTH = 12, HEIGHT = 8, BOXSIZE = 90;
    private Box[][] mazeArray;

    Maze() {
        mazeArray = new Box[WIDTH][HEIGHT];
        resetMaze();
    }
    void generateMaze() {
        resetMaze();
        Stack<Integer[]> stack = new Stack<>();
        stack.push(new Integer[]{0,0});
        while (stack.size() > 0) {
            if (stack.size() > 1 && Math.random() < 0.05) {
                Integer[] top = stack.pop();
                Integer[] currentSpot = stack.pop();
                mazeArray[currentSpot[0]][currentSpot[1]].setVisited(true);
                List<Integer[]> neighbors = neighborsIgnoreWalls(currentSpot[0], currentSpot[1]);
                if (neighbors.size() > 0) {
                    Integer[] nextSpot = neighbors.get((int) (Math.random() * neighbors.size()));
                    if (currentSpot[0] - 1 == nextSpot[0]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setLeft(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setRight(false);
                    } else if (currentSpot[0] + 1 == nextSpot[0]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setRight(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setLeft(false);
                    } else if (currentSpot[1] - 1 == nextSpot[1]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setUp(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setDown(false);
                    } else if (currentSpot[1] + 1 == nextSpot[1]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setDown(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setUp(false);
                    }
                    stack.push(currentSpot);
                    stack.push(nextSpot);
                }
                stack.push(top);
            } else {
                Integer[] currentSpot = stack.pop();
                mazeArray[currentSpot[0]][currentSpot[1]].setVisited(true);
                List<Integer[]> neighbors = neighborsIgnoreWalls(currentSpot[0], currentSpot[1]);
                if (neighbors.size() > 0) {
                    Integer[] nextSpot = neighbors.get((int) (Math.random() * neighbors.size()));
                    if (currentSpot[0] - 1 == nextSpot[0]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setLeft(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setRight(false);
                    } else if (currentSpot[0] + 1 == nextSpot[0]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setRight(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setLeft(false);
                    } else if (currentSpot[1] - 1 == nextSpot[1]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setUp(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setDown(false);
                    } else if (currentSpot[1] + 1 == nextSpot[1]) {
                        mazeArray[currentSpot[0]][currentSpot[1]].setDown(false);
                        mazeArray[nextSpot[0]][nextSpot[1]].setUp(false);
                    }
                    stack.push(currentSpot);
                    stack.push(nextSpot);
                }
            }
        }
        clearVisits();
    }
    void solveMaze() {
        clearVisits();
        Stack<Integer[]> path = new Stack<>();
        path.push(new Integer[]{0,0});
        while (path.peek()[0] != WIDTH - 1 || path.peek()[1] != HEIGHT - 1) {
            mazeArray[path.peek()[0]][path.peek()[1]].setVisited(true);
            mazeArray[path.peek()[0]][path.peek()[1]].setOnPath(true);
            while (neighbors(path.peek()[0],path.peek()[1]).size() == 0)
                mazeArray[path.peek()[0]][path.pop()[1]].setOnPath(false);
            List<Integer[]> neighbors = neighbors(path.peek()[0],path.peek()[1]);
            Integer[] nextSpot = neighbors.get((int) (neighbors.size()*Math.random()));
            path.push(nextSpot);
        }
        mazeArray[WIDTH-1][HEIGHT-1].setVisited(true);
        mazeArray[WIDTH-1][HEIGHT-1].setOnPath(true);
    }
    void resetMaze() {
        for (int i = 0; i < mazeArray.length; i++)
            for (int j = 0; j < mazeArray[0].length; j++)
                mazeArray[i][j] = new Box();
        mazeArray[0][0].setVisited(true);
        mazeArray[0][0].setOnPath(true);
        mazeArray[0][0].setLeft(false);
        mazeArray[WIDTH - 1][HEIGHT - 1].setRight(false);
    }
    void clearVisits() {
        for (Box[] aMazeArray : mazeArray)
            for (int j = 0; j < mazeArray[0].length; j++) {
                aMazeArray[j].setVisited(false);
                aMazeArray[j].setOnPath(false);
            }
        mazeArray[0][0].setVisited(true);
        mazeArray[0][0].setOnPath(true);
    }
    Box[][] getMaze() {
        return mazeArray;
    }
    List<Integer[]> neighbors(int x, int y) {
        List<Integer[]> spots = new ArrayList<>();
        if (x > 0 && !mazeArray[x][y].isLeft() && !mazeArray[x - 1][y].isVisited())
            spots.add(new Integer[]{x-1,y});
        if (x < WIDTH - 1 && !mazeArray[x][y].isRight() && !mazeArray[x + 1][y].isVisited())
            spots.add(new Integer[]{x+1,y});
        if (y > 0 && !mazeArray[x][y].isUp() && !mazeArray[x][y - 1].isVisited())
            spots.add(new Integer[]{x,y-1});
        if (y < HEIGHT - 1 && !mazeArray[x][y].isDown() && !mazeArray[x][y + 1].isVisited())
            spots.add(new Integer[]{x,y+1});
        return spots;
    }
    List<Integer[]> neighborsIgnoreWalls(int x, int y) {
        List<Integer[]> spots = new ArrayList<>();
        if (x > 0 && !mazeArray[x-1][y].isVisited())
            spots.add(new Integer[]{x-1,y});
        if (x < WIDTH - 1 && !mazeArray[x+1][y].isVisited())
            spots.add(new Integer[]{x+1,y});
        if (y > 0 && !mazeArray[x][y-1].isVisited())
            spots.add(new Integer[]{x,y-1});
        if (y < HEIGHT - 1 && !mazeArray[x][y+1].isVisited())
            spots.add(new Integer[]{x,y+1});
        return spots;
    }
    float percentExplored() {
        int boxes = 0;
        int total = WIDTH * HEIGHT;
        for (Box[] array: mazeArray)
            for (Box b: array)
                if (b.isVisited())
                    boxes++;
                return 100f*boxes/total;
    }
}
