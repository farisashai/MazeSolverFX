package projMAZE;

class Box {

    private boolean left, right, up, down, visited, onPath;

    Box() {
        left = true;
        right = true;
        up = true;
        down = true;
    }

    void setLeft(boolean left) {
        this.left = left;
    }
    void setRight(boolean right) {
        this.right = right;
    }
    void setUp(boolean up) {
        this.up = up;
    }
    void setDown(boolean down) {
        this.down = down;
    }
    void setVisited(boolean visited) {
        this.visited = visited;
    }
    void setOnPath(boolean onPath) {
        this.onPath = onPath;
    }
    boolean isLeft() {
        return left;
    }
    boolean isRight() {
        return right;
    }
    boolean isUp() {
        return up;
    }
    boolean isDown() {
        return down;
    }
    boolean isVisited() {
        return visited;
    }
    boolean isOnPath() {
        return onPath;
    }
}