package pt.project.marblequarry.manager.service;

import pt.project.marblequarry.manager.model.Block;
import pt.project.marblequarry.manager.repos.BlockRepository;
import java.util.List;

public class CuttingService {

    private BlockRepository blockRepository;

    public CuttingService() {
        this.blockRepository = new BlockRepository();
    }

    public double calculatePlatesPerBlock(Block b, double plateThicknessM) {
        if (b == null || plateThicknessM <= 0) {
            throw new IllegalArgumentException("Invalid block or plate thickness.");
        }

        // You can't cut plates thicker than the block's remaining thickness
        return (double) Math.floor(b.getHeightM() / plateThicknessM);
    }

    public double calculateUnitsPerPlate(Block b, double unitLengthM, double unitThicknessM) {
        double plateArea = b.getLengthM() * b.getThicknessM();
        double unitArea = unitLengthM * unitThicknessM;
        return plateArea / unitArea;
    }

    public double calculateTotalUnits(double cutThickness, double unitLengthM, double unitThicknessM) {
        List<Block> blocks = blockRepository.findAllAvailable();
        double totalUnits = 0;

        for (Block b : blocks) {
            double plates = calculatePlatesPerBlock(b, cutThickness);
            double unitsPerPlate = calculateUnitsPerPlate(b, unitLengthM, unitThicknessM);
            totalUnits += plates * unitsPerPlate;
        }

        return totalUnits;
    }

    public int calculateTotalUnitsForOrder(List<Block> blocks, double cutThickness, double unitLength,
            double unitThickness, int requestedUnits) {
        double total = 0;
        int usedBlocks = 0;

        for (Block b : blocks) {
            double plates = calculatePlatesPerBlock(b, cutThickness);
            double unitsPerPlate = calculateUnitsPerPlate(b, unitLength, unitThickness);
            total += plates * unitsPerPlate;
            usedBlocks++;
            if (total >= requestedUnits)
                break;
        }

        return usedBlocks;
    }

    public double calculateTotalUnitsForOrderFromDB(double cutThickness, double unitLengthM, double unitThicknessM,
            int orderId) {
        List<Block> blocks = blockRepository.findAllAvailableForOrder(orderId);
        double totalUnits = 0;

        for (Block b : blocks) {
            double plates = calculatePlatesPerBlock(b, cutThickness);
            double unitsPerPlate = calculateUnitsPerPlate(b, unitLengthM, unitThicknessM);
            totalUnits += plates * unitsPerPlate;
        }

        return totalUnits;
    }
}
