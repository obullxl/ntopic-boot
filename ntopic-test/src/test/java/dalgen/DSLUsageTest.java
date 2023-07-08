///**
// * Author: obullxl@163.com
// * Copyright (c) 2020-2021 All Rights Reserved.
// */
//package dalgen;
//
//import com.querydsl.core.Tuple;
//import com.querydsl.sql.dml.SQLDeleteClause;
//import com.querydsl.sql.dml.SQLInsertClause;
//import com.querydsl.sql.dml.SQLUpdateClause;
//import com.querydsl.sql.mysql.MySQLQueryFactory;
//import dalgen.core.NTGenUtils;
//import ntopic.NTBootApplication;
//import ntopic.core.value.NTListX;
//import ntopic.core.value.NTMapX;
//import ntopic.das.enums.UserRoleEnum;
//import ntopic.das.model.UserBaseDO;
//import ntopic.das.table.UserBaseDSL;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.sql.DataSource;
//import java.util.Date;
//import java.util.List;
//
///**
// * QueryDSL使用
// *
// * @author obullxl 2021年06月12日: 新增
// */
//@SpringBootTest(classes = {NTBootApplication.class})
//@RunWith(SpringRunner.class)
//public class DSLUsageTest {
//
//    /**
//     * DSL工厂
//     *
//     * @see ntopic.NTBootBeanConfig#dslFactory(DataSource)
//     */
//    @Autowired
//    private MySQLQueryFactory dslFactory;
//
//    private static final String USER_ID_PRE = "NT-USER-";
//    private static final String USER_ID_001 = USER_ID_PRE + "001";
//    private static final String USER_ID_002 = USER_ID_PRE + "002";
//    private static final String USER_ID_003 = USER_ID_PRE + "003";
//
//    /**
//     * 数据清理
//     */
//    @After
//    public void after() {
//        UserBaseDSL table = UserBaseDSL.DSL;
//        long count = this.dslFactory.delete(table).where(table.id.like(USER_ID_PRE + "%")).execute();
//        NTGenUtils.output("After:数据清理[%s]条.", count);
//    }
//
//    /**
//     * CRUD
//     */
//    @Test
//    public void test() {
//        UserBaseDSL table = UserBaseDSL.DSL;
//
//        /**
//         * INSERT
//         */
//        this.dslFactory.delete(table).where(table.id.eq(USER_ID_001)).execute();
//        this.dslFactory.delete(table).where(table.id.eq(USER_ID_002)).execute();
//        this.dslFactory.delete(table).where(table.id.eq(USER_ID_003)).execute();
//
//        NTMapX ntMapX = NTMapX.with();
//
//        // 001
//        ntMapX.put("id", "001");
//
//        UserBaseDO dataRecord = new UserBaseDO();
//        dataRecord.setId(USER_ID_001);
//        dataRecord.setName("NT测试001");
//        dataRecord.setPassword("xyz");
//        dataRecord.setRoleList(NTListX.with(UserRoleEnum.USER.getCode(), UserRoleEnum.ADMIN.getCode()));
//        dataRecord.setExtMap(ntMapX);
//        dataRecord.setCreateTime(new Date());
//        dataRecord.setModifyTime(new Date());
//
//        String id = this.dslFactory.insertIgnore(table).populate(dataRecord).executeWithKey(table.id);
//        Assert.assertNull(id);
//
//        // 002
//        ntMapX.put("id", "002");
//
//        dataRecord = new UserBaseDO();
//        dataRecord.setId(USER_ID_002);
//        dataRecord.setName("NT测试002");
//        dataRecord.setPassword("xyz");
//        dataRecord.setRoleList(NTListX.with(UserRoleEnum.USER.getCode(), UserRoleEnum.ADMIN.getCode()));
//        dataRecord.setExtMap(ntMapX);
//        dataRecord.setCreateTime(new Date());
//        dataRecord.setModifyTime(new Date());
//
//        long insert = this.dslFactory.insertIgnore(table).populate(dataRecord).execute();
//        Assert.assertEquals(1, insert);
//
//        // 003
//        ntMapX.put("id", "003");
//
//        SQLInsertClause insertClause = this.dslFactory.insert(table);
//        insertClause.set(table.id, USER_ID_003);
//        insertClause.set(table.name, "NT测试003");
//        insertClause.set(table.password, "xyz");
//        insertClause.set(table.roleList, NTListX.with(UserRoleEnum.USER.getCode(), UserRoleEnum.ADMIN.getCode()));
//        insertClause.set(table.extMap, ntMapX);
//        insertClause.set(table.createTime, new Date());
//        insertClause.set(table.modifyTime, new Date());
//
//        Assert.assertEquals(1, insertClause.execute());
//
//        /**
//         * SELECT
//         */
//        List<Tuple> dataRows = this.dslFactory.select(table.all())
//                .from(table)
//                .where(table.id.like(USER_ID_PRE + "%"))
//                .limit(10).fetch();
//
//        List<UserBaseDO> dataRecords = table.toRecords(dataRows);
//        Assert.assertEquals(3, dataRecords.size());
//
//        for (UserBaseDO userBaseDO : dataRecords) {
//            Assert.assertNotNull(userBaseDO.getRoleList());
//            Assert.assertTrue(userBaseDO.getExtMap().containsKey("id"));
//        }
//
//        /**
//         * GroupBy
//         */
//        List<Tuple> tuples = this.dslFactory.select(table.roleList
//                , table.id.count(), table.createTime.max())
//                .from(table)
//                .where(table.id.like(USER_ID_PRE + "%"))
//                .groupBy(table.roleList)
//                .limit(3L)
//                .fetch();
//
//        Assert.assertFalse(tuples.isEmpty());
//
//        for (Tuple tuple : tuples) {
//            NTListX roleList = tuple.get(table.roleList);
//            Long count = tuple.get(table.id.count());
//            Date maxCreate = tuple.get(table.createTime.max());
//
//            Assert.assertNotNull(roleList);
//            Assert.assertNotNull(maxCreate);
//            Assert.assertTrue(count > 0L);
//        }
//
//        /**
//         * UPDATE
//         */
//        SQLUpdateClause updateClause = this.dslFactory.update(table);
//
//        ntMapX.put("id", "001");
//        ntMapX.put("update", "true");
//        updateClause.set(table.extMap, ntMapX).where(table.id.eq(USER_ID_001)).limit(1).addBatch();
//
//        ntMapX.put("id", "002");
//        updateClause.set(table.extMap, ntMapX).where(table.id.eq(USER_ID_002)).limit(1).addBatch();
//
//        ntMapX.put("id", "003");
//        updateClause.set(table.extMap, ntMapX).where(table.id.eq(USER_ID_003)).limit(1).addBatch();
//
//        Assert.assertEquals(3, updateClause.execute());
//
//        dataRows = this.dslFactory.select(table.all())
//                .from(table)
//                .where(table.id.like(USER_ID_PRE + "%"))
//                .limit(10).fetch();
//
//        dataRecords = table.toRecords(dataRows);
//        Assert.assertEquals(3, dataRecords.size());
//
//        for (UserBaseDO userBaseDO : dataRecords) {
//            Assert.assertNotNull(userBaseDO.getRoleList());
//            Assert.assertTrue(userBaseDO.getExtMap().containsKey("id"));
//            Assert.assertEquals("true", userBaseDO.getExtMap().get("update"));
//        }
//
//        /**
//         * DELETE
//         */
//        SQLDeleteClause deleteClause = this.dslFactory.delete(table);
//        deleteClause.where(table.id.eq(USER_ID_001)).limit(1).addBatch();
//        deleteClause.where(table.id.eq(USER_ID_002)).limit(1).addBatch();
//        deleteClause.where(table.id.eq(USER_ID_003)).limit(1).addBatch();
//
//        Assert.assertEquals(3, deleteClause.execute());
//    }
//
//}
