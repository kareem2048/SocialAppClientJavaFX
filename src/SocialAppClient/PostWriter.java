package SocialAppClient;

import SocialAppGeneral.Command;
import SocialAppGeneral.Post;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by billy on 2016-11-26.
 */
public class PostWriter extends VBox{
    private TextArea postText;
    public Button addImage;
    public Button postBtn;
    public PostWriter(){

        setLayout();
    }

    private void setLayout(){
        setAlignment(Pos.CENTER);
        postText = new TextArea();
        postText.setPromptText("What's on your mind?!");
        postText.setWrapText(true);
        postText.setFont(Font.font(18));
        postText.setMaxSize(450,100);

        HBox option = new HBox();
        addImage = new Button("Choose an image");
        postBtn = new Button("Post");
        addImage.setStyle("-fx-font: 12 arial; -fx-background-color: #000000; -fx-text-fill: #eeeeee;");
        addImage.setOnMouseEntered(event -> addImage.setStyle("-fx-font: 12 arial; -fx-background-color: #999999; -fx-text-fill: #000000;"));
        addImage.setOnMouseExited(event -> addImage.setStyle("-fx-font: 12 arial; -fx-background-color: #000000; -fx-text-fill: #eeeeee;"));

        addImage.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

            File file = fileChooser.showOpenDialog(null);

            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            } catch (Exception ex) {
            }

        });

        postBtn.setStyle("-fx-font: 12 arial; -fx-background-color: #000000; -fx-text-fill: #eeeeee;");
        postBtn.setOnMouseEntered(event -> postBtn.setStyle("-fx-font: 12 arial; -fx-background-color: #999999; -fx-text-fill: #000000;"));
        postBtn.setOnMouseExited(event -> postBtn.setStyle("-fx-font: 12 arial; -fx-background-color: #000000; -fx-text-fill: #eeeeee;"));




        option.setSpacing(150);
        option.setAlignment(Pos.CENTER);
        option.getChildren().addAll(addImage,postBtn);
        setPadding(new Insets(30,0,0,0));

        getChildren().addAll(postText, option);
    }
    public String getPostText(){

        return postText.getText();
    }
    public void SavePost(String id){
        postBtn.setOnAction(e->{
            Post post=new Post();
            post.setOwnerId(Long.parseLong(MainWindow.id));
            post.setContent(getPostText());
            post.setPostPos(Long.parseLong(MainWindow.id));
            Command command = new Command();
            command.setKeyWord(Post.SAVE_POST_USER);
            command.setSharableObject(post.convertToJsonString());
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
                @Override
                void analyze(Command cmd) {
                    if (cmd.getKeyWord().equals(Post.SAVE_POST_USER)) {


                    }
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);

        });
    }
}
