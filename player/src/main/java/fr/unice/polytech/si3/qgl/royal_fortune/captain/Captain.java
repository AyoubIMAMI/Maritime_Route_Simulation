package fr.unice.polytech.si3.qgl.royal_fortune.captain;

import fr.unice.polytech.si3.qgl.royal_fortune.Checkpoint;
import fr.unice.polytech.si3.qgl.royal_fortune.Goal;
import fr.unice.polytech.si3.qgl.royal_fortune.Sailor;
import fr.unice.polytech.si3.qgl.royal_fortune.action.Action;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Position;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.Ship;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.entities.Oar;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.entities.Rudder;
import fr.unice.polytech.si3.qgl.royal_fortune.ship.shape.Circle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Captain {
    private Ship ship;
    private Goal goal;
    private List<Sailor> sailors;
    private FictitiousCheckpoint fictitiousCheckpoints;
    private ArrayList<Action> roundActions;
    private DirectionsManager directionsManager;
    final Logger logger = Logger.getLogger(Captain.class.getName());

    public Captain(Ship ship, List<Sailor> sailors, Goal goal, FictitiousCheckpoint fictitiousCheckpoints){
        this.ship = ship;
        this.sailors = sailors;
        this.goal = goal;
        this.fictitiousCheckpoints = fictitiousCheckpoints;
        roundActions = new ArrayList<>();
        directionsManager = new DirectionsManager(ship, fictitiousCheckpoints);
    }
    public Captain(){}


    public String roundDecisions() {

        disassociate();
        roundActions.clear();
        updateCheckPoint();
        double angleMove = directionsManager.getAngleMove();
        double angleCone = directionsManager.getAngleCone();
        double angleMadeBySailors = 0;
        double signOfAngleMove = (angleMove/Math.abs(angleMove));

        if (!directionsManager.isConeTooSmall(angleMove, angleCone) && !directionsManager.isInCone(angleMove, angleCone)) {
            angleMadeBySailors = associateSailorToOar(angleMove);
        }

        if(-Math.PI/4 <= angleMove - angleMadeBySailors && angleMove - angleMadeBySailors <= Math.PI/4 && Math.abs(angleMove - angleMadeBySailors)>Math.pow(10,-3)) {
            askSailorToMoveToRudder();
            askSailorsToTurnWithRudder(angleMove - angleMadeBySailors);
        }
        else if(angleMove - angleMadeBySailors < -Math.PI/4 || Math.PI/4 < angleMove - angleMadeBySailors) {
            askSailorToMoveToRudder();
            askSailorsToTurnWithRudder(signOfAngleMove*Math.PI/4);
        }

        associateSailorToOarEvenly();
        askSailorsToMove();
        askSailorsToOar();


        StringBuilder actionsToDo = new StringBuilder();
        for(Action action : roundActions)
            actionsToDo.append(action.toString()).append(",");
        String out = actionsToDo.substring(0, actionsToDo.length() - 1);
        return "[" + out + "]";
    }

    private void updateCheckPoint() {
        if (isInCheckpoint(goal.getCurrentCheckPoint()))
        {goal.nextCheckPoint();
            fictitiousCheckpoints.nextCheckPoint();}
        }
    private boolean isInCheckpoint(Checkpoint checkpoint) {
        return(isInCheckpointShipPos(checkpoint,ship.getPosition().getX(),ship.getPosition().getY()));
    }

    private boolean isInCheckpointShipPos(Checkpoint checkpoint,double shipX,double shipY) {
        double distanceSCX = checkpoint.getPosition().getX() - shipX;
        double distanceSCY = checkpoint.getPosition().getY() - shipY;
        double distanceSC = Math.sqrt(Math.pow(distanceSCX,2) + Math.pow(distanceSCY,2));
        double radius=((Circle)checkpoint.getShape()).getRadius();
        return(distanceSC<=radius);
    }

    private void disassociate() {
        sailors.forEach(sailor -> sailor.setTargetEntity(null));
    }

    /**
     * Captain will associate the best number of sailors to proceed a rotation of the given angle.
     * @param orientation The rotation of the given angle.
     */
    public double associateSailorToOar(double orientation){
        int maxSailors = Math.abs((int) Math.ceil(orientation/(Math.PI / ship.getNbrOar())));
        List<Oar> oarList = ship.getOarList(orientation < 0 ? "right" : "left");
        int i = 0;

        // We continue associating until we run out of sailors or oars
        while(i < oarList.size() && i < sailors.size() && i < maxSailors){
            Oar oar = oarList.get(i);
            logger.info(String.valueOf(oar));
            sailors.get(i).setTargetEntity(oar);
            oar.setSailor(sailors.get(i));
            i++;
        }

        return i*orientation/Math.abs(orientation)*(Math.PI/ship.getNbrOar());
    }

    /**
     * Associate the same amount of sailors to the left oars and the right oars of the ship.
     */
    public void associateSailorToOarEvenly(){
        List<Oar> leftOarList = ship.getOarList("left");
        List<Oar> rightOarList = ship.getOarList("right");
        int oarIndex = 0;
        int sailorIndex = 0;
        ArrayList<Sailor> listOfUnassignedSailors = (ArrayList<Sailor>) sailors.stream()
                .filter(sailor-> sailor.getTargetEntity()==null)
                .collect(Collectors.toList());

        // We continue associating until we run out of sailors or oars
        while(oarIndex < leftOarList.size() && oarIndex < rightOarList.size() && sailorIndex + 1 < listOfUnassignedSailors.size()&&needSailorToOar(sailorIndex)){
            Oar leftOar = leftOarList.get(oarIndex);
            Oar rightOar = rightOarList.get(oarIndex);
            listOfUnassignedSailors.get(sailorIndex).setTargetEntity(leftOar);
            leftOar.setSailor(listOfUnassignedSailors.get(sailorIndex));
            listOfUnassignedSailors.get(++sailorIndex).setTargetEntity(rightOar);
            rightOar.setSailor(listOfUnassignedSailors.get(sailorIndex));
            sailorIndex++;
            oarIndex++;
        }
    }
    public boolean needSailorToOar(int numberOfCoples){
        int norme=165*numberOfCoples/ship.getNbrOar();
        double newX=ship.getPosition().getX();
        double newY= ship.getPosition().getY();
        double angleCalcul=ship.getPosition().getOrientation();
        newX+=norme*Math.cos(angleCalcul);
        newY+=norme*Math.sin(angleCalcul);
        return !isInCheckpointShipPos(fictitiousCheckpoints.getCurrentCheckPoint(),newX,newY);
    }

    /**
     * Will ask the nearest sailor to the rudder to move to.
     */
    public void askSailorToMoveToRudder(){
        Rudder rudder = ship.getRudder();
        if (rudder == null)
            return;

        Optional<Sailor> sailorToMove = sailors.stream()
                .filter(sailor -> sailor.getTargetEntity() == null)
                .min(Comparator.comparingInt(sailor -> sailor.getDistanceToEntity(rudder)));

        if(sailorToMove.isPresent()) {
            Sailor s = sailorToMove.get();
            s.setTargetEntity(rudder);
            roundActions.add(s.moveToTarget());
        }
    }

    /**
     * Ask all sailors associated to an Entity to move to
     * If the sailor is already on the Entity, sailor will not move
     * This method update list of Action (roundActions)
     */
    public void askSailorsToMove(){
        roundActions.addAll(sailors.stream()
                .filter(sailor -> sailor.getTargetEntity() != null)
                .filter(sailor -> !sailor.isOnTheTargetEntity())
                .map(Sailor::moveToTarget)
                .toList());
    }

    /**
     * Ask all sailors associated to an Oar to oar
     * This method update list of Action (roundActions)
     */
    public void askSailorsToOar(){
        roundActions.addAll(sailors.stream()
                .filter(sailor -> sailor.getTargetEntity() != null)
                .filter(sailor-> sailor.getTargetEntity().isOar())
                .filter(Sailor::isOnTheTargetEntity)
                .map(Sailor::oar)
                .toList());
    }

    /**
     * Ask a sailor to turn with the rudder
     * This method update list of Action (roundActions)
     * @param rotationRudder
     */
    void askSailorsToTurnWithRudder(double rotationRudder) {
        roundActions.addAll(sailors.stream()
                .filter(sailor -> sailor.getTargetEntity() instanceof Rudder)
                .filter(Sailor::isOnTheTargetEntity)
                .map(sailor -> sailor.turnWithRudder(rotationRudder))
                .toList());
    }

    public List<Action> getRoundActions(){
        return roundActions;
    }
}
