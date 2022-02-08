package fr.unice.polytech.si3.qgl.royal_fortune.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.unice.polytech.si3.qgl.royal_fortune.Sailor;

public class OarAction extends Action {
    public OarAction(Sailor sailor) {
        super(sailor, "OAR");
    }

    public OarAction(int sailorId,String type) {
        super.sailorId=sailorId;
        super.type=type;
    }
    public OarAction(){
    }
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode oarActionJSON = mapper.createObjectNode();
        oarActionJSON.put("sailorId", sailorId);
        oarActionJSON.put("type", "OAR");

        try {
            return mapper.writeValueAsString(oarActionJSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
