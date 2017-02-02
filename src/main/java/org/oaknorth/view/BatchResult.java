package org.oaknorth.view;

import java.util.List;

/**
 * Created by Heerok on 31-01-2017.
 */
public class BatchResult {

    private String key;
    private String status;
    private List<CompanyDTO> companies;

    public BatchResult(String key, String status) {
        this.key = key;
        this.status = status;
    }

    public List<CompanyDTO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyDTO> companies) {
        this.companies = companies;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
