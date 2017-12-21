import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Pro6 {
    public static void main (String[] args) {
        Scanner input = getInputScanner(args[0]);

        int maxWeight = input.nextInt();
        System.out.printf("Capacity of the knapsack: %d%n", maxWeight);
        List<Good> goods = getGoodsFromFile(input);

        System.out.println();

        List<Good> packedGoods = packKnapsack(goods, maxWeight);
        int bestProfit = packedGoods.stream().mapToInt(Good::getProfit).sum();

        String nodeNames = packedGoods.stream().map(Good::getName).collect(
            Collectors.joining(", "));
        System.out.printf("The best profit is %d from [%s].%n", bestProfit,
            nodeNames);
    }

    private static List<Good> packKnapsack (List<Good> goods, int maxWeight) {
        PriorityQueue<KnapsackNode> unvisitedNodes =
            new PriorityQueue<>(KnapsackNode::compareTo);

        int nodeCount = 0;
        double rootBound = calculateBound(goods, maxWeight, 0, 0);
        KnapsackNode rootNode = new KnapsackNode(++nodeCount, rootBound, 0,
            new ArrayList<>());
        KnapsackNode bestNode = rootNode;
        unvisitedNodes.add(rootNode);

        do {
            // Gets the unvisited node with the best bound and removes it
            // from the PriorityQueue
            KnapsackNode current = unvisitedNodes.poll();
            System.out.printf("Exploring %s%n", current.toString());

            if (current.getBoundProfit() <= bestNode.getRealProfit()) {
                // If the node with the best bound has a worse bound than the
                // best known profit then it or its children can not win.
                // Best first also means also skipping bounds exactly equal
                // to best known profit
                System.out.printf("  Pruning node because bound %.1f does not" +
                        " beat known best profit of %d%n",
                    current.getBoundProfit(), bestNode.getRealProfit());

                printRemainingNodes(unvisitedNodes);
                continue;
            }

            List<Good> goodsRemaining = goods.stream().skip(current.getLevel
                ()).collect(Collectors.toList());

            KnapsackNode leftChild = createLeftChild(maxWeight, ++nodeCount,
                current, goodsRemaining);
            bestNode = addOrPruneNode(leftChild, unvisitedNodes, maxWeight,
                goods.size(),
                bestNode);

            KnapsackNode rightChild =
                createRightChild(goods.get(current.getLevel()), maxWeight,
                    ++nodeCount, current, goodsRemaining);
            bestNode = addOrPruneNode(rightChild, unvisitedNodes, maxWeight,
                goods.size(),
                bestNode);

            System.out.printf("Note best profit so far is %d%n", bestNode
                .getRealProfit());

            printRemainingNodes(unvisitedNodes);
        } while (unvisitedNodes.size() > 0);

        System.out.printf("Found best node: %s%n%n", bestNode.toString());
        return bestNode.getGoodsContained();
    }

    private static KnapsackNode addOrPruneNode (KnapsackNode node,
        PriorityQueue<KnapsackNode> nodes, int maxWeight, int maxLevel,
        KnapsackNode currentBestNode) {
        // Check if a node should even be explored before adding.
        if (node.getRealWeight() < maxWeight) {
            if (node.getRealProfit() > currentBestNode.getRealProfit()) {
                currentBestNode = node;
            }
            if (node.getLevel() < maxLevel) {
                nodes.add(node);
                System.out.println("    Exploring further.");
            } else {
                System.out.println("    No more levels. No further " +
                    "exploration");
            }
        } else if (node.getRealWeight() == maxWeight) {
            if (node.getRealProfit() > currentBestNode.getRealProfit()) {
                currentBestNode = node;
            }
            System.out.println("    Weight equals max exactly. No further " +
                "exploration.");
        } else {
            System.out.println("    Node over max weight. Pruning this node");
        }

        return currentBestNode;
    }

    private static KnapsackNode createRightChild (Good newGood, int maxWeight,
        int nodeCount, KnapsackNode parent, List<Good> goodsRemaining) {
        ArrayList<Good> goodsCopy = new ArrayList<>(goodsRemaining);
        double rightBound = calculateBound(goodsCopy, maxWeight,
            parent.getRealWeight(), parent.getRealProfit());

        List<Good> rightGoods = new ArrayList<>(parent.getGoodsContained());
        rightGoods.add(newGood);
        KnapsackNode rightNode = new KnapsackNode(nodeCount, rightBound,
            parent.getLevel() + 1, rightGoods);

        System.out.printf("  Right child is %s%n", rightNode);
        return rightNode;
    }

    private static KnapsackNode createLeftChild (int maxWeight, int nodeCount,
        KnapsackNode parent, List<Good> goodsRemaining) {
        List<Good> firstSkipped = goodsRemaining.stream().skip(1)
            .collect(Collectors.toList());
        double leftBound = calculateBound(firstSkipped, maxWeight,
            parent.getRealWeight(), parent.getRealProfit());

        List<Good> leftGoods = new ArrayList<>(parent.getGoodsContained());
        KnapsackNode leftNode = new KnapsackNode(nodeCount, leftBound,
            parent.getLevel() + 1, leftGoods);

        System.out.printf("  Left child is %s%n", leftNode);
        return leftNode;
    }

    // Assumes goods is sorted by p/w
    private static double calculateBound (List<Good> goods, int maxWeight,
        int startingWeight, int startingProfit) {
        int weightTotal = startingWeight;
        double boundTotal = startingProfit;

        for (Good good : goods) {
            int weightRemaining = maxWeight - weightTotal;
            if (weightRemaining > 0) {
                if (weightRemaining >= good.getWeight()) {
                    weightTotal += good.getWeight();
                    boundTotal += good.getProfit();
                } else {
                    weightTotal += good.getWeight();
                    boundTotal += (double)weightRemaining * good.getScore();
                }
            } else {
                break;
            }
        }

        return boundTotal;
    }

    private static void printRemainingNodes (
        PriorityQueue<KnapsackNode> nodes) {
        if (nodes.size() > 0) {
            System.out.println("Remaining nodes:");
            for (KnapsackNode node : nodes) {
                System.out.printf("    %s%n", node.toString());
            }
        }
        System.out.println();
    }

    private static List<Good> getGoodsFromFile (Scanner file) {
        System.out.println("Items are: ");
        List<Good> goods;
        int numItems = file.nextInt();

        goods = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            int profit = file.nextInt();
            int weight = file.nextInt();
            Good good = new Good(Integer.toString(i + 1), profit, weight);
            System.out.println(good.toString());
            goods.add(good);
        }

        // sorts goods by p/w
        goods.sort(Good::compareTo);
        return goods;
    }

    private static Scanner getInputScanner (String filename) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.err.printf("Input file <%s> not found.", filename);
            System.exit(1);
        }
        return new Scanner(fis);
    }
}
