import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Pro6 {
    public static void main (String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(args[0]);
        Scanner file = new Scanner(fis);

        int maxWeight = file.nextInt();
        List<Good> goods = getGoods(file);

        List<Good> inKanpsack = knapsack(goods, maxWeight);
        int bestProfit = inKanpsack.stream().mapToInt(Good::getProfit).sum();

        System.out.printf("The best profit is %d from [%s].%n", bestProfit,
            goods.stream().map(Good::getName).collect(
                Collectors.joining(", ")));
    }

    private static List<Good> knapsack (List<Good> goods, int maxWeight) {
        PriorityQueue<KnapsackNode> nodes = new PriorityQueue<>();

        KnapsackNode firstNode = new KnapsackNode(1, getBound(goods,
            maxWeight, 0, 0), -1, new ArrayList<>());
        nodes.add(firstNode);

        do {
            KnapsackNode current = nodes.poll();
            System.out.printf("Exploring node %s", current.toString());
            if (current.level >= goods.size() - 1) {
                return current.goodsContained;
            }
        } while (nodes.size() > 1);

        return null;
    }

    private static int getBound (List<Good> goods, int maxWeight,
        int startingWeight, int startingProfit) {
        int weightTotal = startingWeight;
        int boundTotal = startingProfit;
        for (Good good : goods) {
            int weightRemaining = maxWeight - weightTotal;
            if (weightRemaining > 0) {
                if (weightRemaining >= good.getWeight()) {
                    weightTotal += good.getWeight();
                    boundTotal += good.getProfit();
                } else {
                    weightTotal += good.getWeight();
                    boundTotal += weightRemaining * good.getScore();
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
        return goods;
    }
}
