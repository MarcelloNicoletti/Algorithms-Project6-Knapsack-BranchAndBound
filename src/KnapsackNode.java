import java.util.List;

public class KnapsackNode {
    public List<Good> goodsContained;
    public int nodeNumber, boundProfit, level;

    public KnapsackNode (int nodeNumber, int boundProfit,
        int level, List<Good> goodsContained) {
        this.nodeNumber = nodeNumber;
        this.goodsContained = goodsContained;
        this.boundProfit = boundProfit;
        this.level = level;
    }

    public int getBoundProfit () {
        return this.boundProfit;
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
        sb.append("Node ").append(nodeNumber).append(": ");
        sb.append("items: [").append(goodsContained.toString()).append("], ");
        sb.append("level: ").append(level).append(", ");
        sb.append("profit: ").append(this.getRealProfit()).append(", ");
        sb.append("weight: ").append(this.getRealWeight()).append(", ");
        sb.append("bound: ").append(this.getBoundProfit());
        return sb.toString();
    }
}
