package cn.smilex.vueblog.common.entity.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/12/3/22:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    @JsonProperty("username")
    private String userName;

    @JsonProperty("password")
    private String password;
}
