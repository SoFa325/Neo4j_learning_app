package app.spring.data.neo4j.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class AppController {
    //String names =  "";
    TextForms tf = new TextForms();
    private Service service;

    ArrayList<String> tasks = new ArrayList<>();

    public AppController() {
        this.service = new Service("neo4j+s://4f27c3c6.databases.neo4j.io", "neo4j", "cIAj3pH9n76obhrxIQWIvYl8YNHV4FIwbwLBtOjJmrg");//для локалки neo4jneo4j
        tasks.add(0, "Петя и Ваня хотят присоедениться к нашей социальной сети, необходимо помочь им с этим. Добавьте в базу данных узлы, описывающие Петю и Ваню, зная, что они хотят чтобы их имена были 'Petr' и 'Ivan' соответсвенно, фамилии 'Konstantinov' и 'Smirnov', а родились они 12.09.2004 и 31.05.2000 соответственно. Имена, фамилии и даты задавать строго, как указано в условии.");
        tasks.add(1, "В предыдущем задании вы добавили в социальную сеть двоих пользователей - Петю и Ваню. Петя кинул запрос в друзья Ване, а Ваня его принял. Соедините их узлы связью FRIENDS. Направление важно!");
        tasks.add(2, "Алиса и Маша узнали от Пети и Васи о нашей социальной сети и захотели добавиться. Также, Алиса решила сразу же кинуть запрос в друзья Маше. В данном задании необходимо одним CREATE запросом сделать то, что вы делали в предыдущих задачах - создать два узла с Алисой и Машей и связь FRIENDS между ними (P.s. их имена 'Alice' и 'Maria' соответсвенно, фамилии 'Fedorova' и 'Leygova' а родились они 10.11.2009 и 30.08.2010 соответственно).");
        tasks.add(3, "Ваня так же решил добавиться в друзья к Пете. Напишите запрос, помогающий ему с этим, избежав дублирования узлов и связей.");
        tasks.add(4, "Крайне импульсивный Олег решил присоединиться к нашей социальной сети и сразу же после этого вышел из нее. Воспроизведите две команды необходимые для данного сценария, зная, что Олег при регистрации указал лишь свое имя = Oleg.");
        tasks.add(5, "Петя и Ваня из первой задачи разругались друг с другом и Петя решил выйти из друзей Вани. Произведите удаление данного отношения FRIENDS.");
        tasks.add(6, "Ваня, узнав о поступке Пети решил выйти из социальной сети. Помогите ему с этим, удалив его узел.");
        tasks.add(7, "При регистрации группы 'Popular games' была допущена ошибка. Регистрирующий не знал, что пока он этим занимался на группу успело подписаться еще 100 человек. Исправьте его ошибку использовав лишь один запрос.");
        tasks.add(8, "Алиса из третьего задания решила, что другим пользователям знать ее возраст ни к чему и попросила вас убрать из базы данных информацию о ее дате рождения.");
        tasks.add(9, "Выведите все группы, число подписчиков которых больше 350.");
        tasks.add(10, "Выведите все узлы, соединённые ребром FOLLOWS, отсеяв все, у которых значение since меньше 2021 и больше 2014");
        tasks.add(11, "Выведите все группы, отсортировав их по возрастанию числа подписок");
        tasks.add(12, "Выведите все узлы, соединённые ребром.");
        tasks.add(13, "Выведите 20 узлов базы данных пропустив 5.");

    }

    @GetMapping("/task{num}")
    public String showpage(Model model, @PathVariable int num){
        //System.out.println(num);
        String task = "";
        String next_task = "";
        if (num <= this.tasks.size()){
            task = this.tasks.get((num-1));
            next_task="/task"+ (num+1);
        }
        if (num >= this.tasks.size()){
            next_task="/task"+ this.tasks.size();
        }
        this.tf.setTask(task);

        model.addAttribute("textData", this.tf);
        model.addAttribute("next_task", next_task);
        //model.addAttribute("graph_vertex_pos", this.gr.get_vertex_pos());
        //model.addAttribute("get_vertex_properties", this.gr.get_vertex_properties());
        //model.addAttribute("get_eadge_properites", this.gr.get_eadge_properites());
        //model.addAttribute("get_eadge_color", this.gr.get_eadge_color());
        //model.addAttribute("get_vertex_color", this.gr.get_vertex_color());
        //model.addAttribute("get_eadge_orientation", this.gr.get_eadge_orientation());
        return "index";
    }
    @GetMapping("/lesson1")
    public String showpage_lesson1(Model model){
        //System.out.println("Get");
        return "lesson1";
    }

    @GetMapping("/lesson2")
    public String showpage_lesson2(Model model){
        //System.out.println("Get");
        return "lesson2";
    }

    @GetMapping("/lesson3")
    public String showpage_lesson3(Model model){
        //System.out.println("Get");
        return "lesson3";
    }

    @GetMapping("/")
    public String show_main_page(Model model){
        //System.out.println("Get");
        return "lesson1";
    }

    @PostMapping(value = {"/task{num}"})
    public String prosessForm(@ModelAttribute("textData") TextForms tf, Model model, @PathVariable int num, @ModelAttribute("needs_reboot") boolean fl){
        //System.out.println(tf.getInput());
        String task = "";
        String next_task = "";
        if (num <= this.tasks.size()){
            task = this.tasks.get((num-1));
            next_task="/task"+ (num+1);
        }
        if (num >= this.tasks.size()){
            next_task="/task"+ this.tasks.size();
        }
        this.tf.setTask(task);
        Graph gr = new Graph();
        this.tf.setInput(tf.getInput());
        if (fl){
            service.reboot();
        } else{
            gr = service.sol_query(tf.getInput());
        }
        //this.tf.setOutput(gr.info);
        //System.out.println(this.tf.getOutput());
        //System.out.println(pair.getSecond().get_vertex_pos());
        model.addAttribute("textData", this.tf);
        model.addAttribute("gr_info", gr.info);
        model.addAttribute("next_task", next_task);
        model.addAttribute("graph_vertex_pos", gr.get_vertex_pos());
        model.addAttribute("get_vertex_properties", gr.get_vertex_properties());
        model.addAttribute("get_eadge_properites", gr.get_eadge_properites());
        model.addAttribute("get_eadge_color", gr.get_eadge_color());
        model.addAttribute("get_vertex_color", gr.get_vertex_color());
        model.addAttribute("get_eadge_orientation", gr.get_eadge_orientation());
        return "index";
    }





}
