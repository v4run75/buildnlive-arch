package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class PurchaseOrder extends RealmObject {
    @Index
    @PrimaryKey
    String id;
    private String rate,qty;
    public PurchaseOrder() {
    }

    public PurchaseOrder(String id, String rate) {
        this.id = id;
        this.rate = rate;
    }

    public PurchaseOrder parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("item_id"));
        setRate(obj.getString("rate"));
        setQuantity(obj.getString("qty"));
        return this;
    }

    private void setQuantity(String qty) {this.qty=qty;
    }

    public String getQuantity() {
        return qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
