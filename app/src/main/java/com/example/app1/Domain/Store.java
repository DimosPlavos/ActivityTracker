package com.example.app1.Domain;

public class Store {
    private String pic;
    private String name;
    private String type; //bar/restaurant..
    private double rating;
    private int number_of_ratings;
    private double availability;
    private boolean working;
    //...

    public Store(String pic, String name, String type,double rating,
                 int number_of_ratings, double availability, boolean working)
    {
        this.pic = pic;
        this.name = name;
        this.type = type;
        this.rating = rating;
        this.number_of_ratings = number_of_ratings;
        this.availability = availability;
        this.working = working;
    }

    public double getAvailability() {
        return availability;
    }

    public double getRating() {
        return rating;
    }

    public int getNumber_of_ratings() {
        return number_of_ratings;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPic() {
        return pic;
    }

    public void setAvailability(double availability) {
        this.availability = availability;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber_of_ratings(int number_of_ratings) {
        this.number_of_ratings = number_of_ratings;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}

