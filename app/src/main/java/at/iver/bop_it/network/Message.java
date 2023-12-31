/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Message implements Serializable {
    private MessageType type;
    private Map<DataKey, Object> data;

    public Message(MessageType type) {
        this.type = type;
        this.data = new HashMap<>();
    }

    public MessageType getType() {
        return type;
    }

    public void setData(DataKey key, Object value) {
        if (validateData(key, value)) {
            data.put(key, value);
        } else {
            throw new IllegalArgumentException("Invalid data type for key " + key);
        }
    }

    public Object getData(DataKey key) {
        return data.get(key);
    }

    /**
     * Validates data entered into the message's key-value pairs.
     *
     * @param key The type of data in the field.
     * @param value The value of the field.
     * @return True if a given value is valid for provided DataKey.
     */
    private boolean validateData(DataKey key, Object value) {
        switch (key) {
            case TYPE:
            case ID:
            case EXTRA:
            case ROUNDS:
                return value instanceof Integer;
            case TIME:
                return value instanceof Long;
            case RESULTS:
                return value instanceof long[];
            case SCORES:
                return value instanceof int[];
            case ISSIMON:
            case SIMON_MODE:
                return value instanceof Boolean;
            case NAME:
                return value instanceof String;
            case ROUND_RECORDS:
                return value instanceof List;
            default:
                return false;
        }
    }
}
