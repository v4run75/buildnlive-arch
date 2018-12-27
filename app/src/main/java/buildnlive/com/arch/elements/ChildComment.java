package buildnlive.com.arch.elements;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class ChildComment extends RealmObject {

    @Index
    @PrimaryKey
    private String id;
    private String parent_id;
    private String comment;
    private String user;
    private String date;
    public ChildComment() {
    }

    public ChildComment(String id, String comment,String parent_id,String user,String date) {
        this.id = id;
        this.parent_id=parent_id;
        this.comment=comment;
        this.user=user;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public String getId() {
        return id;
    }

    public String getParent_id() {return parent_id;}
}
