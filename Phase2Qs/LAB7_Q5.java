class Pair<T, U> {
    T first;
    U second;

    Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    void display() {
        System.out.println("Room No: " + first);
        System.out.println("Guest: " + second);
        System.out.println();
    }
}

public class LAB7_Q5 {
    public static void main(String[] args) {
        Pair<Integer, String> p1 = new Pair<>(101, "Rahul");
        Pair<Integer, String> p2 = new Pair<>(102, "Ankit");
        p1.display();
        p2.display();
    }
}
