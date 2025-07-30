package pt.project.marblequarry.manager.repos;

import pt.project.marblequarry.manager.config.DBConnection;
import pt.project.marblequarry.manager.model.Block;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlockRepository {

    // This class will handle the CRUD operations for Block objects
    // It will interact with the database to store and retrieve Block data

    // method to save a block
    public void saveBlock(Block block) {
        // Code to save the block to the database
        String sql = "INSERT INTO blocks(marble_type, length_m, height_m, thickness_m, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, block.getMarbleType());
            stmt.setDouble(2, block.getLengthM());
            stmt.setDouble(3, block.getHeightM());
            stmt.setDouble(4, block.getThicknessM());
            stmt.setString(5, block.getStatus());

            stmt.executeUpdate();
            System.out.println("Bloco guardado com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao guardar o bloco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // method to find a block by ID
    public Block findBlockById(int id) {
        // Code to retrieve a block from the database by its ID
        String sql = "SELECT * FROM blocks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Block(
                        rs.getInt("id"),
                        rs.getString("marble_type"),
                        rs.getDouble("length_m"),
                        rs.getDouble("height_m"),
                        rs.getDouble("thickness_m"),
                        rs.getString("status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Placeholder return statement
    }

    // Additional methods for finding all blocks, updating and deleting blocks
    // Method to find all available blocks
    public List<Block> findAllAvailable() {
        List<Block> blocks = new ArrayList<>();
        String sql = "SELECT * FROM blocks WHERE status='available'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                blocks.add(new Block(
                        rs.getInt("id"),
                        rs.getString("marble_type"),
                        rs.getDouble("length_m"),
                        rs.getDouble("height_m"),
                        rs.getDouble("thickness_m"),
                        rs.getString("status")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blocks;
    }

    // Method to find all blocks
    public List<Block> findAllBlocks() {
        List<Block> blocks = new ArrayList<>();
        String sql = "SELECT * FROM blocks";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                blocks.add(new Block(
                        rs.getInt("id"),
                        rs.getString("marble_type"),
                        rs.getDouble("length_m"),
                        rs.getDouble("height_m"),
                        rs.getDouble("thickness_m"),
                        rs.getString("status")));
            }

        } catch (SQLException e) {
        System.err.println("Erro ao listar blocos: " + e.getMessage());
            e.printStackTrace();
        }
        return blocks;
    }

    // Method to update a block
    public void updateBlock(Block block) {
        String sql = "UPDATE blocks SET marble_type = ?, length_m = ?, height_m = ?, thickness_m = ?, status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, block.getMarbleType());
            stmt.setDouble(2, block.getLengthM());
            stmt.setDouble(3, block.getHeightM());
            stmt.setDouble(4, block.getThicknessM());
            stmt.setString(5, block.getStatus());
            stmt.setInt(6, block.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Bloco atualizado com sucesso!");
            } else {
                System.out.println("Nenhum bloco encontrado com ID " + block.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a block by ID
    public void deleteBlock(int id) {
        String sql = "DELETE FROM blocks WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Bloco removido com sucesso!");
            } else {
                System.out.println("Nenhum bloco encontrado com ID " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check if a block is assigned to any order
    public boolean isBlockAssignedToAnyOrder(int blockId) {
        String sql = "SELECT COUNT(*) AS total FROM order_blocks WHERE block_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, blockId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("total") > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao verificar associação do bloco: " + e.getMessage());
        }
        return false;
    }

    public void refreshBlockStatus(int blockId) {
        String newStatus = isBlockAssignedToAnyOrder(blockId) ? "reserved" : "available";
        updateStatus(blockId, newStatus);
    }

    public void updateStatus(int blockId, String status) {
        String sql = "UPDATE blocks SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, blockId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do bloco: " + e.getMessage());
        }
    }

    // Method to assign a block to an order
    public void assignBlockToOrder(int orderId, int blockId) {
        String sql = "INSERT INTO order_blocks(order_id, block_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, blockId);
            stmt.executeUpdate();

            // ✅ Atualiza status automaticamente
            refreshBlockStatus(blockId);

        } catch (SQLException e) {
            System.err.println("Erro ao associar bloco à encomenda: " + e.getMessage());
        }
    }

    // Method to find all blocks available for a specific order
    public List<Block> findAllAvailableForOrder(int orderId) {
        List<Block> blocks = new ArrayList<>();
        String sql = "SELECT b.* FROM blocks b " +
                "JOIN order_blocks ob ON b.id = ob.block_id " +
                "WHERE ob.order_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                blocks.add(new Block(
                        rs.getInt("id"),
                        rs.getString("marble_type"),
                        rs.getDouble("length_m"),
                        rs.getDouble("height_m"),
                        rs.getDouble("thickness_m"),
                        rs.getString("status")));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar blocos da encomenda: " + e.getMessage());
        }
        return blocks;
    }

    // Print blocks as a formatted table
    public void printBlocksAsTable(List<Block> blocks) {
        // Header
        System.out.format("%-5s%-15s%-10s%-10s%-12s%-12s%n",
                "ID", "Tipo", "Comp(m)", "Alt(m)", "Esp(m)", "Status");
        System.out.println("---------------------------------------------------------------------");

        // Rows
        for (Block b : blocks) {
            System.out.format("%-5d%-15s%-10.2f%-10.2f%-12.2f%-12s%n",
                    b.getId(), b.getMarbleType(), b.getLengthM(), b.getHeightM(),
                    b.getThicknessM(), b.getStatus());
        }
    }
}
