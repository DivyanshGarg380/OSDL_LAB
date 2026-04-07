public class LAB7_Q2 {
   public static <T> void display(T data) {
        System.out.println("Value: " + data);
   } 

   public static void main(String[] args) {
        display(101);
        display("Deluxe");
        display(2500.75);
        display(true);
   }
}
