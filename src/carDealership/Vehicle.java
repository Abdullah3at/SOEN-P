package carDealership;

import java.io.Serializable;

public class Vehicle implements Serializable {
    protected int id;
    protected String make;
    protected String model;
    protected String color;
    protected int year;
    protected double price;
    protected String type;

    public Vehicle(int id, String make, String model, String color, int year, double price, String type) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.color = color;
        this.year = year;
        this.price = price;
        this.type = type;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nMake: " + make + "\nModel: " + model + "\nColor: " + color + "\nYear: " + year + "\nPrice: " + price + " SAR" + "\nType: " + type;
    }
}