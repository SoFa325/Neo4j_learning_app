package app.spring.data.neo4j.backend;

import java.util.ArrayList;

public class Vertex {
    String elem_id;
    String label;
    int index = -1;
    ArrayList<Double> pos;
    ArrayList<Boolean> connected_vertexes;

    ArrayList<Double> F;

    String properties;
    String full_properties;
     public Vertex(String elemid, String lbl, String prp, String fullprp){
         this.elem_id = elemid;
         this.label = lbl;
         this.properties = prp;
         this.full_properties = fullprp;
         this.index = -1;
         this.pos = new ArrayList<>();
         this.connected_vertexes = new ArrayList<>();
         this.F = new ArrayList<>();
         this.F.add(0, 0.0);
         this.F.add(1, 0.0);
         this.F.add(2, 0.0);

     }

     public String info(){
         return label + " " + full_properties.replaceAll("\n", ", ");
     }

     public String get_pos(){
         return "" + this.pos.get(0) + " " + this.pos.get(1) + " " + this.pos.get(2);
     }

}
