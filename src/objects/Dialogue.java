package objects;

// class for handling Dialogue

// example: Dialogue(2, "TEXT", [["CHOICE 1", TEXT IT LEADS TO], ["CHOICE 2", TEXT IT LEADS TO]])
// dialogue of order 1 is always played first


import java.util.HashMap;

public class Dialogue {
    private Integer order;
    private String text;
    private HashMap<String, Integer> choices;

    public Dialogue(Integer order, String text, HashMap<String, Integer> choices) {
        this.order = order;
        this.text = text;
        this.choices = choices;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public HashMap<String, Integer> getChoices() {
        return choices;
    }

    public void setChoices(HashMap<String, Integer> choices) {
        this.choices = choices;
    }
}
