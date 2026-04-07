class RoomCharges<T extends Number> {
    T price;
    T discount;

    RoomCharges(T price, T discount) {
        this.price = price;
        this.discount = discount;
    }

    void calculate() {
        double p = price.doubleValue();
        double d = discount.doubleValue();

        double total = p;
        double finalPrice = p - d;

        System.out.println("Total Price: " + total);
        System.out.println("Discounted Price: " + finalPrice);
    }
}

public class LAB7_Q3 {
    public static void main(String[] args) {
        RoomCharges<Double> r = new RoomCharges(3000.0, 500.0);
        r.calculate();
    }
}
