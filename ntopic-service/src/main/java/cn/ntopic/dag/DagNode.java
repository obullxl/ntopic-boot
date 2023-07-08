/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.dag;

import cn.ntopic.core.builder.ToString;

import java.util.List;

/**
 * DAG节点接口
 *
 * @author obullxl 2023年06月10日: 新增
 */
public interface DagNode<NID, DATA extends ToString> {

    /**
     * 开始节点编码
     */
    String START_CODE = "start";

    /**
     * 结束节点编码
     */
    String END_CODE = "end";

    /**
     * 获取节点ID
     */
    NID getId();

    /**
     * 设置节点ID
     */
    DagNode<NID, DATA> setId(NID id);

    /**
     * 获取节点编码
     */
    String getCode();

    /**
     * 设置节点编码
     */
    DagNode<NID, DATA> setCode(String code);

    /**
     * 获取节点数据
     */
    DATA getData();

    /**
     * 设置节点数据
     */
    DagNode<NID, DATA> setData(DATA data);

    /**
     * 获取上游节点列表
     */
    List<DagNode<NID, DATA>> getPrevList();

    /**
     * 获取下游节点列表
     */
    List<DagNode<NID, DATA>> getNextList();
}
