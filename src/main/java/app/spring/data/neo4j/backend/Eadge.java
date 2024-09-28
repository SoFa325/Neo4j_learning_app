package app.spring.data.neo4j.backend;

public class Eadge {
    String elem_id;
    String type;
    String properties;
    String element_startid;
    Integer start_id;
    Integer end_id;
    String element_endid;
    public Eadge(String elemid, String t, String sid, String eid, String prp){
        this.elem_id = elemid;
        this.type = t;
        this.element_startid = sid;
        this.element_endid = eid;
        this.properties = prp;
        //System.out.println(this.info());
    }

    public String info(){
        return type + " " + properties;
    }
}
