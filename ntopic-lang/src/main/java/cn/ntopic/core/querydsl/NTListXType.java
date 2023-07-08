/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.querydsl;

import com.querydsl.sql.types.AbstractType;
import cn.ntopic.core.value.NTListX;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * NTList类型
 *
 * @author obullxl 2021年06月13日: 新增
 * @see NTListX
 */
public class NTListXType extends AbstractType<NTListX> {

    public NTListXType() {
        super(Types.VARCHAR);
    }

    public NTListXType(int type) {
        super(type);
    }

    @Override
    public Class<NTListX> getReturnedClass() {
        return NTListX.class;
    }

    @Override
    @Nullable
    public NTListX getValue(ResultSet rs, int startIndex) throws SQLException {
        return NTListX.with(rs.getString(startIndex));
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, NTListX value) throws SQLException {
        st.setString(startIndex, NTListX.format(value));
    }

}
