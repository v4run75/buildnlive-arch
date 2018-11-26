package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class CustomActivity extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name;
    public CustomActivity() {
    }

    public CustomActivity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CustomActivity parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("activity_id"));
        setName(obj.getString("activity_name"));
        return this;
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
}
