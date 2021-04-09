package edu.sjsu.android.animallisting;

public class Animal {
    private String name;
    private String path;
    private String details;

    public Animal(String name, String path, String details){
        this.name = name;
        this.path = path;
        this.details = details;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPath(String path){
        this.path = path;
    }

    public void setDetails(String details){
        this.details = details;
    }

    public String getName(){
        return name;
    }

    public String getPath(){
        return path;
    }

    public String getDetails(){
        return details;
    }
}
