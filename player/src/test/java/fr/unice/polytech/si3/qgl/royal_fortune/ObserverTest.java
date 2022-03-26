package fr.unice.polytech.si3.qgl.royal_fortune;

import fr.unice.polytech.si3.qgl.royal_fortune.calculus.Cartologue;
import fr.unice.polytech.si3.qgl.royal_fortune.calculus.Mathematician;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.SeaEntities;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.Stream;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.shape.Rectangle;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;
import fr.unice.polytech.si3.qgl.royal_fortune.target.Beacon;
import fr.unice.polytech.si3.qgl.royal_fortune.target.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObserverTest {
    List<SeaEntities> currentSeaEntities;
    Position shipPosition;
    Position nextCheckPointPosition;
    Observer observer;

    @BeforeEach
    void init(){
        currentSeaEntities=new ArrayList<>();
        shipPosition=new Position(0,0,0);
        nextCheckPointPosition=new Position(1000,0,0);
        observer=new Observer();
    }

    @Test
    void watchSeaTest(){
        Stream stream=new Stream(new Position(500,0,0),new Rectangle(100,100,Math.PI),100);
        currentSeaEntities.add(stream);
        Beacon beacon=observer.watchSea(currentSeaEntities).get();
        assertTrue(observer.watchSea(currentSeaEntities).isPresent());
    }
}
