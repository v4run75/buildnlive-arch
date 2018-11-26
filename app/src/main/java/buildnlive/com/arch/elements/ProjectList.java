package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class ProjectList extends RealmObject {
    @Index
    @PrimaryKey
    private String id;
    private String name;
    private String type;
    private String address;
    private String status;

    public ProjectList(String id, String name, String type,String address,String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.address=address;
//        this.status=status;
    }

    public ProjectList() {
    }

    public ProjectList parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("project_id"));
        setName(obj.getString("project_name"));
        setType(obj.getString("project_type"));
        setAddress(obj.getString("site_address"));
        setStatus(obj.getString("project_status"));
        return this;
    }

    private void setStatus(String status) {this.status=status;
    }

    public String getStatus() {
        return status;
    }

    private void setAddress(String site_address) {
        this.address=site_address;
    }

    public String getAddress() {
        return address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
