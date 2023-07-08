/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.querydsl;

import com.querydsl.sql.types.AbstractType;
import cn.ntopic.core.NTEnum;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * EnumBase类型
 *
 * @see NTEnum
 *
 * @author obullxl 2021年06月13日: 新增
 */
public class NTEnumType<T extends NTEnum> extends AbstractType<T> {

    private final Class<T> type;

    public NTEnumType(Class<T> type) {
        this(Types.VARCHAR, type);
    }

    public NTEnumType(int jdbcType, Class<T> type) {
        super(jdbcType);
        this.type = type;
    }

    @Override
    public Class<T> getReturnedClass() {
        return this.type;
    }

    @Override
    @Nullable
    public T getValue(ResultSet rs, int startIndex) throws SQLException {
        return this.convert(rs.getString(startIndex));
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, T value) throws SQLException {
        st.setString(startIndex, NTEnum.findEnumCode(value));
    }

    /**
     * 枚举转换
     */
    private T convert(String code) {
        T[] enums = this.type.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return null;
        }

        for (T target : enums) {
            if (StringUtils.equals(code, NTEnum.class.cast(target).getCode())) {
                return target;
            }
        }

        return null;
    }

}
