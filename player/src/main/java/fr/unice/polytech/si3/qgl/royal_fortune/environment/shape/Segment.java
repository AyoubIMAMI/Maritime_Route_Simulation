package fr.unice.polytech.si3.qgl.royal_fortune.environment.shape;

import fr.unice.polytech.si3.qgl.royal_fortune.calculus.Mathematician;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.text.html.Option;
import java.util.Optional;

public class Segment {
    private Position pointA;
    private Position pointB;
    private double length;

    //equation of the line
    private double a;
    private double b;

    public Segment(Position pointA, Position pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
        a=(pointA.getY()-pointB.getY())/(pointA.getX()-pointB.getX());
        b=pointA.getY()-a*pointA.getX();
        length=Math.sqrt(Math.pow(pointB.getY()-pointA.getY(),2)+Math.pow(pointB.getX()-pointA.getX(),2));
    }

    /**
     * Compute the intersection between this segment and an other segment
     * @param segment
     * @return the position of the intersection
     */
    public Optional<Position> computeIntersectionWith(Segment segment){
        double x=(b-segment.getB())/(a-segment.getA());
        double y=a*x+b;
        if ((Math.sqrt(Math.pow(pointB.getY()-y,2)+Math.pow(pointB.getX()-x,2)))<=length){
            return Optional.of(new Position(x,y));
        }
        else
            return Optional.empty();
    }

    /**
     * Calculate the angle between the segment and the orientation of a rectangle
     * @param rectangle
     * @return the angle between the segment and the orientation of a rectangle
     */
    public double angleIntersectionBetweenTwoLines(Rectangle rectangle) {
        double distanceSegmentExtremityX = pointB.getX() - pointA.getX();
        double distanceSegmentExtremityY = pointB.getY() - pointA.getY();
        double distanceSegmentExtremity = Mathematician.distanceFormula(pointA, pointB);
        double num = distanceSCX * Math.cos(angleShip) + distanceSCY * Math.sin(angleShip);

        double angleCone = Math.atan(radius / distanceSC);

        double angleMove = Math.acos(num / distanceSC);
        return 0;
    }

    public Position getPointA() {
        return pointA;
    }

    public Position getPointB() {
        return pointB;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }
}
