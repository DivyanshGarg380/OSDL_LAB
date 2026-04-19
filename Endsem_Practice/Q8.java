/*
Author :

███████╗████████╗ █████╗ ██████╗  ███╗   ███╗ █████╗ ███╗   ██╗
██╔════╝╚══██╔══╝██╔══██╗██╔══██╗ ████╗ ████║██╔══██╗████╗  ██║
███████╗   ██║   ███████║██████╔╝ ██╔████╔██║███████║██╔██╗ ██║
╚════██║   ██║   ██╔══██║██║  ██║ ██║╚██╔╝██║██╔══██║██║╚██╗██║
███████║   ██║   ██║  ██║██║  ██║ ██║ ╚═╝ ██║██║  ██║██║ ╚████║
╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝  STARMAN248
*/

/*
    Hotel billing with wrapper classes, autoboxing, and JavaFX

    - Build a hotel billing JavaFX application that stores room tariff and number of days as wrapper class objects (Integer for days, Double for tariff). 
    - Use autoboxing when assigning primitive values to these wrappers, and unboxing when computing the total bill (tariff × days + 18% GST). 
    - The GUI should include TextFields for tariff and days, a Button 'Generate Bill', and a Label that shows the itemized bill: base amount, GST amount, and total. 
    - Internally, all intermediate calculations must pass through wrapper objects at least once — do not use only primitives throughout.
*/

import javafx.apllication.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.scene.control.*;

public class Q8 extends Application {
    static class BillUtil {
        static String generateBill(String tariff, String days) {
            try {
                double t = Double.parseDouble(tariff);
                int d = Integer.parseInt(days);
                Double tarif = t;
                Integer day = d;
                double base = tarif * day;
                Double baseAmount = base;
                double gst = baseAmount * 0.18;
                Double gstAmount = gst;
                double total = baseAmount + gstAmount;
                Double totalAmount = total;

                return "Base Amount: " + baseAmount +
                       "\nGST (18%): " + gstAmount +
                       "\nTotal Bill: " + totalAmount;
            } catch (Exception e) {
                return "Invalin Input!";
            }
        }
    }

    @Override
    public void start(Stage stage) {
        TextField txt = new TextField();
        txt.setPromptText("Enter Tariff");

        TextField days = new TextField();
        days.setPromptText("Enter Days");

        Button btn = new Button("Generate Bill");
        Label res = new Label();

        btn.setOnAction(e -> {
            String output = BillUtil.generateBill(txt.getText(), days.getText());
            res.setText(output);
        });

        VBox root = new VBox(10, txt, days, btn, res);

        Scene scene = new Scene(root, 500, 600);

        stage.setTitle("Hotel Billing");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}