package org.oaknorth.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.oaknorth.view.CompanyDTO;
import org.oaknorth.view.OfficerDTO;
import org.oaknorth.view.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Heerok on 02-02-2017.
 */
@Service
public class IntegrationService {

    @Value("${api.companieshouse.key}")
    private String apikey;

    @Value("${api.companieshouse.companySearchUrl}")
    private String searchCompanyUrl;

    @Value("${api.companieshouse.officerSearchUrl}")
    private String searchOfficerUrl;


    public List<CompanyDTO> searchCompany(String name){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createHeaders(apikey);
        String searchUrl = searchCompanyUrl+"?q="+name;
        List<CompanyDTO> companyDTOList=null;
        ResponseEntity<Response> response = restTemplate.exchange(searchUrl, HttpMethod.GET,
                new HttpEntity<>(headers),Response.class);
        if(HttpStatus.OK.equals(response.getStatusCode())){
            companyDTOList = parserCompanyDTO(response.getBody().getItems());
            for(CompanyDTO company:companyDTOList){
                String url = searchOfficerUrl+company.getCompany_number()+"/officers";
                ResponseEntity<Response> officerResponse = restTemplate.exchange(url, HttpMethod.GET,
                        new HttpEntity<>(headers),Response.class);
                if(HttpStatus.OK.equals(officerResponse.getStatusCode())){
                    company.setOfficers(officerResponse.getBody().getItems());
                }
            }
        }
        return companyDTOList;
    }

    private List<CompanyDTO> parserCompanyDTO(List<LinkedHashMap<String,String>> maps){
        List<CompanyDTO> dtos = new ArrayList<>();
        for(LinkedHashMap<String,String> o:maps){
            CompanyDTO dto = new CompanyDTO();
            dto.setTitle(o.get("title"));
            dto.setAddress_snippet(o.get("address_snippet"));
            dto.setCompany_number(o.get("company_number"));
            dto.setCompany_status(o.get("company_status"));
            dtos.add(dto);
        }
        return dtos;
    }

    public List<CompanyDTO> searchCompany1(String name){
        RestTemplate restTemplate = new RestTemplate();
        String searchUrl = searchCompanyUrl+"?key="+apikey+"&q="+name;
        List<CompanyDTO> companyDTOList=null;
        Response<CompanyDTO> response = restTemplate.getForObject(searchUrl,Response.class);
        if(response!=null){
            companyDTOList = response.getItems();
            for(CompanyDTO company:companyDTOList){
                String url = searchOfficerUrl+company.getCompany_number()+"/officers?key="+apikey;
                Response<OfficerDTO> response1 = restTemplate.getForObject(url,Response.class);
                    company.setOfficers(response1.getItems());
            }
        }
        return companyDTOList;
    }

    private HttpHeaders createHeaders(final String key){
        HttpHeaders headers =  new HttpHeaders(){
            {
                byte[] encodedAuth = Base64.encodeBase64(
                        key.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                set( "Authorization", authHeader );
            }
        };
        headers.add("Content-Type", "application/json");
        return headers;
    }

}
