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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<Node ").append(nodeNumber).append(": ");
        sb.append("items: [").append(goodsContained.stream().map
            (Good::getName).collect(Collectors.joining(", ")))
            .append("], ");
        sb.append("level: ").append(level).append(", ");
        sb.append("profit: ").append(getRealProfit()).append(", ");
        sb.append("weight: ").append(getRealWeight()).append(", ");
        sb.append("bound: ").append(getBoundProfit()).append(">");
        return sb.toString();
    }
}
