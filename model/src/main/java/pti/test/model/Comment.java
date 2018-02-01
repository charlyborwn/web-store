package pti.test.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * THis class is used for comments holding as <code>HashMap</code>
 * where key is a random <code>String</code> value.
 */
public class Comment implements Serializable {

    private HashMap<String, LocalComment> comments;

    public Comment(HashMap<String, LocalComment> comments) {
        this.comments = comments;
    }

    /**
     * This class represents a single comment with the <code>LocalDateTime</code>
     * time and collections of likes and dislikes.
     */
    public static class LocalComment implements Serializable {

        private String text;
        private String user;
        private LocalDateTime dateTime;
        private String time;
        private ArrayList<String> likes;
        private ArrayList<String> dislikes;
        private String id;

        public LocalComment(String text, String user, LocalDateTime dateTime, ArrayList<String> likes, ArrayList<String> dislikes, String id) {
            this.text = text;
            this.user = user;
            this.dateTime = dateTime;
            this.likes = likes;
            this.dislikes = dislikes;
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public String getTime() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
            return dateTime.format(formatter);
        }

        public void setTime(String time) {
            this.time = time;
        }

        public ArrayList<String> getLikes() {
            return likes;
        }

        public void setLikes(ArrayList<String> likes) {
            this.likes = likes;
        }

        public ArrayList<String> getDislikes() {
            return dislikes;
        }

        public void setDislikes(ArrayList<String> dislikes) {
            this.dislikes = dislikes;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        //        public static class UserLikes implements Serializable {
//            private ArrayList<String> users;
//            private int likes;
//
//            public UserLikes(ArrayList<String> users, int likes) {
//                this.users = users;
//                this.likes = likes;
//            }
//
//            public ArrayList<String> getUsers() {
//                return users;
//            }
//
//            public void setUsers(ArrayList<String> users) {
//                this.users = users;
//            }
//
//            public boolean addUsers(String user) {
//                if (users.contains(user)) {
//                    return false;
//                }
//                users.add(user);
//                return true;
//            }
//
//            public int getLikes() {
//                return likes;
//            }
//
//            public void setLikes(int likes) {
//                this.likes = likes;
//            }
//        }
//
//        public static class UserDislikes implements Serializable {
//            private ArrayList<String> users;
//            private int dislikes;
//
//            public UserDislikes(ArrayList<String> users, int dislikes) {
//                this.users = users;
//                this.dislikes = dislikes;
//            }
//
//            public ArrayList<String> getUsers() {
//                return users;
//            }
//
//            public void setUsers(ArrayList<String> users) {
//                this.users = users;
//            }
//
//            public boolean addUsers(String user) {
//                if (users.contains(user)) {
//                    return false;
//                }
//                users.add(user);
//                return true;
//            }
//
//            public int getDislikes() {
//                return dislikes;
//            }
//
//            public void setDislikes(int dislikes) {
//                this.dislikes = dislikes;
//            }
//        }

    }

    public HashMap<String, LocalComment> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, LocalComment> comments) {
        this.comments = comments;
    }

}
