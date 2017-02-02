package org.oaknorth.view;

import java.util.List;

/**
 * Created by Heerok on 02-02-2017.
 */
public class Response<T> {

    private Long total_results;
    private List<T> items;

    public Long getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Long total_results) {
        this.total_results = total_results;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Response{" +
                "total_results=" + total_results +
                ", items=" + items +
                '}';
    }

}
