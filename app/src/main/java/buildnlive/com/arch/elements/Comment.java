package buildnlive.com.arch.elements;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Comment extends RealmObject {

    private String comment,id,child_id="";
    private String quantity;
//    private String child_comment="";
//    private ArrayList<ChildComment> child_comment;
    private String images;
    private String user;
    private String date;


    public Comment() {
    }

    public Comment( String id,String comment,String quantity,String child_id,String images,String user,String date) {
        this.id = id;
        this.comment=comment;
        this.quantity=quantity;
        this.child_id=child_id;
//        this.child_comment=child_comment;
        this.images=images;
        this.user=user;
        this.date=date;
    }
//    public Comment parseFromJSON(JSONObject obj,JSONArray child) throws JSONException {
//        setId(obj.getString("id"));
//        setComment(obj.getString("comment"));
//        setQuantity(obj.getString("qty"));
////        setChildId(child.getString("id"));
////        setChildComment(child.getString("comment"));
//
////        for (int i=0;i<child.length();i++)
////        {
////                JSONObject jsonObject=child.getJSONObject(i);
////                setChild(jsonObject);
////        }
//        setImage(obj.getString("images"));
//        return this;
//    }

//    private void setChild(JSONObject jsonObject) throws JSONException {
//        child_comment.add(new ChildComment(jsonObject.getString("id"),jsonObject.getString("comment")));
//    }

    public Comment parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("id"));
        setComment(obj.getString("comment"));
        setQuantity(obj.getString("qty"));
//        setChildId(child.getString("id"));
//        setChildComment(child.getString("comment"));
        setImage(obj.getString("images"));
        setUser(obj.getString("user"));
        setDate(obj.getString("date"));
        return this;
    }

    private void setDate(String date) { this.date=date;
    }

    private void setUser(String user) { this.user=user;
    }

    public String getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

//    public ArrayList<ChildComment> getChild_comment() {
//        return child_comment;
//    }

    //    public ArrayList<String> getChild_comment() {
//        return child_comment;
//    }
//
//    public String getChild_id() {
//        return child_id;
//    }

    public String getComment() {
        return comment;
    }

    public String getImages() {
        return images;
    }

    private void setImage(String images) {this.images=images;
    }

//    private void setChildComment(ArrayList<String> comment) {this.child_comment=comment;
//    }
//
//    private void setChildId(String id) {this.child_id=id;
//    }

    private void setComment(String comment) {this.comment=comment;
    }

    private void setId(String id) {this.id=id;
    }


    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
