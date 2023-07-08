/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.dag;

import cn.ntopic.core.builder.ToString;

import java.util.List;
import java.util.Optional;

/**
 * DAG图接口
 *
 * @author obullxl 2023年06月10日: 新增
 */
public interface Dag<GID, NID, DATA extends ToString> {

    /**
     * 获取图ID
     */
    GID getId();

    /**
     * 设置图ID
     */
    Dag<GID, NID, DATA> setId(GID id);

    /**
     * 获取开始节点
     */
    DagNode<NID, DATA> getStart();

    /**
     * 设置开始节点
     */
    Dag<GID, NID, DATA> setStart(DagNode<NID, DATA> startNode);

    /**
     * 获取结束节点
     */
    DagNode<NID, DATA> getEnd();

    /**
     * 设置结束节点
     */
    Dag<GID, NID, DATA> setEnd(DagNode<NID, DATA> startNode);

    /**
     * 获取单个节点
     */
    Optional<DagNode<NID, DATA>> getNode(NID id);

    /**
     * 获取所有节点列表
     */
    List<DagNode<NID, DATA>> getNodeList();

    /**
     * 根据编码获取节点列表
     */
    List<DagNode<NID, DATA>> getNodeList(String code);

    /**
     * 获取节点数量
     */
    default int getNodeCount() {
        return this.getNodeList().size();
    }

    /**
     * 插入节点
     */
    Dag<GID, NID, DATA> insertNode(DagNode<NID, DATA> prevNode, DagNode<NID, DATA> node);

    /**
     * 删除边
     */
    boolean removeEdge(DagNode<NID, DATA> fromNode, DagNode<NID, DATA> toNode);

    /**
     * 边检测
     */
    boolean existEdge();

    /**
     * 导出图
     * <pre>
     * start --> data_prepare --> data_download --> file_transform --> file_merge --> compress --> end
     *       --> data_prepare --> data_download --> file_transform --> file_merge -->
     * </pre>
     */
    String dumpGraph();

}
