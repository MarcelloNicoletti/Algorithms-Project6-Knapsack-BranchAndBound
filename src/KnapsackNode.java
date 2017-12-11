import java.util.List;

public class KnapsackNode {
    public List<Good> goodsContained;
    public int boundProfit, realProfit, weight;

    public KnapsackNode (List<Good> goodsContained, int boundProfit,
        int realProfit,
        int weight) {
        this.goodsContained = goodsContained;
        this.boundProfit = boundProfit;
        this.realProfit = realProfit;
        this.weight = weight;
    }
}
