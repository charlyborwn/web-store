package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pti.test.controller.Controller;
import pti.test.model.Comment;
import pti.test.model.DTO.ProductDTO;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.ImagesEngine;
import pti.test.server.interfaces.ProductEngine;
import pti.test.server.interfaces.UserEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.time.LocalDateTime;
import java.util.*;

/**
 * This class is used for obtaining information about
 * product. In addition it has a user's actions functionality
 * implementations.
 */

@ManagedBean
@RestController
@ViewScoped
public class ProductViewBean {

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private UserEngine userEngine;

    @Autowired
    private ImagesEngine imagesEngine;

//    @Autowired
//    private UserService userService;

    @Autowired
    private Logger logger;

    private ProductDTO product = new ProductDTO();
    private long ipk;
    private String comment;
    private ArrayList<Comment.LocalComment> userComments;


    /**
     * Checks a length of entered message and if it is larger than 3
     * symbols saves it to DB. Every message has its own unique ID
     * obtained using <code>RandomUUID</code>.
     */
    public void updateComments() {
        if (comment.length() < 3) {
            JSFMessages.error("To short comment.", "Enter at least 3 symbols!");
            return;
        }
        Comment comments = product.getComments();
        if (comments == null) {
            comments = new Comment(new HashMap<>());
        }
        HashMap<String, Comment.LocalComment> map = comments.getComments();
        if (map == null) {
            map = new HashMap<>();
        }
        String id = UUID.randomUUID().toString();
        Comment.LocalComment localComment = new Comment.LocalComment(comment, userEngine.getUser().getName(),
                LocalDateTime.now(), new ArrayList<>(), new ArrayList<>(), id);
        map.put(id, localComment);
        product.setComments(new Comment(map));
        productEngine.save(product);
        JSFMessages.info("Your comment is sent!");
        clear();
    }

    /**
     * Obtains current user.
     *
     * @return current user
     */
//    private Users getUser() {
//        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return userService.findByMail(principal.getUsername());
//    }

    /**
     * Initiates <code>ProductDTO</code> product using obtained from faces
     * context external ID.
     */
    public void initProduct() {
        product = productEngine.findByIpk(ipk);
        imagesEngine.serveFileSystem(ipk);
    }

    /**
     * Is responsible for idle message showing.
     */
    public void onIdle() {
        JSFMessages.warn("No activity", "What are you doing over there?");
    }

    /**
     * Is responsible for activity message showing.
     */
    public void onActive() {
        JSFMessages.info("Welcome Back", "Well, that's a long coffee break!");
    }

    /**
     * Shows availability information about product.
     *
     * @return 'Available' if product's count is larger then 0 or 'Not' if it is not
     */
    public String availability() {
        return product.getCount() > 0 ? "Available in store" : "Not available";
    }

    /**
     * @return information about product's discount
     */
    public String saled() {
        String s = product.getInfo().get("Discount");
        if (s != null){
            return "Discount: " + (s.contains("%") ? s : s.concat("%"));
        }
        return "Discount: 0%";
    }

    /**
     * Removes the entered comment from class field.
     */
    public void clear() {
        comment = null;
    }

    /**
     * Is responsible for comments liking. If the comment has no
     * current user's like then it will be added. If the comment has
     * user's dislike then it will be vanished and like will be added.
     * If the comment already has current user's like then it will
     * be vanished as well.
     *
     * @param id current comment ID
     */
    public void setLike(String id) {
        HashMap<String, Comment.LocalComment> comments = product.getComments().getComments();
        Comment.LocalComment localComment = comments.get(id);
        ArrayList<String> likes = localComment.getLikes();
        ArrayList<String> dislikes = localComment.getDislikes();
        String mail = userEngine.getUser().getMail();
        if (!likes.contains(mail) & !dislikes.contains(mail)) {
            likes.add(mail);
            localComment.setLikes(likes);
            comments.put(id, localComment);
            JSFMessages.info("You successfully like this comment!", "To repeal press like button once more.");
        } else if (likes.contains(mail)) {
            likes.remove(mail);
            localComment.setLikes(likes);
            comments.put(id, localComment);
            JSFMessages.warn("You have successfully remove your like.", null);
        } else if (!likes.contains(mail)) {
            likes.add(mail);
            localComment.setLikes(likes);
            dislikes.remove(mail);
            localComment.setDislikes(dislikes);
            comments.put(id, localComment);
            JSFMessages.info("You successfully like this comment!", "To repeal press like button once more.");
        }
        product.setComments(new Comment(comments));
        productEngine.save(product);
    }

    /**
     * Is responsible for comments disliking. If the comment has no
     * current user's dislike then it will be added. If the comment has
     * user's like then it will be vanished and dislike will be added.
     * If the comment already has current user's dislike then it will
     * be vanished as well.
     *
     * @param id current comment ID
     */
    public void setDislike(String id) {
        HashMap<String, Comment.LocalComment> comments = product.getComments().getComments();
        Comment.LocalComment localComment = comments.get(id);
        ArrayList<String> likes = localComment.getLikes();
        ArrayList<String> dislikes = localComment.getDislikes();
        String mail = userEngine.getUser().getMail();
        if (!likes.contains(mail) & !dislikes.contains(mail)) {
            dislikes.add(mail);
            localComment.setDislikes(dislikes);
            comments.put(id, localComment);
            JSFMessages.info("You successfully dislike this comment!", "To repeal press like button once more.");
        } else if (dislikes.contains(mail)) {
            dislikes.remove(mail);
            localComment.setDislikes(dislikes);
            comments.put(id, localComment);
            JSFMessages.warn("You have successfully remove your dislike.", null);
        } else if (!dislikes.contains(mail)) {
            dislikes.add(mail);
            localComment.setDislikes(dislikes);
            likes.remove(mail);
            localComment.setLikes(likes);
            comments.put(id, localComment);
            JSFMessages.info("You successfully dislike this comment!", "To repeal press dislike button once more.");
        }
        product.setComments(new Comment(comments));
        productEngine.save(product);
    }

    /**
     * Removes a comment with appointed ID from DB.
     *
     * @param id current comment ID
     */
    public void deleteComment(String id) {
        HashMap<String, Comment.LocalComment> comments = product.getComments().getComments();
        comments.remove(id);
        product.setComments(new Comment(comments));
        productEngine.save(product);
    }

    public ProductDTO getProduct() {
        return productEngine.findByIpk(ipk);
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public long getIpk() {
        return ipk;
    }

    public void setIpk(long ipk) {
        this.ipk = ipk;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<Comment.LocalComment> getUserComments() {
        Comment comments = product.getComments();
        if (comments == null) {
            return new ArrayList<>();
        }
        ArrayList<Comment.LocalComment> list = new ArrayList<>();
        comments.getComments().entrySet().stream().forEach(x -> list.add(x.getValue()));
        Collections.sort(list, (o1, o2) -> -o1.getDateTime().compareTo(o2.getDateTime()));
        userComments = list;
        return userComments;
    }

    public void setUserComments(ArrayList<Comment.LocalComment> userComments) {
        this.userComments = userComments;
    }

    public String forBarCode() {
        return String.valueOf(ipk);
    }

}
