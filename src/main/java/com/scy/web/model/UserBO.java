package com.scy.web.model;

import com.scy.core.format.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * UserBO
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
@ApiModel(value = "登陆用户信息")
@Getter
@Setter
@ToString
public class UserBO {

    @ApiModelProperty(value = "用户id", required = true, example = "10")
    private Integer id;

    @ApiModelProperty(value = "用户名", required = true, example = "13264232894")
    private String username;

    @ApiModelProperty(value = "用户信息创建时间", required = true, example = DateUtil.DEFAULT_TIME)
    private Date createdAt;
}
