package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class CreateWork extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name,unit;
    public CreateWork() {
    }

    public CreateWork(String id, String name,String unit) {
        this.id = id;
        this.name = name;
        this.unit=unit;
    }

    public CreateWork parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("work_id"));
        setName(obj.getString("work_name"));
        setUnit(obj.getString("work_unit"));
        return this;
    }

    private void setUnit(String work_unit) { this.unit=unit;
    }

    public String getUnit() {
        return unit;
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
