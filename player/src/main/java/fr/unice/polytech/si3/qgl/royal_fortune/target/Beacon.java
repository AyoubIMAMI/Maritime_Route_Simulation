package fr.unice.polytech.si3.qgl.royal_fortune.target;

import fr.unice.polytech.si3.qgl.royal_fortune.environment.Checkpoint;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.shape.Circle;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.shape.Shape;

public class Beacon extends Checkpoint {
    public static int RADIUSBEACON=150;

    public Beacon (Position position){
        super(position,new Circle(RADIUSBEACON));
    }
}
