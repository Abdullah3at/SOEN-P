package carDealership;

import persistance.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dealership {

    public Dealership(String name, String location, int maxInventory) throws SQLException {
    }
    // This method is used to sell a vehicle by removing it from inventory and
    // adding a sales record.
    // returns true if the vehicle is successfully sold, false otherwise.
    public boolean sellVehicle(Vehicle vehicle, String customerName, String date) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            
            // Insert the sale record into the sales table
            String insertSaleQuery = "INSERT INTO sales (vehicleId, customerName, date, price) VALUES (?, ?, ?, ?)";
            PreparedStatement insertSaleStmt = dbManager.getConnection().prepareStatement(insertSaleQuery);
            insertSaleStmt.setInt(1, vehicle.getId());
            insertSaleStmt.setString(2, customerName);
            insertSaleStmt.setString(3, date);
            insertSaleStmt.setDouble(4, vehicle.getPrice());
            insertSaleStmt.executeUpdate();
            
            // Update the vehicle record to mark it as sold instead of deleting it.
            String updateVehicleQuery = "UPDATE vehicles SET sold = 1 WHERE id = ?";
            PreparedStatement updateVehicleStmt = dbManager.getConnection().prepareStatement(updateVehicleQuery);
            updateVehicleStmt.setInt(1, vehicle.getId());
            updateVehicleStmt.executeUpdate();
            
            dbManager.close();
            
            // Remove the vehicle from local inventory so it no longer appears in the GUI.
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Vehicle getVehicleFromId(int vehicleId) {
        Vehicle vehicle = null;
        try {
            DatabaseManager dbManager = new DatabaseManager();
            // Only get vehicles that are not sold (sold = 0)
            String query = "SELECT * FROM vehicles WHERE id = ? AND sold = 0";
            PreparedStatement stmt = dbManager.getConnection().prepareStatement(query);
            stmt.setInt(1, vehicleId);
            ResultSet resultSet = stmt.executeQuery();
    
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String make = resultSet.getString("make");
                String model = resultSet.getString("model");
                int year = resultSet.getInt("year");
                double price = resultSet.getDouble("price");
                String type = resultSet.getString("type");
                String color = resultSet.getString("color");
    
                vehicle = new Vehicle(id, make, model, color, year, price, type);
            }
    
            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }
    
}