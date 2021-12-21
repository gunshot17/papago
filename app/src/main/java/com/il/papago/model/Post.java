package com.il.papago.model;

public class Post {


    private int id;
    private String origintext;
    private String Text;

    public Post(String origintext, String Text) {

        this.origintext = origintext;
        this.Text = Text;

    }
    public Post(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigintext() {
        return origintext;
    }

    public void setOrigintext(String origintext) {
        this.origintext = origintext;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }


}
