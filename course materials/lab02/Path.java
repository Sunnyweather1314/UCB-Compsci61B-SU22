/** A class that represents a path via pursuit curves. */
public class Path {
    private Point curr, next;

    // TODO
    public Path(double x, double y){
        this.next = new Point(x,y);
        this .curr =  new Point(0,0);
    }

    public double getCurrX(){
        return curr.getX();
    }
    public double getCurrY(){
        return curr.getY();
    }

    public double getNextX(){
        return next.getX();
    }
    public double getNextY(){
        return next.getY();
    }

    public Point getCurrentPoint(){
        return curr;
    }

    public void setCurrentPoint(Point point){
        curr = point;
    }

    public void iterate(double dx, double dy){

        curr.setX(next.getX());
        curr.setY(next.getY());
        double nextX = curr.getX()+dx;
        double nextY =curr.getY()+dy;
        next = new Point(nextX,nextY);
    }
}
