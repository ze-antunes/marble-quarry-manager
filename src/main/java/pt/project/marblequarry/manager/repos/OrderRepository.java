package pt.project.marblequarry.manager.repos;

import pt.project.marblequarry.manager.config.DBConnection;
import pt.project.marblequarry.manager.model.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    
    // This class will handle the CRUD operations for Order objects
    // It will interact with the database to store and retrieve Order data

    // method to save an order
    public int saveOrder(Order order) {
        String sql = "INSERT INTO orders(unit_count, unit_length_m, unit_height_m, unit_thickness_m) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getUnitCount());
            stmt.setDouble(2, order.getUnitLengthM());
            stmt.setDouble(3, order.getUnitHeightM());
            stmt.setDouble(4, order.getUnitThicknessM());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt("id");
                order.setId(generatedId); // guarda o ID no objeto
                System.out.println("Encomenda realizada com sucesso! ID: " + generatedId);
                return generatedId;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao realizar a encomenda: " + e.getMessage());
        }
        return -1; // se falhar
    }

    // method to find an order by ID
    public Order findOrderById(int id) {
        // Code to retrieve an order from the database by its ID
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("unit_count"),
                        rs.getDouble("unit_length_m"),
                        rs.getDouble("unit_height_m"),
                        rs.getDouble("unit_thickness_m"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Placeholder return statement
    }

    // method to find all orders
    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try(Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("unit_count"),
                        rs.getDouble("unit_length_m"),
                        rs.getDouble("unit_height_m"),
                        rs.getDouble("unit_thickness_m"));
                orders.add(order);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar encomendas: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }

    public void printOrdersAsTable(List<Order> orders) {
        // Cabeçalho
        System.out.format("%-5s%-12s%-15s%-15s%-15s%n",
                "ID", "Unidades", "Comp. Unidade", "Alt. Unidade", "Esp. Unidade");
        System.out.println("---------------------------------------------------------------------");
    
        // Linhas
        for (Order o : orders) {
            System.out.format("%-5d%-12d%-15.2f%-15.2f%-15.2f%n",
                    o.getId(),
                    o.getUnitCount(),
                    o.getUnitLengthM(),
                    o.getUnitHeightM(),
                    o.getUnitThicknessM());
        }
    }
}
