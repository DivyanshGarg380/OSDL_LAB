public class LAB7_Q4 {
    public static <T> void printArray(T[] arr) {
        for(T item: arr) {
            System.out.println(item + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] roomNumbers = {101, 102, 103};
        String[] roomTypes = {"Deluxe", "Suite", "Standard"};
        Double[] prices = {2000.0, 3500.0, 1500.0};

        printArray(roomNumbers);
        printArray(roomTypes);
        printArray(prices);
    }
}
