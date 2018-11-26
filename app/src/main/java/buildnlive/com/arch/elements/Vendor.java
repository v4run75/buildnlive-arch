package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Vendor extends RealmObject {
    @Index
    @PrimaryKey
    private String vendor_id;
    private String vendor_name;
    private String vendor_add;
    private String type_id;
    private String gst;

    public Vendor(String vendor_id,String vendor_name, String vendor_add, String type_id,String gst) {
        this.vendor_id=vendor_id;
        this.vendor_name = vendor_name;
        this.vendor_add = vendor_add;
        this.type_id = type_id;
        this.gst=gst;
    }

    public Vendor() {
    }

    public Vendor parseFromJSON(JSONObject obj) throws JSONException {
        setVendorId(obj.getString("vendor_id"));
        setVendorName(obj.getString("vendor_name"));
        setVendorAddress(obj.getString("vendor_add"));
        setTypeId(obj.getString("vendor_type"));
        setGST(obj.getString("gst"));
        return this;
    }

    private void setGST(String gst) {this.gst=gst;
    }

    public String getGST() {
        return gst;
    }

    private void setVendorId(String vendor_id) { this.vendor_id=vendor_id;
    }

    private void setTypeId(String type_id) {this.type_id=type_id;
    }

    private void setVendorAddress(String vendor_add) { this.vendor_add=vendor_add;
    }

    private void setVendorName(String vendor_name) { this.vendor_name=vendor_name;
    }

    public String getType_id() {
        return type_id;
    }

    public String getVendor_add() {
        return vendor_add;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public String getVendor_id() {
        return vendor_id;
    }
}
