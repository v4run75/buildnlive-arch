package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Employee extends RealmObject {
    @Index
    @PrimaryKey
    private String user_id;
    private String mobile_no;
    private String email_add;
    private String profession_name;

    public Employee(String user_id,String mobile_no, String email_add, String profession_name) {
        this.user_id=user_id;
        this.mobile_no = mobile_no;
        this.email_add = email_add;
        this.profession_name = profession_name;
    }

    public Employee() {
    }

    public Employee parseFromJSON(JSONObject obj) throws JSONException {
        setUserId(obj.getString("user_id"));
        setMobileNo(obj.getString("mobile_no"));
        setEmail(obj.getString("email_add"));
        setProfessionName(obj.getString("profession_name"));
        return this;
    }

    private void setUserId(String user_id) { this.user_id=user_id;
    }

    private void setProfessionName(String profession_name) {this.profession_name=profession_name;
    }

    private void setEmail(String email_add) { this.email_add=email_add;
    }

    private void setMobileNo(String mobile_no) { this.mobile_no=mobile_no;
    }

    public String getProfession_name() {
        return profession_name;
    }

    public String getEmail_add() {
        return email_add;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getUser_id() {
        return user_id;
    }
}
