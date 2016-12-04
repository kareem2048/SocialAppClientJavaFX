package SocialAppClient;

import SocialAppGeneral.Command;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by kemo on 01/12/2016.
 */
public class ImageViewer extends ImageView implements SocialAppImages {
    String id;
    ImageViewer(String id)
    {
        this.id = id;
        Thread thread  = new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    //TODO: #Config
                    new ServerConnection("127.0.0.1", 6010) {
                        @Override
                        public void startConnection() {
                            try {
                                sendCommand(connectionSocket);
                                connectionSocket.setSoTimeout(100000);
                                BufferedImage bufferedImage = ImageIO.read(connectionSocket.getInputStream());
                               Platform.runLater(() -> setImage(SwingFXUtils.toFXImage( bufferedImage, null)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
    void sendCommand(Socket socket)
    {
        try {
            DataOutputStream  dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Command command = new Command();
            command.setKeyWord(DOWNLOADIMAGE);
            command.setSharableObject(id);
            dataOutputStream.writeUTF(command.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}