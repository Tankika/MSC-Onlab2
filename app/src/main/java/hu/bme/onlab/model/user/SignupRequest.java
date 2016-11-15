package hu.bme.onlab.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "")
public class SignupRequest {

    @SerializedName("email")
    private String email = null;
    @SerializedName("password")
    private String password = null;

    @ApiModelProperty(value = "")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @ApiModelProperty(value = "")
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignupRequest signupRequest = (SignupRequest) o;
        return (email == null ? signupRequest.email == null : email.equals(signupRequest.email)) &&
                (password == null ? signupRequest.password == null : password.equals(signupRequest.password));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (email == null ? 0: email.hashCode());
        result = 31 * result + (password == null ? 0: password.hashCode());
        return result;
    }

    @Override
    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class SignupRequest {\n");

        sb.append("  email: ").append(email).append("\n");
        sb.append("  password: ").append(password).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
