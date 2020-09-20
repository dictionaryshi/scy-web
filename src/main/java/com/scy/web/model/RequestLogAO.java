package com.scy.web.model;

import com.scy.core.model.JoinPointBO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

/**
 * RequestLogAO
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
@Getter
@Setter
@ToString
public class RequestLogAO {

    private HttpServletRequest request;

    private JoinPointBO joinPointBO;

    private long startTime;

    private String ip;
}
