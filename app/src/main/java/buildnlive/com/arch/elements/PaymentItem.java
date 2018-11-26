package buildnlive.com.arch.elements;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class PaymentItem extends RealmObject {
    @Index
    @PrimaryKey
    String account_detail_id;
    private String item_name,type,payment_type,payee,total_amount,overheads,comments;
    private String bill_copy;
    public PaymentItem() {
    }

    public PaymentItem(String id, String item_name, String type, String total_amount, String payment_type, String overheads, String bill_copy,String comments,String payee) {
        this.account_detail_id= id;
        this.item_name=item_name;
        this.type=type;
        this.total_amount=total_amount;
        this.payment_type=payment_type;
        this.overheads=overheads;
        this.bill_copy=bill_copy;
        this.comments=comments;
        this.payee=payee;
    }

    public PaymentItem parseFromJSON(JSONObject obj) throws JSONException {
        setId(obj.getString("account_detail_id"));
        setName(obj.getString("name"));
        setType(obj.getString("type"));
        setAmount(obj.getString("amount"));
        setPayee(obj.getString("payee"));
        setPaymentType(obj.getString("payment_type"));
        setComments(obj.getString("comment"));
        setOverheads(obj.getString("overheads"));
        setBillCopy(obj.getString("bill_copy"));
        return this;
    }

    public String getComments() {
        return comments;
    }

    public String getPayee() {
        return payee;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getType() {
        return type;
    }

    public String getOverheads() {
        return overheads;
    }

    public String getBill_copy() {
        return bill_copy;
    }

    public String getTotal_amount() {
        return total_amount;
    }
    public String getName() {
        return item_name;
    }
    public String getId() {
        return account_detail_id;
    }


    private void setBillCopy(String bill_copy) {this.bill_copy=bill_copy;
    }


    private void setAmount(String total_amount) {this.total_amount=total_amount;
    }


    public void setId(String id) {
        this.account_detail_id = id;
    }


    public void setName(String name) {
        this.item_name = name;
    }
    private void setPaymentType(String payment_type) {this.payment_type=payment_type;
    }

    private void setOverheads(String overheads) {this.overheads=overheads;
    }

    private void setComments(String comments) { this.comments=comments;
    }

    private void setPayee(String payee) {this.payee=payee;
    }

    private void setType(String type) {this.type=type;
    }

}
