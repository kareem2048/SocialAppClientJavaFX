package SocialAppClient.Control;

import SocialAppClient.Main;
import SocialAppClient.Connections.MainServerConnection;
import SocialAppClient.View.*;
import SocialAppGeneral.*;
import SocialAppGeneral.LoggedUser;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by kemo on 10/12/2016.
 */

public class ClientLoggedUser extends LoggedUser {
    public ClientLoggedUser(String id) {
        super(id);
    }

    //TODO #khaled
    //>>>>>>>>>>>>>>>>>>>>>>>>
    @Override
    public void createGroup(String check) {
        Command command = new Command();
        command.setKeyWord(Group.CREATE_GROUP);
        Group group=new Group(check);
        group.setAdminId(Long.parseLong(MainWindow.id));
        command.setSharableObject(group.convertToJsonString());

        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {


            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(Group.CREATE_GROUP)) {
                    Group group1 = Group.fromJsonString(cmd.getObjectStr());
                    //TODO #Fix
                    //fix error on threading
                    groupsId.add(""+group1.getId());
                 //   inorder to recieve in server
                   //@SuppressWarnings("unchecked") ArrayList<String>s=(ArrayList<String>)(ArrayList<?>) socialArrayList.getItems();
                    Platform.runLater(() -> MainWindow.navigateTo(new GroupPage(group1)));

                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);


    }
    //<<<<<<<<<<<<<<<<<<<<<<<<
    @Override     //TODO #khaled
                    //what IS THIS ????
    public void getgroup() {
        Command command = new Command();
        command.setKeyWord(Group.LOAD_GROUP);
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public
                //TODO #ُERORE
            void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(Group.LOAD_GROUP)) {
                    SocialArrayList list=SocialArrayList.convertFromJsonString(cmd.getObjectStr());
                    for(int i=0;i<list.getItems().size();i++) {
                        getGroups().add(Group.fromJsonString(list.getItems().get(i)));
                    }
                }
            }

        };
        CommandsExecutor.getInstance().add(commandRequest);

    }
    public ArrayList<Group> loadGroups(){
        getgroup();
        return  getGroups();
    }
    public abstract class GetGroups
    {
        public GetGroups()
        {
            Command command = new Command();
            command.setKeyWord(Group.LOAD_GROUPS);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public
                    //TODO #ُERORE
                void analyze(Command cmd) {
                    if (cmd.getKeyWord().equals(Group.LOAD_GROUPS)) {
                        getGroups().clear();
                        SocialArrayList list=SocialArrayList.convertFromJsonString(cmd.getObjectStr());
                        for(int i=0;i<list.getItems().size();i++) {
                         getGroups().add(Group.fromJsonString(list.getItems().get(i)));
                        }
                        onFinish(getGroups());
                    }
                }

            };
            CommandsExecutor.getInstance().add(commandRequest);
        }

        public abstract void onFinish(ArrayList<Group> groups);
    }
    public abstract class GetPosts{
        public GetPosts(long numberPost){
            Command command = new Command();
            command.setKeyWord(Post.LOAD_POST_HOME);
            command.setSharableObject(String.valueOf(numberPost));
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command cmd) {
                    if (cmd.getKeyWord().equals(Post.LOAD_POST_HOME)) {
                        SocialArrayList list=SocialArrayList.convertFromJsonString(cmd.getObjectStr());
                        getPosts().clear();
                        for(int i=0;i<list.getItems().size();i++) {

                            getPosts().add(Post.fromJsonString(list.getItems().get(i)));
                        }
                        onFinish( getPosts());
                    }
                }

            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(ArrayList<Post> posts);
        }
    public abstract class GetPostsProfile{
        public GetPostsProfile(long numberPost, String id){
            SocialArrayList posts=new SocialArrayList();
            posts.setExtra(id);
            posts.setTarget(String.valueOf(numberPost));
            Command command = new Command();
            command.setKeyWord(Post.LOAD_POST_USERS);
            command.setSharableObject(posts.convertToJsonString());
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command cmd) {
                    if (cmd.getKeyWord().equals(Post.LOAD_POST_USERS)) {
                        SocialArrayList list=SocialArrayList.convertFromJsonString(cmd.getObjectStr());
                        getPosts().clear();
                        for(int i=0;i<list.getItems().size();i++) {
                            getPosts().add(Post.fromJsonString(list.getItems().get(i)));
                        }

                        onFinish( getPosts());
                    }
                }

            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(ArrayList<Post> posts);
    }
    public abstract class GetPostsGroup{
        public GetPostsGroup(long numberPost, long id){
            SocialArrayList posts=new SocialArrayList();
            posts.setExtra(String.valueOf(id));
            posts.setTarget(String.valueOf(numberPost));
            Command command = new Command();
            command.setKeyWord(Post.LOAD_POST_GROUPS);
            command.setSharableObject(posts.convertToJsonString());
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command cmd) {
                    if (cmd.getKeyWord().equals(Post.LOAD_POST_GROUPS)) {
                        SocialArrayList list=SocialArrayList.convertFromJsonString(cmd.getObjectStr());
                        getPosts().clear();
                        for(int i=0;i<list.getItems().size();i++) {
                            getPosts().add(Post.fromJsonString(list.getItems().get(i)));
                        }
                        onFinish( getPosts());
                    }
                }

            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(ArrayList<Post> posts);
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<
    //Using inner public abstract class to get results
    //as these functions run in another threads
    public abstract class GetFriendReq
    {
        public GetFriendReq()
        {
            Command command = new Command();
            command.setKeyWord(LoggedUser.FETCH_REQS);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
                @Override
                public void analyze(Command cmd) {
                    onFinish(cmd);
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(Command cmd);
    }
        public abstract class GetStatus
        {
            public GetStatus(String id)
            {
                Command command = new Command();
                command.setKeyWord(LoggedUser.GET_RELATION_STATUS);
                command.setSharableObject(id);
                CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
                    @Override
                    public void analyze(Command cmd) {
                        onFinish(cmd.getObjectStr());
                    }
                };
            CommandsExecutor.getInstance().add(commandRequest);
            }
            public abstract void onFinish(String s);
        }

        public abstract class AcceptFriendReq
        {
            public AcceptFriendReq(String id)
            {
                Command command = initialize(id);
                command.setKeyWord(LoggedUser.ACCEPT_FRIEND);
                CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                    @Override
                    public void analyze(Command commandFromServer) {
                        onFinish(commandFromServer);
                    }
                };
                CommandsExecutor.getInstance().add(commandRequest);
            }
            public abstract void onFinish(Command cmd);
        }

        public abstract class DeclineFriendReq
        {
            public DeclineFriendReq(String id)
            {
                Command command = initialize(id);
                command.setKeyWord(LoggedUser.DECLINE_FRIEND);
                CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                    @Override
                    public void analyze(Command commandFromServer) {
                      onFinish(commandFromServer);
                    }
                };
                CommandsExecutor.getInstance().add(commandRequest);
            }
            public abstract void onFinish(Command cmd);
        }
        public abstract class RemoveFriend
        {
            public RemoveFriend(String id)
            {
                Command command = initialize(id);
                command.setKeyWord(LoggedUser.REMOVE_FRIEND);
                CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                    @Override
                    public void analyze(Command commandFromServer) {
                        onFinish(commandFromServer);
                    }
                };
                CommandsExecutor.getInstance().add(commandRequest);
            }
            public abstract void onFinish(Command cmd);
        }
        public abstract class CancelFriendReq
        {
            public CancelFriendReq(String id)
            {
                Command command = initialize(id);
                command.setKeyWord(LoggedUser.CANCEL_FRIEND_REQ);
                CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                    @Override
                    public void analyze(Command commandFromServer) {
                        onFinish(commandFromServer);
                    }
                };
                CommandsExecutor.getInstance().add(commandRequest);
            }
            public abstract void onFinish(Command cmd);
        }
    public abstract class addFriend
    {
        public addFriend(String id)
        {
            Command command = initialize(id);
            command.setKeyWord(LoggedUser.ADD_FRIEND);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command commandFromServer) {
                   onFinish(commandFromServer);
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(Command cmd);
    }
    private Command initialize(String id)
    {
        Command command = new Command();
        command.setSharableObject(id);
        return command;
    }
    public abstract class getFriends
    {
        public getFriends()
        {
            Command command = new Command();
            command.setKeyWord(LoggedUser.GET_FRIENDS);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command ) {
                @Override
                public void analyze(Command cmd) {
                    //TODO #lastly
                    SocialArrayList socialArrayList = SocialArrayList.convertFromJsonString(cmd.getObjectStr());

                    ArrayList<String> strings = socialArrayList.getItems().stream().map(o -> o).collect(Collectors.toCollection(ArrayList::new));
                    onFinish(strings);
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(ArrayList<String> friends);
    }

    public void savePostUser(String relation, String text){
        Post post=new Post();
        post.setOwnerId(Long.parseLong(MainWindow.id));
        post.setContent(text);
        post.setPostPos(Long.parseLong(MainWindow.id));
        Command command = new Command();
        command.setKeyWord(Post.SAVE_POST_USER);
        command.setSharableObject(post.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(Post.SAVE_POST_USER)) {
                    if(relation.equals(Relations.HOME_PAGE.toString()))
                        Platform.runLater(() -> MainWindow.navigateTo(new HomePage(MainWindow.id)));
                    else
                        Platform.runLater(() -> MainWindow.navigateTo(new ProfilePage(""+post.getPostPos())));

                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }
    public void savePostGroup(String text, String id){
        Post post=new Post();
        post.setOwnerId(Long.parseLong(MainWindow.id));
        post.setContent(text);
        post.setPostPos(Long.parseLong(id));
        Command command = new Command();
        command.setKeyWord(Post.SAVE_POST_GROUP);
        command.setSharableObject(post.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(Post.SAVE_POST_GROUP)) {
                    Platform.runLater(() -> MainWindow.navigateTo(new GroupPage(Long.parseLong(id))));

                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }

    public void editPostUser(long postid, long postpos, String text) {
        Post post1 = new Post();
        post1.setId(postid);
        post1.setPostPos(postpos);
        post1.setContent(text);
        Command command = new Command();
        command.setKeyWord(Post.EDITE_POST_USERS);
        command.setSharableObject(post1.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(Post.EDITE_POST_USERS)) {

                    Post b = Post.fromJsonString(cmd.getObjectStr());
                    if (b.getId() ==0) {
                        Platform.runLater(() -> Utility.errorWindow("please refresh window"));


                    }
                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }
    public void editPostGroup(long postid, long postpos, String text) {
        Post post1 = new Post();
        post1.setId(postid);
        post1.setPostPos(postpos);
        post1.setContent(text);
        Command command = new Command();
        command.setKeyWord(Post.EDITE_POST_GROUPS);
        command.setSharableObject(post1.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(Post.EDITE_POST_GROUPS)) {

                    Post b = Post.fromJsonString(cmd.getObjectStr());
                    if (b.getId() ==0) {
                        Platform.runLater(() -> Utility.errorWindow("please refresh window"));
                    }
                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }

    public void deletePostUser(Post post){
        Command command = new Command();
        command.setKeyWord(Post.DELETE_POST_USERS);
        command.setSharableObject(post.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public void analyze(Command cmd) { //ignore
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }
    public void deletePostGroup(Post post){
        Command command = new Command();
        command.setKeyWord(Post.DELETE_POST_GROUPS);
        command.setSharableObject(post.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public void analyze(Command cmd) {
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }

    public void setCommentCommandUser(Relations show, String text, long commentId, long postId, long postPos){
        Comment comment=new Comment();
        comment.setCommentcontent(text);
        comment.setOwnerID(Long.parseLong(MainWindow.id));
        comment.setShow(show);
        comment.setCommentId(commentId);
        AttachmentSender sender=new AttachmentSender(comment,postPos,postId);
        Command command = new Command();
        command.setKeyWord(AttachmentSender.ATTACHMENT_USER);
        command.setSharableObject(sender.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(AttachmentSender.ATTACHMENT_USER)) {
                    Post b= Post.fromJsonString(cmd.getObjectStr());
                    if(b.getId() !=0) {
                        Platform.runLater(() -> Content.showPostDetails(b));
                    }
                    else{
                        Platform.runLater(() ->  Utility.errorWindow("please refresh window"));
                    }
                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }

    public void setCommentCommandGroup(Relations show, String text, long commentId, long postId, long postPos){
        Comment comment=new Comment();
        comment.setCommentcontent(text);
        comment.setOwnerID(Long.parseLong(MainWindow.id));
        comment.setShow(show);
        comment.setCommentId(commentId);
        AttachmentSender sender=new AttachmentSender(comment,postPos,postId);
        Command command = new Command();
        command.setKeyWord(AttachmentSender.ATTACHMENT_GROUP);
        command.setSharableObject(sender.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(AttachmentSender.ATTACHMENT_GROUP)) {
                    Post b= Post.fromJsonString(cmd.getObjectStr());
                    if(b.getId() !=0) {
                        Platform.runLater(() -> Content.showPostDetails(b));
                    }
                    else{
                        Platform.runLater(() ->  Utility.errorWindow("please refresh window"));
                    }
                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }

    public void setLikeCommandUsers(Relations i, Post post) {
        Like like = new Like();
        like.setLike(i);
        like.setOwnerID(Long.parseLong(MainWindow.id));
        AttachmentSender sender=new AttachmentSender(like,post.getPostPos(),post.getId());
        Command command = new Command();
        command.setKeyWord(AttachmentSender.ATTACHMENT_USER);
        command.setSharableObject(sender.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(AttachmentSender.ATTACHMENT_USER)) {
                    int check = Utility.checkID(post);
                    Post b= Post.fromJsonString(cmd.getObjectStr());
                    if (b.getId() !=0) {
                        if (check == -1) {
                            post.getLike().add(like);
                        } else {
                            post.getLike().get(check).setLike(i);
                        }

                    } else {

                        Platform.runLater(() ->  Utility.errorWindow("please refresh window"));


                    }

                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }

    public void setLikeCommandGroup(Relations i, Post post) {
        Like like = new Like();
        like.setLike(i);
        like.setOwnerID(Long.parseLong(MainWindow.id));
        AttachmentSender sender=new AttachmentSender(like,post.getPostPos(),post.getId());
        Command command = new Command();
        command.setKeyWord(AttachmentSender.ATTACHMENT_GROUP);
        command.setSharableObject(sender.convertToJsonString());
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public void analyze(Command cmd) {
                if (cmd.getKeyWord().equals(AttachmentSender.ATTACHMENT_GROUP)) {
                    int check = Utility.checkID(post);
                    Post b= Post.fromJsonString(cmd.getObjectStr());
                    if (b.getId() !=0) {
                        if (check == -1) {
                            post.getLike().add(like);
                        } else {
                            post.getLike().get(check).setLike(i);
                        }

                    } else {
                        Platform.runLater(() ->  Utility.errorWindow("please refresh window"));
                    }
                }
            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }

    public void deactivate(){
        Command command = new Command();
        command.setKeyWord(LoggedUser.DEACTIVATE);
        CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
            @Override
            public void analyze(Command commandFromServer) {
                Platform.runLater(() -> {
                    Utility.alertWindow(" Deactivation","We are sorry about that feeling, Please check your E-mail!");
                    Main.logout();
                });

            }
        };
        CommandsExecutor.getInstance().add(commandRequest);
    }
    public abstract class LoadNotification
    {
        public LoadNotification()
        {
            Command command = new Command();
            command.setKeyWord(Notification.LOAD_NOTI);

            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command commandFromServer) {
                    onFinish (SocialArrayList.convertFromJsonString(commandFromServer.getObjectStr()));
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(SocialArrayList list );
    }
    public abstract class GetLogs
    {
        protected GetLogs()
        {
            Command command = new Command();
            command.setKeyWord(Log.LOAD_LOG);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket, command) {
                @Override
                public void analyze(Command commandFromServer) {
                    getLogs().clear();
                    SocialArrayList list=SocialArrayList.convertFromJsonString(commandFromServer.getObjectStr());
                    for(int i=0;i<list.getItems().size();i++) {
                        getLogs().add(Log.fromJsonString(list.getItems().get(i)));
                    }
                    onFinish(getLogs());
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }

        public abstract void onFinish(ArrayList<Log> logs);
    }
    public abstract class ReActivate
    {
        public ReActivate()
        {
            Command command = new Command();
            command.setKeyWord(REACTIVATE);
            CommandRequest commandRequest = new CommandRequest(MainServerConnection.mainConnectionSocket,command) {
                @Override
                public void analyze(Command cmd) {
                    onFinish(cmd.getObjectStr());
                }
            };
            CommandsExecutor.getInstance().add(commandRequest);
        }
        public abstract void onFinish(String result);
    }
}
