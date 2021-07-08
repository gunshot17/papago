package com.il.papago.model;

public class Post {


    private String origintext;
    private String translatedtext;

    public Post(String origintext, String translatedtext) {

        this.origintext = origintext;
        this.translatedtext = translatedtext;

    }
    public Post(){

    }



    public String getOrigintext() {
        return origintext;
    }

    public void setOrigintext(String origintext) {
        this.origintext = origintext;
    }

    public String getTranslatedtext() {
        return translatedtext;
    }

    public void setTranslatedtext(String translatedtext) {
        this.translatedtext = translatedtext;
    }


}
