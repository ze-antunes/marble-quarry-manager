package pt.project.marblequarry.manager.service;

import org.junit.jupiter.api.Test;
import pt.project.marblequarry.manager.model.Block;

import static org.junit.jupiter.api.Assertions.*;

public class CuttingServiceTest {

    CuttingService service = new CuttingService();

    @Test
    void testCalculatePlatesPerBlock() {
        Block b = new Block(1, "Calcário", 2.0, 2.0, 1.0, "available");
        double result = service.calculatePlatesPerBlock(b, 0.5); // altura 2 / corte 0.5 = 4
        assertEquals(4.0, result, 0.01);
    }

    @Test
    void testCalculateUnitsPerPlate() {
        Block b = new Block(1, "Granito", 2.0, 2.0, 1.0, "available");
        double result = service.calculateUnitsPerPlate(b, 1.0, 1.0); // area placa 2 / area unidade 1 = 2
        assertEquals(2.0, result, 0.01);
    }

    @Test
    void testCalculateTotalUnitsForOrder() {
        Block b1 = new Block(1, "Mármore", 2.0, 2.0, 1.0, "available");
        Block b2 = new Block(2, "Granito", 2.0, 2.0, 1.0, "available");
        var blocks = java.util.List.of(b1, b2);

        int neededBlocks = service.calculateTotalUnitsForOrder(blocks, 0.5, 1.0, 1.0, 8);
        assertEquals(1, neededBlocks); // Só 1 bloco já cobre 8 unidades
    }
}
