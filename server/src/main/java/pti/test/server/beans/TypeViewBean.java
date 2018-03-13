package pti.test.server.beans;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.Type;
import pti.test.server.interfaces.TypeEngine;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for creating the
 * navigation tree that consists of product
 * findAll ang categories lists corresponding them.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@Scope("view")
public class TypeViewBean {

    @Autowired
    private TypeEngine typeEngine;

    private TreeNode root;

    @PostConstruct
    public void init() {
        root = treeNode();
    }

    /**
     * Forms the <code>TreeNode</code> tree that consists of product
     * findAll ang categories lists corresponding them.
     *
     * @return <code>TreeNode</code> filled with product
     * findAll ang categories
     */
    private TreeNode treeNode() {

        TreeNode root = new DefaultTreeNode("Types", null);
        Set<String> types = typeEngine.getTypeList().stream().map(Type::getType).collect(Collectors.toSet());
        for (String t : types) {
            TreeNode type = new DefaultTreeNode(t, root);
            List<Type> categories = typeEngine.getTypeList().stream().filter(x -> x.getType().equals(t)).collect(Collectors.toList());
            for (Type c : categories) {
                TreeNode category = new DefaultTreeNode(c.getCategory(), type);
            }
        }
        return root;

    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

}
