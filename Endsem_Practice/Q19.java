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
    Implement a Library Book Management System using JavaFX where:
    - Functionalities:
        - Add Book (Book ID, Title, Author, Price)
        - Save all books to file using Serialization
        - Load books from file using Deserialization
        - Search book by ID
        - View all books
        - Prevent duplicate Book IDs
    - Requirements:
        - Use Generics (Pair<T,U>)
        - Use HashMap for storage (ID → Book)
        - Use Serialization
        - JavaFX GUI
        - Proper exception handling
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;
import java.io.*;

public class Q19 extends Application {
    static class Pair<T, U> implements Serializable {
        T first;
        U second;
        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    static class Book implements Serializable {
        String id, title, author;
        double price;

        Book(String id, String title, String author, double price) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.price = price;
        }
    }

    HashMap<String, Pair<String, Book>> map = new HashMap<>();

    @Override
    public void start(Stage stage){
        TextField id = new TextField(); id.setPromptText("Book ID");
        TextField title = new TextField(); title.setPromptText("Title");
        TextField author = new TextField(); author.setPromptText("Author");

        TextField price = new TextField(); price.setPromptText("Price");
        TextArea output = new TextArea();

        Button add = new Button("Add Book");
        Button save = new Button("Save");
        Button load = new Button("Load");
        Button search = new Button("Search");
        Button view = new Button("View All");

        add.setOnAction(e -> {
            try {
                if (id.getText().isEmpty() || title.getText().isEmpty() ||
                        author.getText().isEmpty() || price.getText().isEmpty())
                    throw new Exception("Fill all fields");

                if(map.containsKey(id.getText())) throw new Exception("Duplicate ID");
                double p = Double.parseDouble(price.getText());
                Book b = new Book(id.getText(), title.getText(), author.getText(), p);
                map.put(id.getText(), new Pair<>(title.getText(), b));
                output.setText("Book Added");    

            } catch (Exception ex) { output.setText("Error: " + ex.getMessage()); }
        });

        save.setOnAction(e -> {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("book.dat"));
                oos.writeObject(map);
                oos.close();
                output.setText("Saved");
            } catch (Exception ex) { output.setText("Error Saving!"); }
        });

        load.setOnAction(e -> {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book.dat"));
                map = (HashMap<String, Pair<String, Book>>) ois.readObject();
                ois.close();
                output.setText("Loaded!");
            } catch (Exception ex) { output.setText("Error Loading!"); }
        });

        search.setOnAction(e -> {
            Pair<String, Book> p = map.get(id.getText());
            if(p != null) {
                Book b = p.second;
                output.setText(b.title + " | " + b.author + " | " + b.price);
            } else {
                output.setText("Not Found!");
            }
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Book> p : map.values()) {
                Book b = p.second;
                sb.append(b.id + " | " + b.title + " | " + b.author + " | " + b.price + "\n");
            }
            output.setText(sb.toString());
        });

        VBox root = new VBox(10,
                id, title, author, price,
                add, save, load, search, view,
                output
        );

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Library System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}