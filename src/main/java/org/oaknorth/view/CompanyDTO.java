package org.oaknorth.view;

import java.util.List;

/**
 * Created by Heerok on 02-02-2017.
 */
public class CompanyDTO {

    private String company_status;
    private String title;
    private String address_snippet;
    private String company_number;
    private List<OfficerDTO> officers;

    public List<OfficerDTO> getOfficers() {
        return officers;
    }

    public void setOfficers(List<OfficerDTO> officers) {
        this.officers = officers;
    }

    public String getCompany_status() {
        return company_status;
    }

    public void setCompany_status(String company_status) {
        this.company_status = company_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CompanyDTO{" +
                "company_status='" + company_status + '\'' +
                ", title='" + title + '\'' +
                ", address_snippet='" + address_snippet + '\'' +
                ", company_number='" + company_number + '\'' +
                '}';
    }

    public String getAddress_snippet() {
        return address_snippet;
    }

    public void setAddress_snippet(String address_snippet) {
        this.address_snippet = address_snippet;
    }

    public String getCompany_number() {
        return company_number;
    }

    public void setCompany_number(String company_number) {
        this.company_number = company_number;
    }
}
