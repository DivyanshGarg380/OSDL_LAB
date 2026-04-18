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
    File copy utility with byte streams and JavaFX

    - Write a JavaFX desktop application that copies the binary contents of one file to another using FileInputStream and FileOutputStream. 
    - The GUI should have two TextFields — one for the source file path and one for the 
        destination path — a Button labeled 'Copy File', and a Label that reports either 'File copied successfully.' or the error message if an IOException occurs. 
    - The copy must be done byte by byte in a loop (do not use any NIO shortcuts). 
    - After copying, read the destination file back using a FileReader and display its text content in a TextArea inside the same JavaFX window.
*/

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.io.*;

public class Q6 extends Application {
    static class FileUtil {
        static void copyFile(String source, String dest) throws IOException {
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(dest);

            int data;
            while((data = fis.read()) != -1) fos.write(data);

            fis.close();
            fos.close();
        }

        static String readFile(String path) throws IOException {
            FileReader fr = new FileReader(path);
            StringBuilder content = new StringBuilder();

            int ch;
            while((ch = fr.read()) != -1) {
                content.append((char) ch);
            }

            fr.close();
            return content.toString();
        }
    }

    @Override
    public void start(Stage stage) {
        TextField tsource = new TextField();
        tsource.setPromptText("Source File Path");

        TextField tdest = new TextField();
        tdest.setPromptText("Destination File Path");

        Button btnCopy = new Button("Copy File");
        Label status = new Label();

        TextArea output = new TextArea();
        output.setPromptText("File content will appear here");

        btnCopy.setOnAction(e -> {
            try {
                String src = tsource.getText();
                String dest = tdest.getText();

                FileUtil.copyFile(src, dest);
                String content = FileUtil.readFile(dest);
                output.setText(content);
                status.setText("File copied successfully");
            } catch (IOException ex) { status.setText("Error: " + ex.getMessage()); }
        });

        VBox root = new VBox(10, tsource, tdest, btnCopy, output, status);
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("File Copy Utility");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}