import java.util.List;
import java.util.stream.Collectors;

public class KnapsackNode {
    private List<Good> goodsContained;
    private int nodeNumber, level;
    private double boundProfit;

    public KnapsackNode (int nodeNumber, double boundProfit,
        int level, List<Good> goodsContained) {
        this.nodeNumber = nodeNumber;
        this.goodsContained = goodsContained;
        this.boundProfit = boundProfit;
        this.level = level;
    }

    public List<Good> getGoodsContained () {
        return goodsContained;
    }

    public int getNodeNumber () {
        return nodeNumber;
    }

    public double getBoundProfit () {
        return boundProfit;
    }

    public int getLevel () {
        return level;
    }

    public int getRealProfit () {
        return goodsContained.stream().mapToInt(Good::getProfit).sum();
    }

    public int getRealWeight () {
        return goodsContained.stream().mapToInt(Good::getWeight).sum();
    }

    public int compareTo (KnapsackNode other) {
        double difference = other.getBoundProfit() - this.getBoundProfit();

        if (difference == 0.0) {
            return 0;
        }

        // The compare to spec just says negative, zero, and positive as the
        // possibilities. This means I don't have to return the actual
        // difference. A number divided by its own absolute value will be -1
        // or +1 and +/-1 is definitely in the safe range to downcast to int.
        return (int)(difference / Math.abs(difference));
    }

    @Override
    public String toString () {
        return "<Node " + nodeNumber + ": " +
            "items: [" + goodsContained.stream().map
            (Good::getName).collect(Collectors.joining(", ")) +
            "], " +
            "level: " + level + ", " +
            "profit: " + getRealProfit() + ", " +
            "weight: " + getRealWeight() + ", " +
            "bound: " + getBoundProfit() + ">";
    }
}
