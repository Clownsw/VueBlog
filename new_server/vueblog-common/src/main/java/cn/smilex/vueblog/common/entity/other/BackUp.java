package cn.smilex.vueblog.common.entity.other;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/12/17/13:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackUp {
    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String passWord;

    @JsonProperty("operator")
    private String operator;

    @JsonProperty("operator_password")
    private String operatorPassWord;

    @JsonProperty("bucket_name")
    private String bucketName;
}
