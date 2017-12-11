import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Pro6 {
    public static void main (String[] args) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(args[0]);
        Scanner file = new Scanner(fis);

        int maxWeight = file.nextInt();
        List<Good> goods = getGoods(file);

        List<Good> inKanpsack = knapsack(goods, maxWeight);
    }

    private static List<Good> knapsack (List<Good> goods, int maxWeight) {
        PriorityQueue<KnapsackNode> nodes = new PriorityQueue<>();

        return null;
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
