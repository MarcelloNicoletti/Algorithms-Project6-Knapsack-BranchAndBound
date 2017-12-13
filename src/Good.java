public class Good {
    private final String name;
    private final int profit;
    private final int weight;

    Good (String name, int profit, int weight) {
        this.name = name;
        this.profit = profit;
        this.weight = weight;
    }

    public String getName () {
        return name;
    }

    public int getProfit () {
        return profit;
    }

    public int getWeight () {
        return weight;
    }

    public double getScore () {
        return (double) profit / (double) weight;
    }

    public int compareTo (Good other) {
        int a = this.getProfit() * other.getWeight();
        int b = other.getProfit() * this.getWeight();
        return b - a;
    }

    @Override
    public String toString () {
        return String.format("%s:\t%d\t%d\t%.2f", this.name, this.profit,
            this.weight, this.getScore());
    }
}
