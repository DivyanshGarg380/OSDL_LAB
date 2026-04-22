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

/* ------------------ IT-C Question ---------------------------------------- */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

public class Q22 extends Application {
    static class Pair<T, U> {
        T first;
        U second;

        Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
    }

    static class Book {
        String id, name, author;
        boolean issued;
        Book(String id, String name, String author) {
            this.id = id; this.name = name; this.author = author; this.issued = false;
        }
    }

    HashMap<String, Pair<String, Book>> map = new HashMap<>();

    @Override
    public void start(Stage stage) {
        TextField id = new TextField(); id.setPromptText("Book ID");
        TextField name = new TextField(); name.setPromptText("Book Name");
        TextField author = new TextField(); author.setPromptText("Author");
        TextArea output = new TextArea();

        Button add = new Button("Add");
        Button view = new Button("View");
        Button searchId = new Button("Search by ID");
        Button searchAuthor = new Button("Search by Author");
        Button delete = new Button("Delete");
        Button issue = new Button("Issue Book");
        Button ret = new Button("Return Book");

        add.setOnAction(e -> {
            try {
                if (id.getText().isEmpty() || name.getText().isEmpty() || author.getText().isEmpty())
                    throw new Exception("Fill all");

                if (map.containsKey(id.getText()))
                    throw new Exception("Duplicate ID");

                Book b = new Book(id.getText(), name.getText(), author.getText());
                map.put(id.getText(), new Pair<>(name.getText(), b));

                output.setText("Book Added");

            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        view.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Pair<String, Book> p : map.values()) {
                Book b = p.second;
                sb.append(b.id + " | " + b.name + " | " + b.author + " | Issued: " + b.issued + "\n");
            }
            output.setText(sb.toString());
        });

        searchId.setOnAction(e -> {
            Pair<String, Book> p = map.get(id.getText());

            if (p != null) {
                Book b = p.second;
                output.setText(b.name + " | " + b.author + " | Issued: " + b.issued);
            } else {
                output.setText("Not found");
            }
        });

        searchAuthor.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();

            for (Pair<String, Book> p : map.values()) {
                Book b = p.second;
                if (b.author.equals(author.getText())) {
                    sb.append(b.name + " | Issued: " + b.issued + "\n");
                }
            }

            output.setText(sb.length() == 0 ? "No books" : sb.toString());
        });

        delete.setOnAction(e -> {
            try {
                if (!map.containsKey(id.getText()))
                    throw new Exception("Not found");

                map.remove(id.getText());
                output.setText("Deleted");

            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        issue.setOnAction(e -> {
            try {
                Pair<String, Book> p = map.get(id.getText());

                if (p == null)
                    throw new Exception("Not found");

                Book b = p.second;

                if (b.issued)
                    throw new Exception("Already issued");

                b.issued = true;
                output.setText("Book Issued");

            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        ret.setOnAction(e -> {
            try {
                Pair<String, Book> p = map.get(id.getText());

                if (p == null)
                    throw new Exception("Not found");

                Book b = p.second;

                if (!b.issued)
                    throw new Exception("Already available");

                b.issued = false;
                output.setText("Book Returned");

            } catch (Exception ex) {
                output.setText("Error: " + ex.getMessage());
            }
        });

        VBox root = new VBox(10,
                id, name, author,
                add, view, searchId, searchAuthor, delete,
                issue, ret,
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