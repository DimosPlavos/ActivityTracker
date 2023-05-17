package com.example.app1.Domain;
//na dw an mporv na exw klasi store sto Activity kai na thn kanw extend paroti einai se allo package

public class PopularDomain extends Store{
    private String pic;
    private String name;
    private String type; //bar/restaurant..
    private double rating;
    private int number_of_ratings;
    private double availability;
    private boolean working;
    //...

    public PopularDomain(String pic,String name, String type,double rating,
                 int number_of_ratings, double availability, boolean working)
    {
        super(pic, name,type,rating,number_of_ratings,availability,working);
    }

}
