package pt.project.marblequarry.manager.model;

public class Order {
    private int id;
    private int unitCount;
    private double unitLengthM;
    private double unitHeightM;
    private double unitThicknessM;

    // Constructor
    public Order(int id, int unitCount, double unitLengthM, double unitHeightM, double unitThicknessM) {
        this.id = id;
        this.unitCount = unitCount;
        this.unitLengthM = unitLengthM;
        this.unitHeightM = unitHeightM;
        this.unitThicknessM = unitThicknessM;
    }

    // Getters and Setters
    public Integer getId() {return id;}   
    public Integer getUnitCount() {return unitCount;}   
    public double getUnitLengthM() {return unitLengthM;}   
    public double getUnitHeightM() {return unitHeightM;}   
    public double getUnitThicknessM() {return unitThicknessM;}   

    public void setId(Integer id) {this.id = id;}
    public void setUnitCount(Integer unitCount) {this.unitCount = unitCount;}
    public void setUnitLengthM(double unitLengthM) {this.unitLengthM = unitLengthM;}
    public void setUnitHeightM(double unitHeightM) {this.unitHeightM = unitHeightM;}
    public void setUnitThicknessM(double unitThicknessM) {this.unitThicknessM = unitThicknessM;}

    // toString method for easy representation
    @Override
    public String toString() {
        return "Order {" +
                "id=" + id +
                ", unitCount=" + unitCount +
                ", unitLengthM=" + unitLengthM +
                ", unitHeightM=" + unitHeightM +
                ", unitThicknessM=" + unitThicknessM +
                '}';
    }
}
