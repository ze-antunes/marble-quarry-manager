package pt.project.marblequarry.manager.model;

public class Stock {
    private int id;
    private int orderId;
    private int blockId;

    // Constructor
    public Stock(int id, int orderId, int blockId) {
        this.id = id;
        this.orderId = orderId;
        this.blockId = blockId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getBlockId() { return blockId; }
    
    public void setId(int id) { this.id = id; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public void setBlockId(int blockId) { this.blockId = blockId; }
    
    // toString method for easy representation
    @Override
    public String toString() {
        return "Stock {" +
                "id=" + id +
                ", orderId=" + orderId +
                ", blockId=" + blockId +
                '}';
    }
}
