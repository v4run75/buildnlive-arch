package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class LocalPurchaseItem extends RealmObject {
    @Index
    @PrimaryKey
    String purchase_id;
    private String item_name,quantity,total_amount,vendor_name,bill_no;
    private String bill_copy;
    public LocalPurchaseItem() {
    }

    public LocalPurchaseItem(String id, String item_name, String quantity, String total_amount, String vendor_name, String bill_no, String bill_copy) {
        this.purchase_id = id;
        this.item_name=item_name;
        this.quantity=quantity;
        this.total_amount=total_amount;
        this.vendor_name=vendor_name;
        this.bill_no=bill_no;
        this.bill_copy=bill_copy;
    }

    public LocalPurchaseItem parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("purchase_id"));
        setName(obj.getString("item_name"));
        seQuantity(obj.getString("quantity"));
        setAmount(obj.getString("total_amount"));
        setVendor(obj.getString("vendor_name"));
        setBillNo(obj.getString("bill_no"));
        setBillCopy(obj.getString("bill_copy"));
        return this;
    }

    public String getBill_copy() {
        return bill_copy;
    }

    public String getBill_no() {
        return bill_no;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getPurchase_id() {
        return purchase_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getVendor_name() {
        return vendor_name;
    }
    public String getQuantity() {
        return quantity;
    }

    private void setBillCopy(String bill_copy) {this.bill_copy=bill_copy;
    }

    private void setBillNo(String bill_no) {this.bill_no=bill_no;
    }

    private void setVendor(String vendor_name) {this.vendor_name=vendor_name;
    }

    private void setAmount(String total_amount) {this.total_amount=total_amount;
    }

    private void seQuantity(String quantity) {this.quantity=quantity; }

    public String getId() {
        return purchase_id;
    }

    public void setId(String id) {
        this.purchase_id = id;
    }

    public String getName() {
        return item_name;
    }

    public void setName(String name) {
        this.item_name = name;
    }
}
