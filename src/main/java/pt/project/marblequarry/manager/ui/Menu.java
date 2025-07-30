package pt.project.marblequarry.manager.ui;

import java.util.List;
import java.util.Scanner;
import pt.project.marblequarry.manager.model.Block;
import pt.project.marblequarry.manager.model.Order;
import pt.project.marblequarry.manager.repos.BlockRepository;
import pt.project.marblequarry.manager.repos.OrderRepository;
import pt.project.marblequarry.manager.service.CuttingService;

public class Menu {
    public void show() {
        Scanner scanner = new Scanner(System.in);
        BlockRepository blockRepository = new BlockRepository();
        OrderRepository orderRepository = new OrderRepository();
        boolean isRunning = true;
        int choice;

        while (isRunning) {
            System.out.println("===== MENU =====");
            System.out.println("1. Registar novo bloco");
            System.out.println("2. Listar blocos disponíveis");
            System.out.println("3. Registar nova encomenda");
            System.out.println("4. Listar encomendas");
            System.out.println("5. Simular cortes teóricos");
            System.out.println("6. Listar todos os blocos");
            System.out.println("0. Sair");
            System.out.println("================");

            System.out.print("Escolha uma opção: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> registerNewBlock(blockRepository, scanner);
                case 2 -> blockRepository.printBlocksAsTable(blockRepository.findAllAvailable());
                case 3 -> registerNewOrder(orderRepository, scanner);
                case 4 -> orderRepository.printOrdersAsTable(orderRepository.findAllOrders());
                case 5 -> simulatedCuts(blockRepository, orderRepository, scanner);
                case 6 -> blockRepository.printBlocksAsTable(blockRepository.findAllBlocks());
                case 0 -> {
                    System.out.println("Exiting the program. Thank you!");
                    isRunning = false; // Set isRunning to false to exit the loop
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }

        // Close the scanner to avoid resource leaks
        scanner.close();
    }

    static void registerNewBlock(BlockRepository repo, Scanner scanner) {
        System.out.println("Time to register a new block!");
        // Logic to register a new block
        // Example: create a new Block object and save it using the repository
        System.out.print("Enter marble type: ");
        String marbleType = scanner.nextLine();

        System.out.print("Enter length (m): ");
        double lengthM = scanner.nextDouble();

        System.out.print("Enter height (m): ");
        double heightM = scanner.nextDouble();

        System.out.print("Enter thickness (m): ");
        double thicknessM = scanner.nextDouble();

        scanner.nextLine(); // Consume the newline character
        System.out.print("Enter status. Is it available (true or false): ");
        String status;
        boolean isAvailable = scanner.nextBoolean(); // Assuming blocks are available by default
        status = isAvailable ? "available" : "reserved";

        Block b = new Block(1, marbleType, lengthM, heightM, thicknessM, status);
        repo.saveBlock(b);
    }

    static void registerNewOrder(OrderRepository orderRepo, Scanner scanner) {
        BlockRepository blockRepo = new BlockRepository();
        CuttingService cuttingService = new CuttingService();

        // ➤ Pedir dados da encomenda
        System.out.println("Registering a new order...");
        System.out.print("Enter unit count: ");
        int unitCount = scanner.nextInt();
        System.out.print("Enter unit length (m): ");
        double unitLength = scanner.nextDouble();
        System.out.print("Enter unit height (m): ");
        double unitHeight = scanner.nextDouble();
        System.out.print("Enter unit thickness (m): ");
        double unitThickness = scanner.nextDouble();
        System.out.print("Enter cut thickness (m): ");
        double cutThickness = scanner.nextDouble();
        scanner.nextLine();

        // ➤ Criar a encomenda na BD
        Order order = new Order(1, unitCount, unitLength, unitHeight, unitThickness);
        orderRepo.saveOrder(order);

        // ➤ Calcular blocos necessários
        List<Block> availableBlocks = blockRepo.findAllAvailable();
        double neededBlocks = cuttingService.calculateTotalUnitsForOrder(availableBlocks, cutThickness, unitLength, unitHeight,
                order.getId());

        System.out.println("Estimativa: serão necessários " + neededBlocks + " blocos para satisfazer a encomenda.");

        // ➤ Reservar os blocos necessários (atualizar status na BD)
        int reservados = 0;
        for (Block b : availableBlocks) {
            blockRepo.updateBlock(b);
            blockRepo.assignBlockToOrder(order.getId(), b.getId()); // associa bloco à encomenda
            reservados++;
            if (reservados == neededBlocks)
                break;
        }

        System.out.println(reservados + " blocos foram reservados com sucesso.");
    }

    private Object simulatedCuts(BlockRepository blockRepository, OrderRepository orderRepository, Scanner scanner) {
        List<Block> blocks = blockRepository.findAllAvailable();

        System.out.println("=== Simular cortes ===");
        System.out.print("Espessura de corte (m): ");
        double cutThickness = scanner.nextDouble();

        System.out.print("Comprimento da unidade (m): ");
        double unitLength = scanner.nextDouble();

        System.out.print("Altura da unidade (m): ");
        double unitWidth = scanner.nextDouble();

        CuttingService cuttingService = new CuttingService();

        System.out.format("%-5s%-15s%-15s%-15s%n", "ID", "Placas", "Unid/Placa", "Total Unid");
        System.out.println("--------------------------------------------------------------");

        double totalGlobal = 0;
        for (Block b : blocks) {
            double plates = cuttingService.calculatePlatesPerBlock(b, cutThickness);
            double unitsPerPlate = cuttingService.calculateUnitsPerPlate(b, unitLength, unitWidth);
            double total = plates * unitsPerPlate;
            totalGlobal += total;

            System.out.format("%-5d%-15.2f%-15.2f%-15.2f%n", b.getId(), plates, unitsPerPlate, total);
        }
        System.out.println("==============================================================");
        System.out.printf("TOTAL UNIDADES POSSÍVEIS: %.2f\n", totalGlobal);
        System.out.println("=                                                            =");
        return totalGlobal;
    }
}
