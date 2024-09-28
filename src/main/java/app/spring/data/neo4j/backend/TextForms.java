package app.spring.data.neo4j.backend;

public class TextForms {
    private String input = "";
    private String output = "";
    private String task= "";
    public String getInput(){
        return this.input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
