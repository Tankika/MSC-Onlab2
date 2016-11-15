/**
 * ServiceFinder API
 * No descripton provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.bme.onlab.model.post;

import java.math.BigDecimal;

import io.swagger.annotations.*;

import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ListPostsRequest  {

  @SerializedName("page")
  private BigDecimal page = null;
  @SerializedName("pageSize")
  private BigDecimal pageSize = null;

  @ApiModelProperty(value = "")
  public BigDecimal getPage() {
    return page;
  }
  public void setPage(BigDecimal page) {
    this.page = page;
  }

  @ApiModelProperty(value = "")
  public BigDecimal getPageSize() {
    return pageSize;
  }
  public void setPageSize(BigDecimal pageSize) {
    this.pageSize = pageSize;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListPostsRequest listPostsRequest = (ListPostsRequest) o;
    return (page == null ? listPostsRequest.page == null : page.equals(listPostsRequest.page)) &&
        (pageSize == null ? listPostsRequest.pageSize == null : pageSize.equals(listPostsRequest.pageSize));
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * result + (page == null ? 0: page.hashCode());
    result = 31 * result + (pageSize == null ? 0: pageSize.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListPostsRequest {\n");
    
    sb.append("  page: ").append(page).append("\n");
    sb.append("  pageSize: ").append(pageSize).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}