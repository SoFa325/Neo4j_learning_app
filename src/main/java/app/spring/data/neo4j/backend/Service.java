package app.spring.data.neo4j.backend;

import com.google.gson.Gson;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Service implements AutoCloseable{
    private final Driver driver;
    private Gson gson = new Gson();
    public Service(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
    @Override
    public void close() throws RuntimeException {
        driver.close();
    }
    public Graph sol_query(final String q) {
        String str = "";
        StringBuilder stringBuilder = new StringBuilder();
        Graph gr = new Graph();
        Graph gr1 = new Graph();
        try (var session = driver.session()) {
            var greeting = session.executeWrite(tx -> {
                var query = new Query(q);
                var result = tx.run(query);
                while ( result.hasNext() ) {
                    Record record = result.next();
                    List<String> keys = record.keys();
                    for (int i = 0; i < keys.size(); i++){
                        String key = keys.get(i);
                        String skey = "";
                        if (gson.toJson(record.get(key)).contains("labels")){
                            skey = gson.toJson(record.get(key).asNode());
                            Node n = this.get_node(skey);
                            Vertex v = new Vertex(n.get_elementId(), n.get_label(), n.make_Properites(), n.make_FullProperties());
                            gr.add_vertex(v);
                        }else{
                            skey = gson.toJson(record.get(key).asRelationship());
                            Relationship r = this.get_rel(skey);
                            Eadge e = new Eadge(r.get_elementId(), r.get_type(), r.get_startElementId(), r.get_endElementId(), r.make_Properites());
                            gr.add_eadge(e);
                        }

                    }
                }
                gr.preinitGraph(5);
                gr.set_info();
                return gr;
            });

            gr1 = greeting;
        }catch (Exception e) {
            str = e.toString();
        }
        if (str.equals("")) {
            str = stringBuilder.toString();
            if (str.equals("")){
                str = "Done Successfully!";
            }
            //gr1.info = str;
        }
        if (gr1.eadges.size() == 0 && gr1.vertexes.size() ==0){
            gr1 = this.get_all_data();
            gr1.info = str;
        }
        return gr1;
    }

    public Graph get_all_data(){
        return this.sol_query("Match (n)-[r]-(m) Return n,r,m");
    }

    public void reboot(){
        ArrayList<String> ar = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("bd_examples.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Пропускаем строки, состоящие только из пробелов
                if (!line.trim().isEmpty()) {
                   ar.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }

        try (var session = driver.session()) {
            var greeting = session.executeWrite(tx -> {

                var query1 = new Query("MATCH ()-[r]->() DELETE r;");
                var result1 = tx.run(query1);
                var query2 = new Query("MATCH (n) DELETE n;");
                var result2 = tx.run(query2);
                for (int i = 0; i < ar.size(); i++){
                    var query = new Query(ar.get(i));
                    var result = tx.run(query);
                }
                return -1;
            });

        }catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public Node get_node(String info){
        Node n = new Node();
        var s = info.split("\"|,|}|]");
        ArrayList<String> str = new ArrayList<>();
        for (int i = 0; i < s.length; i++){
            if (s[i].length() >= 1 && !s[i].equals(":") && !s[i].equals(":{") && !s[i].equals(":[") && !s[i].equals("{")) {
                if (s[i].charAt(0) == ':'){
                    str.add(s[i].substring(1));
                }
                str.add(s[i]);
            }
        }
        if (str.size() >=2) {
            n.set_label(str.get(1));
        }
        if (str.size() >=7) {
            n.set_elementId(str.get(6));
        }
        for (int i = 8; i < str.size(); i++){
            if (!str.get(i).equals("val") && !str.get(i-1).equals("val")) {
                n.properties_names.add(str.get(i));
                //System.out.println(str.get(i));
            }
            if (str.get(i-1).equals("val")){
                n.properties_vals.add(str.get(i));
               // System.out.println(str.get(i));
            }
        }
        return n;
    }

    public Relationship get_rel(String info){
        Relationship n = new Relationship();
        var s = info.split("\"|,|}|]");
        ArrayList<String> str = new ArrayList<>();
        for (int i = 0; i < s.length; i++){
            if (s[i].length() >= 1 && !s[i].equals(":") && !s[i].equals(":{") && !s[i].equals(":[") && !s[i].equals("{")) {
                if (s[i].charAt(0) == ':'){
                    str.add(s[i].substring(1));
                } else {
                    str.add(s[i]);
                }
            }

        }
        //System.out.println(" ");
        if (str.size() >=4) {
            n.set_startElementId(str.get(3));
        }
        //System.out.println("start: "+ n.get_startElementId());
        if (str.size() >=8) {
            n.set_endElementId(str.get(7));
        }
        //System.out.println("end: "+n.get_endElementId());
        if (str.size() >=10) {
            n.set_type(str.get(9));
        }
        //System.out.println("type: "+n.get_type());
        if (str.size() >=14) {
            n.set_elementId(str.get(13));
        }
        //System.out.println("id: "+n.get_elementId());
        for (int i = 15; i < str.size(); i++){
            if (!str.get(i).equals("val") && !str.get(i-1).equals("val")) {
                n.properties_names.add(str.get(i));
                //System.out.println(str.get(i));
            }
            if (str.get(i-1).equals("val")){
                n.properties_vals.add(str.get(i));
                // System.out.println(str.get(i));
            }
            //System.out.println(str.get(i));
        }
        //System.out.println(" ");
        return n;
    }


}
