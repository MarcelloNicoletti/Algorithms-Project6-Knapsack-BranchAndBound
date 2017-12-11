import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Pro6 {
    public static void main (String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(args[0]);
        Scanner file = new Scanner(fis);

        int maxWeight = file.nextInt();
        List<Good> goods = getGoodsFromFile(file);

        System.out.println();
        List<Good> inKnapsack = knapsack(goods, maxWeight);
        int bestProfit = inKnapsack.stream().mapToInt(Good::getProfit).sum();

        System.out.printf("The best profit is %d from [%s].%n", bestProfit,
            inKnapsack.stream().map(Good::getName).collect(
                Collectors.joining(", ")));
    }

    private static List<Good> knapsack (List<Good> goods, int maxWeight) {
        PriorityQueue<KnapsackNode> nodes =
            new PriorityQueue<>((KnapsackNode n1, KnapsackNode n2) ->
                (int)((n2.getBoundProfit() - n1.getBoundProfit()) /
                    Math.abs((n2.getBoundProfit() - n1.getBoundProfit()))));

        int nodeCount = 0;
        KnapsackNode firstNode = new KnapsackNode(++nodeCount,
            calculateBound(goods, maxWeight,0, 0), 0, new ArrayList<>());
        KnapsackNode bestNode = firstNode;
        nodes.add(firstNode);

        do {
            KnapsackNode current = nodes.poll();
            System.out.printf("Exploring %s%n", current.toString());

            if (current.getBoundProfit() <= bestNode.getRealProfit()) {
                System.out.printf("  Pruning node because bound %.1f does not" +
                        " " +
                        "beat known best profit of %d%n%n",
                    current.getBoundProfit(), bestNode.getRealProfit());
                continue;
            }

            List<Good> goodsRemaining = goods.stream().skip(current.getLevel
                ()).collect(Collectors.toList());

            KnapsackNode leftNode = getLeftNode(maxWeight, ++nodeCount,
                current, goodsRemaining);
            bestNode = addOrPruneNode(leftNode, maxWeight, nodes, bestNode,
                goods.size());

            KnapsackNode rightNode =
                getRightNode(goods.get(current.getLevel()), maxWeight,
                    ++nodeCount, current, goodsRemaining);
            bestNode = addOrPruneNode(rightNode, maxWeight, nodes, bestNode,
                goods.size());

            System.out.printf("Note best profit so far is %d%n", bestNode
                .getRealProfit());

            System.out.println("Remaining nodes:");
            for (KnapsackNode node : nodes) {
                System.out.printf("    %s%n", node.toString());
            }
            System.out.println();
        } while (nodes.size() > 0);

        System.out.printf("Found best node: %s%n%n", bestNode.toString());
        return bestNode.getGoodsContained();
    }

    private static KnapsackNode getRightNode (Good good, int maxWeight,
        int nodeCount, KnapsackNode current, List<Good> goodsRemaining) {
        double rightBound = calculateBound(new ArrayList<>(goodsRemaining),
            maxWeight, current.getRealWeight(), current.getRealProfit());
        List<Good> rightGoods = new ArrayList<>(
            current.getGoodsContained());
        rightGoods.add(good);
        KnapsackNode rightNode = new KnapsackNode(nodeCount, rightBound,
            current.getLevel() + 1, rightGoods);
        System.out.printf("  Right child is %s%n", rightNode);
        return rightNode;
    }

    private static KnapsackNode getLeftNode (int maxWeight, int nodeCount,
        KnapsackNode current, List<Good> goodsRemaining) {
        double leftBound = calculateBound(goodsRemaining.stream().skip(1).collect(
            Collectors.toList()), maxWeight, current.getRealWeight(),
            current.getRealProfit());
        List<Good> leftGoods = new ArrayList<>(current.getGoodsContained());
        KnapsackNode leftNode = new KnapsackNode(nodeCount, leftBound,
            current.getLevel() + 1, leftGoods);
        System.out.printf("  Left child is %s%n", leftNode);
        return leftNode;
    }

    private static KnapsackNode addOrPruneNode (KnapsackNode node, int maxWeight,
        PriorityQueue<KnapsackNode> nodes, KnapsackNode bestNode,
        int maxLevel) {
        if (node.getRealWeight() < maxWeight) {
            if (node.getRealProfit() > bestNode.getRealProfit()) {
                bestNode = node;
            }
            if (node.getLevel() < maxLevel) {
                nodes.add(node);
                System.out.println("    Exploring further.");
            } else {
                System.out.println("    No more levels. No further " +
                    "exploration");
            }
        } else if (node.getRealWeight() == maxWeight) {
            if (node.getRealProfit() > bestNode.getRealProfit()) {
                bestNode = node;
            }
            System.out.println("    Weight equals max exactly. No further " +
                "exploration.");
        } else {
            System.out.println("    Node over max weight. Pruning this node");
        }
        return bestNode;
    }

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
                    boundTotal += (double) weightRemaining * good.getScore();
                }
            } else {
                break;
            }
        }

        return boundTotal;
    }

    private static List<Good> getGoodsFromFile (Scanner file) {
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

        goods.sort(Good::compareTo);
        return goods;
    }
}
