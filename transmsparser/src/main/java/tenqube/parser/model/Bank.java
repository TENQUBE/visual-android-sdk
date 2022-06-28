package tenqube.parser.model;

import java.io.Serializable;

/**
 * Created by tenqube on 2017. 3. 27..
 */

public class Bank implements Serializable {


    public int id;
    public String name;
    public int isDelete;

    public Bank() {
    }

    public Bank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getInsertValue() {
        return "(" +
                id + "," +
                "'" +name + "')";
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
