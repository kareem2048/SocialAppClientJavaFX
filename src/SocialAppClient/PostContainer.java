package SocialAppClient;

import SocialAppGeneral.Post;
import SocialAppGeneral.Relations;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Created by billy on 2016-11-26.
 */
public class PostContainer extends VBox implements CallBack {
    private Button loadPostBtn;
    private static String relation;
    private static Pane mainWindow;
    long loadMoreNum;
    public PostContainer(String relation) {
        this.relation = relation;
        mainWindow = this;
        loadMoreNum =1;
        setLayout();
    }

    private void setLayout() {
        setAlignment(Pos.TOP_CENTER);
        setSpacing(20);
        setPadding(new Insets(30, 0, 30, 0));
    }
    public void addPosts(ArrayList<Post> posts, String id) {

        for (int i = 0; i < posts.size(); i++) {
            PostViewer postViewer = new PostViewer(relation, posts.get(i));
            getChildren().add(postViewer);
        }

        if(!relation.equals(Relations.HOME_PAGE.toString())) {

            if (posts.size() == 10) {
                loadPostBtn = new Button("LOAD MORE");
                loadPostBtn.setStyle(Styles.BLACK_BUTTON);
                loadPostBtn.setOnMouseEntered(event -> loadPostBtn.setStyle(Styles.BLACK_BUTTON_HOVER));
                loadPostBtn.setOnMouseExited(event -> loadPostBtn.setStyle(Styles.BLACK_BUTTON));

                getChildren().add(loadPostBtn);
                loadPostBtn.setOnMouseClicked(event -> {
                    getChildren().remove(loadPostBtn);
                    loadMoreNum++;
                    if (relation.equals(Relations.PROFILE_PAGE.toString())) {
                        MainWindow.clientLoggedUser.new GetPostsProfile(loadMoreNum, id) {
                            @Override
                            void onFinish(ArrayList<Post> posts) {
                                Platform.runLater(() -> addPosts(posts, id));
                            }
                        };
                    } else if (relation.equals(Relations.GROUP.toString())) {
                        MainWindow.clientLoggedUser.new GetPostsGroup(loadMoreNum, Long.parseLong(id)) {
                            @Override
                            void onFinish(ArrayList<Post> posts) {
                                Platform.runLater(() -> addPosts(posts, id));
                            }
                        };
                    }
                });

            }
        }
    }

    @Override
    public void showPostDetails(Post post) {

        getChildren().clear();
        ((CallBack)getParent()).removePostWriter();
        getChildren().addAll(new PostDetails(relation,post));
    }
    public void removePostWriter(){
    }

    @Override
    public void setCommentCommend(int show, String text, long id) {

    }
    public static void reloadPostDetails(Post post){
        mainWindow.getChildren().clear();
        mainWindow.getChildren().addAll(new PostDetails(relation,post));
    }

}
