package info.mrconst.qlient.model;

import java.io.Serializable;

public class Street implements Serializable {
    String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
