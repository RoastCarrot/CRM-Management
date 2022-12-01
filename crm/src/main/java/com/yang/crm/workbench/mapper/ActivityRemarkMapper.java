package com.yang.crm.workbench.mapper;

import com.yang.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Mar 13 15:48:02 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Mar 13 15:48:02 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Mar 13 15:48:02 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Mar 13 15:48:02 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Mar 13 15:48:02 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sun Mar 13 15:48:02 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);

    /**
     * 根据市场活动id查询该市场活动的所有备注
     * @param activityId 市场活动id
     * @return 备注列表
     */
    List<ActivityRemark> selectActivityRemarkForDetailByActivityId(String activityId);

    /**
     * 添加市场活动备注
     * @param activityRemark 市场活动备注
     * @return 添加的条数
     */
    int insertActivityRemark(ActivityRemark activityRemark);

    /**
     * 通过市场活动id删除备注（用于删除市场活动时同时删除该市场活动备注；因为可能一次性会删除多个市场活动，所以传入的id是市场活动id数组）
     * @param activityIds
     * @return 删除的条数
     */
    int deleteActivityRemarkByActivityId(String[] activityIds);

    /**
     * 根据备注id删除市场活动备注
     * @param id 备注的id
     * @return 删除的条数
     */
    int deleteActivityRemarkById(String id);

    /**
     * 更新市场活动备注
     * @param activityRemark 更新的市场活动备注
     * @return 更新的条数
     */
    int updateActivityRemark(ActivityRemark activityRemark);
}