import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Pro6 {
    public static void main (String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(args[0]);
        Scanner file = new Scanner(fis);

        int maxWeight = file.nextInt();
        List<Good> goods = getGoods(file);

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
            getBound(goods, maxWeight,0, 0), 0, new ArrayList<>());
        KnapsackNode bestNode = firstNode;
        nodes.add(firstNode);

        do {
            KnapsackNode current = nodes.poll();
            System.out.printf("Exploring %s%n", current.toString());

            if (current.getBoundProfit() <= bestNode.getRealProfit()) {
                System.out.printf("Pruning node because bound %f does not " +
                        "beat known best profit of %d%n%n",
                    current.getBoundProfit(), bestNode.getRealProfit());
                break;
            }
            if (current.getLevel() >= goods.size()) {
                System.out.printf("Node %s can't have children.%n",
                    current.toString());
                break;
            }

            List<Good> goodsRemaining = goods.stream().skip(current.getLevel
                ()).collect(Collectors.toList());

            KnapsackNode leftNode = getLeftNode(maxWeight, ++nodeCount,
                current, goodsRemaining);
            bestNode = overloadedMethod(leftNode, maxWeight, nodes, bestNode);

            KnapsackNode rightNode =
                getRightNode(goods.get(current.getLevel()), maxWeight,
                    ++nodeCount, current, goodsRemaining);
            bestNode = overloadedMethod(rightNode, maxWeight, nodes, bestNode);

            System.out.printf("Note best profit so far is %d%n%n", bestNode
                .getRealProfit());
        } while (nodes.size() > 0);

        System.out.printf("Found best node: %s%n%n", bestNode.toString());
        return bestNode.getGoodsContained();
    }

    private static KnapsackNode getRightNode (Good good, int maxWeight,
        int nodeCount, KnapsackNode current, List<Good> goodsRemaining) {
        double rightBound = getBound(new ArrayList<>(goodsRemaining),
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
        double leftBound = getBound(goodsRemaining.stream().skip(1).collect(
            Collectors.toList()), maxWeight, current.getRealWeight(),
            current.getRealProfit());
        List<Good> leftGoods = new ArrayList<>(current.getGoodsContained());
        KnapsackNode leftNode = new KnapsackNode(nodeCount, leftBound,
            current.getLevel() + 1, leftGoods);
        System.out.printf("  Left child is %s%n", leftNode);
        return leftNode;
    }

    private static KnapsackNode overloadedMethod (KnapsackNode node, int maxWeight,
        PriorityQueue<KnapsackNode> nodes, KnapsackNode bestNode) {
        if (node.getRealWeight() < maxWeight) {
            if (node.getRealProfit() > bestNode.getRealProfit()) {
                bestNode = node;
            }
            nodes.add(node);
            System.out.println("    Exploring further.");
        } else if (node.getRealWeight() == maxWeight) {
            if (node.getRealProfit() > bestNode.getRealProfit()) {
                bestNode = node;
            }
            System.out.println("    Weight equals max exactly. No Further " +
                "exploration.");
        } else {
            System.out.println("    Node over max weight. Pruning this node");
        }
        return bestNode;
    }

    private static double getBound (List<Good> goods, int maxWeight,
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

    private static List<Good> getGoods (Scanner file) {
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
