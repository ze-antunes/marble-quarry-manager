package pt.project.marblequarry.manager.model;

public class Block {
    private int id;
    private String marbleType;
    private double lengthM;
    private double heightM;
    private double thicknessM;
    private String status;

    // Constructor 
    public Block(int id, String marbleType, double lengthM, double heightM, double thicknessM, String status) {
        this.id = id;
        this.marbleType = marbleType;
        this.lengthM = lengthM;
        this.heightM = heightM;
        this.thicknessM = thicknessM;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public String getMarbleType() { return marbleType; }
    public double getLengthM() { return lengthM; }
    public double getHeightM() { return heightM; }
    public double getThicknessM() { return thicknessM; }
    public String getStatus() { return status; }

    public void setId(Integer id) { this.id = id; }
    public void setMarbleType(String marbleType) { this.marbleType = marbleType; }
    public void setLengthM(double lengthM) { this.lengthM = lengthM; }
    public void setHeightM(double heightM) { this.heightM = heightM; }
    public void setThicknessM(double thicknessM) { this.thicknessM = thicknessM; }
    public void setStatus(String status) { this.status = status; }

    // toString method for easy representation
    @Override
    public String toString() {
        return "Block {" +
                "id=" + id +
                ", marbleType='" + marbleType + '\'' +
                ", lengthM=" + lengthM +
                ", heightM=" + heightM +
                ", thicknessM=" + thicknessM +
                ", status='" + status + '\'' +
                '}';
    }
}
