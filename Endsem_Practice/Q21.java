/*
Author :

███████╗████████╗ █████╗ ██████╗  ███╗   ███╗ █████╗ ███╗   ██╗
██╔════╝╚══██╔══╝██╔══██╗██╔══██╗ ████╗ ████║██╔══██╗████╗  ██║
███████╗   ██║   ███████║██████╔╝ ██╔████╔██║███████║██╔██╗ ██║
╚════██║   ██║   ██╔══██║██║  ██║ ██║╚██╔╝██║██╔══██║██║╚██╗██║
███████║   ██║   ██║  ██║██║  ██║ ██║ ╚═╝ ██║██║  ██║██║ ╚████║
╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝  STARMAN248
*/

/* Code provided for reference only and not intended for direct use. If found, I will trace you down 😈*/

/* ------------------ Self Made Question ---------------------------------------- */

/*
    Implement a Product Management System using JavaFX where:
    - Functionalities:
        - Add Product (ID, Name, Price)
        - Prevent duplicate IDs
        - Save all products using Serialization
        - Load products using Deserialization
        - Search product by ID
        - Sort products by Price
        - View all products
    
    - Requirements:
        - Use Generics (Pair<T,U>)
        - Use ArrayList
        - Use Serialization
        - JavaFX GUI
        - Exception handling
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;

public class Q21 extends Application {
    static class Pair<T, U> implements Serializable {
        T first;
        U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    static class Product implements Serializable {
        String id, name;
        double price;

        Product(String id, String name, double price) {
            this.id = id; this.name = name; this.price = price;
        }
    }

    ArrayList<Pair<String, Product>> list = new ArrayList<>();
    @Override
    public void start(Stage stage) {
        TextField id = new TextField(); id.setPromptText("Product ID");
        TextField name = new TextField(); name.setPromptText("Product Name");
        TextField price = new TextField(); price.setPromptText("Price");

        TextArea output = new TextArea();

        Button add = new Button("Add");
        Button save = new Button("Save");
        Button load = new Button("Load");
        Button search = new Button("Search");
        Button sort = new Button("Sort by Price");
        Button view = new Button("View All");

        add.setOnAction(e -> {
            try {
                if (id.getText().isEmpty() || name.getText().isEmpty() || price.getText().isEmpty())
                    throw new Exception("Fill all");

                for(Pair<String, Product> p : list) {
                    if(p.first.equals(id.getText())) throw new Exception("Duplicate ID");
                }

                double p = Double.parseDouble(price.getText());
                Product pr = new Product(id.getText(), name.getText(), p);
                list.add(new Pair<>(id.getText(), pr));
                output.setText("Added");

            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        save.setOnAction(e -> {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("prod.dat"));
                oos.writeObject(list);
                oos.close();
                output.setText("Saved");

            } catch (Exception ex) {
                output.setText("Error saving");
            }
        });
        

        load.setOnAction(e -> {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("prod.dat"));
                list = (ArrayList<Pair<String, Product>>) ois.readObject();
                ois.close();
                output.setText("Loaded");

            } catch (Exception ex) {
                output.setText("Error loading");
            }
        });
        
        search.setOnAction(e -> {
            for(Pair<String, Product> p : list) {
                if(p.first.equals(id.getText())) {
                    Product pr = p.second;
                    output.setText(pr.name + " | " + pr.price);
                    return;
                }
            }
            output.setText("Not Found");
        }); 

        sort.setOnAction(e -> {
            Collection.sort(list, (a, b) -> 
                Double.compare(a.second.price, b.second.price)
            );

            StringBuilder sb = new StringBuilder();
            for (Pair<String, Product> p : list) {
                Product pr = p.second;
                sb.append(pr.id + " | " + pr.name + " | " + pr.price + "\n");
            }

            output.setText(sb.toString());
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Product> p : list) {
                Product pr = p.second;
                sb.append(pr.id + " | " + pr.name + " | " + pr.price + "\n");
            }
            output.setText(sb.toString());
        });



        VBox root = new VBox(10,
                id, name, price,
                add, save, load, search, sort, view,
                output
        );

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Product System");
        stage.show();

    }   

    public static void main(String[] args) {
        launch(args);
    }
}
