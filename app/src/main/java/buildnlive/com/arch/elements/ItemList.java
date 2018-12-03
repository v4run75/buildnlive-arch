package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class ItemList extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name,unit,code;
    public ItemList() {
    }

    public ItemList(String id, String name,String unit,String code) {
        this.id = id;
        this.name = name;
        this.unit=unit;
        this.code=code;
    }

    public ItemList parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("item_id"));
        setName(obj.getString("name"));
        setCode(obj.getString("code"));
        setUnit(obj.getString("units"));
        return this;
    }

    private void setUnit(String item_unit) {this.unit=item_unit;
    }

    private void setCode(String item_code) {this.code=item_code;
    }

    public String getUnit() {
        return unit;
    }

    public String getCode() {
        return code;
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
