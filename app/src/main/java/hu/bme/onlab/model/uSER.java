package hu.bme.onlab.model;import java.util.ArrayList;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class User {

    @SerializedName("details")
    @Expose
    private Object details;
    @SerializedName("authorities")
    @Expose
    private List<Object> authorities = new ArrayList<Object>();
    @SerializedName("authenticated")
    @Expose
    private Boolean authenticated;
    @SerializedName("principal")
    @Expose
    private Object principal;
    @SerializedName("credentials")
    @Expose
    private Object credentials;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     *
     * @return
     * The details
     */
    public Object getDetails() {
        return details;
    }

    /**
     *
     * @param details
     * The details
     */
    public void setDetails(Object details) {
        this.details = details;
    }

    /**
     *
     * @return
     * The authorities
     */
    public List<Object> getAuthorities() {
        return authorities;
    }

    /**
     *
     * @param authorities
     * The authorities
     */
    public void setAuthorities(List<Object> authorities) {
        this.authorities = authorities;
    }

    /**
     *
     * @return
     * The authenticated
     */
    public Boolean getAuthenticated() {
        return authenticated;
    }

    /**
     *
     * @param authenticated
     * The authenticated
     */
    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     *
     * @return
     * The principal
     */
    public Object getPrincipal() {
        return principal;
    }

    /**
     *
     * @param principal
     * The principal
     */
    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    /**
     *
     * @return
     * The credentials
     */
    public Object getCredentials() {
        return credentials;
    }

    /**
     *
     * @param credentials
     * The credentials
     */
    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

}
