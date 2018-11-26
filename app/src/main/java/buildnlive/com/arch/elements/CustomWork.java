package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class CustomWork extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String name,unit,project_work_list_id,master_work_id;
    public CustomWork() {
    }

    public CustomWork(String id, String name,String unit,String project_work_list_id,String master_work_id) {
        this.id = id;
        this.name = name;
        this.unit=unit;
        this.project_work_list_id=project_work_list_id;
        this.master_work_id=master_work_id;
    }

    public CustomWork parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("work_id"));
        setProjectWorkListId(obj.getString("project_work_list_id"));
        setMasterWorkId(obj.getString("master_work_id"));
        setUnit(obj.getString("work_unit"));
        setName(obj.getString("work_name"));
        setUnit(obj.getString("work_unit"));
        return this;
    }

    private void setMasterWorkId(String master_work_id) { this.master_work_id=master_work_id;
    }

    private void setProjectWorkListId(String project_work_list_id) {this.project_work_list_id=project_work_list_id;
    }

    private void setUnit(String work_unit) { this.unit=unit;
    }

    public String getMasterWorkId() {
        return master_work_id;
    }

    public String getProjectWorkListId() {
        return project_work_list_id;
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
