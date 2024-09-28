package app.spring.data.neo4j.backend;

import java.util.ArrayList;

public class Relationship {
    String startElementId;
    String endElementId;
    String type;
    String elementId;
    ArrayList<String> properties_names;
    ArrayList<String> properties_vals;
    public Relationship(){
        this.properties_vals = new ArrayList<>();
        this.properties_names = new ArrayList<>();
    }

    public void set_type(String t){
        this.type = t;
    }

    public void set_elementId(String eID){
        this.elementId = eID;
    }

    public void set_startElementId(String eID){
        this.startElementId = eID;
    }

    public void set_endElementId(String eID){
        this.endElementId = eID;
    }

    public String get_type(){
        return this.type;
    }

    public String get_elementId(){
        return this.elementId;
    }

    public String get_startElementId(){
        return this.startElementId;
    }

    public String get_endElementId(){
        return this.endElementId;
    }

    public String make_Properites(){
        String res = "";
        if (this.properties_names.size() > 0) {

                res = this.properties_names.get(0) + ": " + this.properties_vals.get(0);
            //res += "\n";
        }
        return res;
    }

}
