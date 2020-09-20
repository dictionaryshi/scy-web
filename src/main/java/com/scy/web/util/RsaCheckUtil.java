package com.scy.web.util;

import com.scy.core.CollectionUtil;
import com.scy.core.ObjectUtil;
import com.scy.core.StringUtil;
import com.scy.core.encode.RsaUtil;
import com.scy.core.enums.ResponseCodeEnum;
import com.scy.core.exception.BusinessException;
import com.scy.core.reflect.AnnotationUtil;
import com.scy.web.annotation.SignCheck;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RsaCheckUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
@Slf4j
public class RsaCheckUtil {

    private RsaCheckUtil() {
    }

    public static final Map<String, List<String>> KEY_MAP = CollectionUtil.unmodifiableMap(CollectionUtil.newHashMap("scy",
            CollectionUtil.newArrayList(
                    "",
                    ""
            )));

    /**
     * 用户标识
     */
    public static final String APPKEY = "appkey";

    /**
     * 时间戳
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * 签名参数
     */
    public static final String SIGNATURE = "signature";

    public static void signCheck(HttpServletRequest request, Method method) {
        SignCheck signCheck = AnnotationUtil.findAnnotation(method, SignCheck.class);
        if (ObjectUtil.isNull(signCheck)) {
            return;
        }

        Map<String, Object> paramMap = HttpParameterUtil.getParamMap();

        String appkey = ObjectUtil.obj2Str(paramMap.get(APPKEY));
        if (StringUtil.isEmpty(appkey)) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "appkey get error");
        }

        List<String> keys = KEY_MAP.get(appkey);
        if (CollectionUtil.isEmpty(keys)) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "appkey not exist");
        }

        Long timestamp = ObjectUtil.obj2Long(paramMap.get(TIMESTAMP));
        if (ObjectUtil.isNull(timestamp)) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "timestamp get error");
        }

        long timeDiff = System.currentTimeMillis() - timestamp;
        long limitSecond = 300_000L;
        if (timeDiff > limitSecond) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "timestamp expired");
        }

        String signature = ObjectUtil.obj2Str(paramMap.get(SIGNATURE));
        if (StringUtil.isEmpty(signature)) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "signature get error");
        }

        String signatureMeta = getSignatureMeta();
        boolean isPass = RsaUtil.checkSign(signatureMeta, signature, keys.get(0));
        if (!isPass) {
            throw new BusinessException(ResponseCodeEnum.SYSTEM_EXCEPTION.getCode(), "signature check error");
        }
    }

    public static String getSignatureMeta() {
        Map<String, Object> signatureParams = new HashMap<>(HttpParameterUtil.getParamMap());
        signatureParams.remove(SIGNATURE);

        return CollectionUtil.map2Str(signatureParams, Boolean.FALSE);
    }

    public static void setSign(Map<String, Object> params, String appkey) {
        params.put(APPKEY, appkey);
        params.put(TIMESTAMP, System.currentTimeMillis());
        params.put(SIGNATURE, RsaUtil.getSign(CollectionUtil.map2Str(params, Boolean.FALSE), KEY_MAP.get(appkey).get(1)));
    }
}
