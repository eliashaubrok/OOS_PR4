package bank;

import com.google.gson.*;

import java.lang.reflect.Type;
public class CustomJson implements JsonDeserializer<Transaction>, JsonSerializer<Transaction> {
    /**
     * deserilizing jason in transaction
     *
     * @param jsonElement Jason Element to be deserilized
     * @param type  Type of Jason Element
     * @param jsonDeserializationContext
     * @return  deserilized object
     * @throws JsonParseException deserilizingexception
     */
    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject copy = jsonElement.getAsJsonObject();
        JsonObject instance = (JsonObject)copy.get("INSTANCE");
        Transaction transaction;
        switch (copy.get("CLASSNAME").getAsString()) {
            case "Payment":
                transaction = (new Gson()).fromJson(instance, Payment.class);
                break;
            case "OutgoingTransfer":
                transaction = (new Gson()).fromJson(instance, OutgoingTransfer.class);
                break;
            case "IncomingTransfer":
                transaction = (new Gson()).fromJson(instance, IncomingTransfer.class);
                break;
            case "Transfer":
                transaction = (new Gson()).fromJson(instance, Transfer.class);
                break;
            default:
                throw new JsonParseException("Falsche Type");
        }

        return transaction;
    }
    /**
     * serelizing transaction into json
     *
     * @param transaction   transaction object
     * @param type    type of serilization
     * @param jsonSerializationContext
     * @return JSON-Element, which was the result of serilization
     */
    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonInstance = new JsonObject();
        if (transaction instanceof IncomingTransfer x) {
            jsonObject.addProperty("CLASSNAME", "IncomingTransfer");
            jsonInstance.addProperty("sender", x.getsender());
            jsonInstance.addProperty("recipient", x.getrecipient());
            jsonInstance.addProperty("date", x.getdate());
            jsonInstance.addProperty("amount", x.getamount());
            jsonInstance.addProperty("description", x.getdescription());
            jsonObject.add("INSTANCE", jsonInstance);
        } else if (transaction instanceof OutgoingTransfer x) {
            jsonObject.addProperty("CLASSNAME", "OutgoingTransfer");
            jsonInstance.addProperty("sender", x.getsender());
            jsonInstance.addProperty("recipient", x.getrecipient());
            jsonInstance.addProperty("date", x.getdate());
            jsonInstance.addProperty("amount", x.getamount());
            jsonInstance.addProperty("description", x.getdescription());
            jsonObject.add("INSTANCE", jsonInstance);
        } else if (transaction instanceof Payment x) {
            jsonObject.addProperty("CLASSNAME", "Payment");
            jsonInstance.addProperty("incomingInterest", x.getIncomingIntrest());
            jsonInstance.addProperty("outgoingInterest", x.getOutgoingIntrest());
            jsonInstance.addProperty("date", x.getdate());
            jsonInstance.addProperty("amount", x.getamount());
            jsonInstance.addProperty("description", x.getdescription());
            jsonObject.add("INSTANCE", jsonInstance);
        }

        return jsonObject;
    }
}
