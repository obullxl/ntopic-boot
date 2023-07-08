/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package mixture;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.system.SystemProperties;

import java.net.InetAddress;
import java.util.List;

/**
 * 系统参数查看
 *
 * @author obullxl 2021年03月20日: 新增
 */
public class SystemPropertiesMain {
    /**
     * 系统参数Key
     */
    private static final List<String> PROP_KEY_LIST = Lists.newArrayList();

    static {
        PROP_KEY_LIST.add("NT_DB_USER");
        PROP_KEY_LIST.add("NT_DB_PASSWD");
        PROP_KEY_LIST.add("NT_DB_DRIVER");
        PROP_KEY_LIST.add("NT_DB_URL");
    }

    /**
     * 打印参数值
     */
    public static void main(String[] args) throws Exception {
        System.out.println(SystemUtils.USER_NAME);
        System.out.println(InetAddress.getLocalHost().getHostName());

        for (String propKey : PROP_KEY_LIST) {
            System.out.print(propKey);
            System.out.print(": ");
            System.out.println(SystemProperties.get(propKey));
        }
    }
}
