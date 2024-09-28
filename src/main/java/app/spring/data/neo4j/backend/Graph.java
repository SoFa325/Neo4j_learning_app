package app.spring.data.neo4j.backend;

import java.util.ArrayList;
import java.util.Random;

public class Graph {
    ArrayList<Eadge> eadges;
    ArrayList<Vertex> vertexes;

    ArrayList<String> ids;

    String info;

    public Graph(){
        this.eadges = new ArrayList<>();
        this.vertexes = new ArrayList<>();
        this.info = "";
    }

    public void fibonacci_disp(double l){
        double phi = Math.PI * (Math.sqrt(5.0) - 1.0);
        for (int i = 0; i < this.vertexes.size(); i++){
            double y = 1.0 - ((double)i/(double)(this.vertexes.size()-1)) * 2;
            //System.out.println(y);
            double r = Math.sqrt(1.0-y*y);
            double theta = phi*i;
            double x = Math.cos(theta)*r;
            double z = Math.sin(theta)*r;
            this.vertexes.get(i).pos.add(0, x*l);
            this.vertexes.get(i).pos.add(1, y*l);
            this.vertexes.get(i).pos.add(2, z*l);
            //System.out.println(this.vertexes.get(i).pos);
        }

    }

    public void rand_disp(){
        Random r = new Random();
        for (int i = 0; i < this.vertexes.size(); i++){
            double x = r.nextDouble();
            double y = r.nextDouble();
            double z = r.nextDouble();
            double t = r.nextInt(100);
            if (t % 2 == 0){
                x = -x;
            }
            t = r.nextInt(100);
            if (t % 2 == 0){
                y = -y;
            }
            t = r.nextInt(100);
            if (t % 2 == 0){
                z = -z;
            }
            this.vertexes.get(i).pos.add(0, x);
            this.vertexes.get(i).pos.add(1, y);
            this.vertexes.get(i).pos.add(2, z);
        }
    }

    public void add_vertex(Vertex v){
        boolean flag = false;
        for(int i = 0; i < this.vertexes.size(); i++){
            if (vertexes.get(i).elem_id.equals(v.elem_id)){
                flag = true;
            }
        }
        if (!flag) {
            this.vertexes.add(v);
        }
    }

    public void add_eadge(Eadge e){
        boolean flag = false;
        //System.out.println(e.type);
        for(int i = 0; i < this.eadges.size(); i++){
            if (eadges.get(i).elem_id.equals(e.elem_id)){
                flag = true;
            }
        }
        if (!flag) {
            this.eadges.add(e);
        }

    }

    public double vec_len(ArrayList<Double> p){
        double res = Math.sqrt(p.get(0)*p.get(0) + p.get(1)*p.get(1) + p.get(2)*p.get(2));
        if (Math.abs(res) < 0.0001){
            res = 0.001;
        }
        return res;

    }

    public ArrayList<Double> f_magn(ArrayList<Double> pu, ArrayList<Double> pv){
        ArrayList<Double> res = new ArrayList<>();
        double c_magn = 1.0;
        double alpha = 1.0;
        double beta = 1.0;
        double b = 8.0;
        ArrayList<Double> pvpu = new ArrayList<>();
        for(int i = 0; i < pu.size(); i++){
            pvpu.add(i, pv.get(i)-pu.get(i));
        }
        double d = vec_len(pvpu);
        ArrayList<Double> m_vec = m(pvpu);
        double theta = Math.acos((m_vec.get(0)*pvpu.get(0) + m_vec.get(1)*pvpu.get(1) + m_vec.get(2)*pvpu.get(2))/(vec_len(m_vec)*vec_len(pvpu)));
        for(int i = 0; i < pu.size(); i++){
            res.add(i, c_magn*b*Math.pow(d, alpha)*Math.pow(theta, beta));
        }
        return res;
    }

    public ArrayList<Double> f_rep(ArrayList<Double> pu, ArrayList<Double> pv){
        ArrayList<Double> res = new ArrayList<>();
        double c_rep = 1.0;
        ArrayList<Double> pvpu = new ArrayList<>();
        for(int i = 0; i < pu.size(); i++){
            pvpu.add(i, pv.get(i)-pu.get(i));
        }
        double pvpu_norm = vec_len(pvpu);
        //System.out.println(pvpu_norm);
        for(int i = 0; i < pu.size(); i++){
            res.add(i, (c_rep/(pvpu_norm*pvpu_norm)) * (pvpu.get(i)/pvpu_norm));
        }
        return res;
    }

    public ArrayList<Double> f_attr(ArrayList<Double> pu, ArrayList<Double> pv, double l){
        ArrayList<Double> res = new ArrayList<>();
        double c_attr = 2.0;
        ArrayList<Double> pvpu = new ArrayList<>();
        for(int i = 0; i < pu.size(); i++){
            pvpu.add(i, pv.get(i)-pu.get(i));
        }

        double pvpv_norm = vec_len(pvpu);
        for(int i = 0; i < pu.size(); i++){
            res.add(i, (c_attr*Math.log(pvpv_norm/l)) * (pvpu.get(i)/pvpv_norm));

        }
        return res;
    }

    public ArrayList<Double> m(ArrayList<Double> xyz){
        double l = vec_len(xyz);
        ArrayList<Double> res = new ArrayList<>();
        res.add(0, xyz.get(1)/l);
        res.add(1, -xyz.get(0)/l);
        res.add(2, xyz.get(2)/l);
        return res;
    }

    public void magnetic_spring_alg(double l, int k){
        fibonacci_disp(l);
        //rand_disp();
        for (int i = 0; i < k; i++){
            for (int j = 0; j < this.vertexes.size(); j++){
                Vertex u = this.vertexes.get(j);
                for (int a = 0; a < u.connected_vertexes.size(); a++){
                    if (a != j) {
                        if (u.connected_vertexes.get(a)) {
                            Vertex v = this.vertexes.get(a);
                            ArrayList<Double> new_f= f_attr(u.pos, v.pos, l);
                            //System.out.println("attr " + new_f);
                            ArrayList<Double> new_magn_f = f_magn(u.pos, v.pos);
                            //System.out.println("magn " + new_magn_f);
                            for (int y = 0; y < new_f.size();y++){
                                u.F.set(y, u.F.get(y) + new_f.get(y)+ new_magn_f.get(y));
                            }
                            //u.F.get(0) += new_f.get(0);
                        } else{
                            Vertex v = this.vertexes.get(a);
                            ArrayList<Double> new_f= f_rep(u.pos, v.pos);
                            //System.out.println("rep " + new_f);
                            for (int y = 0; y < new_f.size();y++){
                                u.F.set(y, u.F.get(y) - new_f.get(y));
                            }
                        }
                    }
                }
                this.vertexes.set(j, u);
            }
            for (int j = 0; j < this.vertexes.size(); j++){
                double sigm = 0.001;
                //System.out.println(this.vertexes.get(j).F);
                for (int a = 0; a < this.vertexes.get(j).pos.size(); a++){

                    this.vertexes.get(j).pos.set(a, this.vertexes.get(j).pos.get(a) +  sigm*this.vertexes.get(j).F.get(a));
                    //this.vertexes.get(j).pos.set(a, Math.min(AA, Math.max(-AA,this.vertexes.get(j).pos.get(a))));

                }
                //System.out.println(this.vertexes.get(j).pos);
            }
        }
    }

    public void preinitGraph(int k){
        double l = 0.1*this.vertexes.size();
        this.ids = new ArrayList<>();
        for (int i = 0; i < this.vertexes.size(); i++){
            this.vertexes.get(i).index = i;
            ids.add(i, this.vertexes.get(i).elem_id);
            for(int j = 0; j < this.vertexes.size(); j++){
                this.vertexes.get(i).connected_vertexes.add(j, false);
            }
        }
        if (this.vertexes.size() > 0) {
            for (int i = 0; i < this.eadges.size(); i++) {
                this.eadges.get(i).start_id = ids.indexOf(this.eadges.get(i).element_startid);
                this.eadges.get(i).end_id = ids.indexOf(this.eadges.get(i).element_endid);
                //System.out.println(this.vertexes.get(this.eadges.get(i).start_id).properties + "->" + this.vertexes.get(this.eadges.get(i).end_id).properties);
                this.vertexes.get(this.eadges.get(i).start_id).connected_vertexes.set(this.eadges.get(i).end_id, true);
            }
            magnetic_spring_alg(l, k);
        }
        //double AA = 5.0;

    }

    public ArrayList<String> get_vertex_pos(){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < this.vertexes.size(); i++){
            res.add(this.vertexes.get(i).get_pos());
        }
        return res;
    }

    public ArrayList<String> get_vertex_properties(){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < this.vertexes.size(); i++){
            res.add(this.vertexes.get(i).label + "\n" + this.vertexes.get(i).properties);
        }
        return res;
    }

    public ArrayList<String> get_eadge_properites(){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < this.eadges.size(); i++){
            res.add(this.eadges.get(i).type + "\n" + this.eadges.get(i).properties);
        }
        return res;
    }
    public ArrayList<String> get_eadge_color(){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < this.eadges.size(); i++){
            String t = this.eadges.get(i).type;
            if (t.equals("FRIENDS")){
                res.add("0x2db414");
            } else if (t.equals("HATES")){
                res.add("0xe9353a");
            } else{
                res.add("0xb35aed");
            }

        }
        return res;
    }

    public ArrayList<String> get_vertex_color(){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < this.vertexes.size(); i++){
            String t = this.vertexes.get(i).label;
            if (t.equals("Person")){
                res.add("#8affd9");
            } else if (t.equals("Group")){
                res.add("#ff96cb");
            } else {
                res.add("#af3763");
            }

        }
        return res;
    }

    public ArrayList<String> get_eadge_orientation(){
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < this.eadges.size(); i++){
            res.add("" + this.eadges.get(i).start_id + " " + this.eadges.get(i).end_id);
        }
        return res;
    }

    public void set_info() {
        this.info = "";
        for (int i = 0; i < this.vertexes.size(); i++) {
            Vertex u = this.vertexes.get(i);
            int l = this.info.length();
            for (int j = 0; j < this.eadges.size(); j++) {
                if (this.eadges.get(j).start_id == i){
                    info += u.info()+  " " + this.eadges.get(j).info() + " " + this.vertexes.get(this.eadges.get(j).end_id).info() + "\n";
                }
            }
            if (l == this.info.length()){
                this.info += u.info() + "\n";
            }
        }

        if (this.vertexes.size() == 0){
            for (int j = 0; j < this.eadges.size(); j++) {

                info += this.eadges.get(j).info()  + "\n";

            }
        }
    }
}
