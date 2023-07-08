/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.querydsl;

import com.querydsl.sql.types.AbstractType;
import cn.ntopic.core.value.NTMapX;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * NTMap类型
 *
 * @see NTMapX
 *
 * @author obullxl 2021年06月13日: 新增
 */
public class NTMapXType extends AbstractType<NTMapX> {

    public NTMapXType() {
        super(Types.VARCHAR);
    }

    public NTMapXType(int type) {
        super(type);
    }

    @Override
    public Class<NTMapX> getReturnedClass() {
        return NTMapX.class;
    }

    @Override
    @Nullable
    public NTMapX getValue(ResultSet rs, int startIndex) throws SQLException {
        return NTMapX.with(rs.getString(startIndex));
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, NTMapX value) throws SQLException {
        st.setString(startIndex, NTMapX.format(value));
    }

}
