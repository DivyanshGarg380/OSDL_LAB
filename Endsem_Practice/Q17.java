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

/* ------------------ IT-D Question ---------------------------------------- */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import java.util.*;

import javax.swing.Box;

public class Q17 extends Application {
    static class Pair<T> {
        T value;
        Pair(T value) this.value = value;
    }

    HashSet<Pair<String>> items = new HashSet<>();

    @Override
    public void start(Stage stage) {
        TextField item = new TextField(); item.setPromptText("Enter Item Name");
        TextArea output = new TextArea();

        Button add = new Button("Add Item");
        Button remove = new Button("Remove Item");
        Button search = new Button("Search Item");
        Button view = new Button("View All");
        Button sort = new Button("Sort Items");

        add.setOnAction(e -> {
            try {
                String titem = item.getText();

                if (titem.isEmpty())
                    throw new Exception("Enter item");

                for (Pair<String> b : items) {
                    if (b.value.equals(item))
                        throw new Exception("Duplicate item");
                }

                items.add(new Pair<>(titem));
                output.setText("Item Added");

            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        remove.setOnAction(e -> {
            try {
                String titem = item.getText();
                boolean found = false;

                Iterator<Pair<String>> it = items.iterator();
                while(it.hasNext()) {
                    Pair<String> b = it.next();
                    if(b.value.equals(titem)) {
                        it.remove();
                        found = true;
                        break;
                    }
                }

                if(!found) throw new Exception("Item not found");
                output.setText("Item removed");
            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        search.setOnAction(e -> {
            String titem = item.getText();
            boolean found = false;

            for(Pair<String> b : items) {
                if(b.value.equals(titem)) {
                    output.setText("Found: " + b.value);
                    found = true;
                    break;
                }
            }

            if(!found) output.setText("Not found");
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String> b : items) {
                sb.append(b.value + "\n");
            }
            output.setText(sb.toString());
        });

        sort.setOnAction(e -> {
            ArrayList<String> list = new ArrayList<>();

            for (Pair<String> b : items) {
                list.add(b.value);
            }

            Collections.sort(list);

            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s + "\n");
            }

            output.setText(sb.toString());
        });

        VBox root = new VBox(10,
                itemField,
                add, remove, search, view, sort,
                output
        );

        Scene scene = new Scene(root, 350, 450);
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
