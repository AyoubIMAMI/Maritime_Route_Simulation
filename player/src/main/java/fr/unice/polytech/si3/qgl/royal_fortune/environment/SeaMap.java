package fr.unice.polytech.si3.qgl.royal_fortune.environment;

import fr.unice.polytech.si3.qgl.royal_fortune.target.Beacon;
import fr.unice.polytech.si3.qgl.royal_fortune.target.Goal;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;
import fr.unice.polytech.si3.qgl.royal_fortune.environment.shape.Circle;
import fr.unice.polytech.si3.qgl.royal_fortune.target.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SeaMap {
    private final Goal goal;
    private FictitiousCheckpoint fictitiousCheckpoints;
    private final Position shipPosition;
    private Wind wind;
    private List<SeaEntities> seaEntities;
    Observer observer;

    public SeaMap(Goal goal,FictitiousCheckpoint fictitiousCheckpoints,Position shipPosition,Wind wind,List<SeaEntities> seaEntities){
        this.goal=goal;
        this.fictitiousCheckpoints=fictitiousCheckpoints;
        this.shipPosition=shipPosition;
        this.wind=wind;
        this.seaEntities=seaEntities;
        observer=new Observer();
    }
    public void updateCheckPoint(List<SeaEntities> newSeaEntities) {
        if (isInCheckpoint(fictitiousCheckpoints.getCurrentCheckPoint()))
        {
            fictitiousCheckpoints.nextCheckPoint();
            this.seaEntities.clear();
        }

        observer.setNextCheckPointPosition(fictitiousCheckpoints.getCurrentCheckPoint().getPosition());
        observer.setShipPosition(shipPosition);
        if (observer.checkIfNewSeaEntities(newSeaEntities)){
            Optional<Beacon> beaconOptional=observer.watchSea(newSeaEntities);
            beaconOptional.ifPresent(beacon -> fictitiousCheckpoints.addFictitiousCheckpoint(beacon));
        }

    }
    public boolean isInCheckpoint(Checkpoint checkpoint) {
        return(isInCheckpointShipPos(checkpoint,shipPosition.getX(),shipPosition.getY()));
    }

    public boolean isInCheckpointShipPos(Checkpoint checkpoint,double shipX,double shipY) {
        double distanceSCX = checkpoint.getPosition().getX() - shipX;
        double distanceSCY = checkpoint.getPosition().getY() - shipY;
        double distanceSC = Math.sqrt(Math.pow(distanceSCX,2) + Math.pow(distanceSCY,2));
        double radius=((Circle)checkpoint.getShape()).getRadius();
        return(distanceSC<=radius);
    }

    public void setFictitiousCheckpoints(FictitiousCheckpoint fictitiousCheckpoints) {
        this.fictitiousCheckpoints=fictitiousCheckpoints;
    }

    public Checkpoint getCurrentFictitiousCheckPoint() {
        return(fictitiousCheckpoints.getCurrentCheckPoint());
    }

    public FictitiousCheckpoint getFictitiousCheckpoints() {
        return fictitiousCheckpoints;
    }

    public Checkpoint getCurrentFictitiousToSlowCheckPoint() {if (fictitiousCheckpoints.getCurrentCheckPoint() instanceof Beacon)
        return fictitiousCheckpoints.getFictitiousCheckpoints().get(1);
        else
            return fictitiousCheckpoints.getCurrentCheckPoint();
    }
}
