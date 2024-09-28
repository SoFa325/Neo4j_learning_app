package app.spring.data.neo4j.backend;

import java.util.ArrayList;

public class Node {
    String label;
    String elementId;
    ArrayList<String> properties_names;
    ArrayList<String> properties_vals;

    public Node(){
        this.properties_vals = new ArrayList<>();
        this.properties_names = new ArrayList<>();
    }

    public void set_label(String l){
        this.label = l;
    }

    public void set_elementId(String eID){
        this.elementId = eID;
    }

    public String get_label(){
        return this.label;
    }

    public String get_elementId(){
        return this.elementId;
    }

    public String make_Properites(){
        String res = "";
        if (this.properties_names.size() > 0) {
            if (this.label.equals("Person")) {
                if (this.properties_names.contains("name")){
                    res += "name: " + this.properties_vals.get(this.properties_names.indexOf("name"));
                }
                if (this.properties_names.contains("surname")){
                    res += " surname: " + this.properties_vals.get(this.properties_names.indexOf("surname"));
                }
            } else if (this.label.equals("Group")){
                res ="name: " + this.properties_vals.get(this.properties_names.indexOf("name"));
            } else{
                res = this.label + "\n" +  this.properties_names.get(0) + ": " + this.properties_vals.get(0);
            }
            //res += "\n";
        }
        return res;
    }

    public String make_FullProperties() {
        String res = "(";
        if (this.properties_names.size() > 0) {
           for (int i = 0; i < this.properties_vals.size(); i++){
                res += this.properties_names.get(i) + ": " + this.properties_vals.get(i) + ", ";
            }
            //res += "\n";
            res = res.substring(0, res.lastIndexOf(","));
        }
        res += ")";
        return res;
    }

}
