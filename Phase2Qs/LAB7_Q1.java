class Room<T, U> {
    T roomId;
    U detail;

    Room(T roomId, U detail) {
        this.roomId = roomId;
        this.detail = detail;
    }

    void display() {
        System.out.println("Room ID: " + roomId);
        System.out.println("Detail: " + detail);
        System.out.println();
    }
}

public class LAB7_Q1 {
    public static void main(String[] args) {
        Room<Integer, String> r1 = new Room<>(101, "Deluxe");
        Room<String, Double> r2 = new Room<>("A102" , 2500.05);
        r1.display();
        r2.display();
    }
}
