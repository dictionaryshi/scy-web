package com.scy.web.model;

import com.scy.core.format.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * UserTokenBO
 *
 * @author shichunyang
 * Created by shichunyang on 2020/12/8.
 */
@ApiModel(value = "登陆用户信息")
@Getter
@Setter
@ToString
public class UserTokenBO {

    @ApiModelProperty(value = "sso token", required = true)
    private String token;

    @ApiModelProperty(value = "用户id", required = true, example = "123456")
    private Long userId;

    @ApiModelProperty(value = "登陆账号", required = true, example = "13264232894")
    private String passport;

    @ApiModelProperty(value = "用户信息创建时间", required = true, example = DateUtil.DEFAULT_TIME)
    private Date createdAt;
}
