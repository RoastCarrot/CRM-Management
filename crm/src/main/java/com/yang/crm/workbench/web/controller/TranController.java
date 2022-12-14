package com.yang.crm.workbench.web.controller;

import com.yang.crm.commons.constants.Constants;
import com.yang.crm.commons.domain.ReturnObject;
import com.yang.crm.commons.utils.DateUtils;
import com.yang.crm.commons.utils.UUIDUtils;
import com.yang.crm.settings.domain.DicValue;
import com.yang.crm.settings.domain.User;
import com.yang.crm.settings.service.DicValueService;
import com.yang.crm.settings.service.UserService;
import com.yang.crm.workbench.domain.*;
import com.yang.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TranController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private TranService tranService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;

        @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/queryTransactionByConditionForPage.do")
    @ResponseBody
    public Object queryTransactionByConditionForPage(String owner, String name, String customerId, String stage, String type,
                                                     String source, String contactsId, int pageNo, int pageSize) {
        // ????????????
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("name", name);
        map.put("customerId", customerId);
        map.put("source", source);
        map.put("stage", stage);
        map.put("type", type);
        map.put("contactsId", contactsId);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        // ??????
        List<Tran> tranList = tranService.queryTransactionByConditionForPage(map);
        int totalRows = tranService.queryCountOfTransactionByCondition(map);
        // ???????????????????????????????????????
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("tranList", tranList);
        resultMap.put("totalRows", totalRows);
        return resultMap;
    }

    @RequestMapping("/workbench/transaction/toSavePage.do")
    public String toSavePage(HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/queryActivityByFuzzyName.do")
    @ResponseBody
    public Object queryActivityByFuzzyName(String activityName) {
        List<Activity> activityList = activityService.queryActivityByFuzzyName(activityName);
        return activityList;
    }

    @RequestMapping("/workbench/transaction/queryContactsByFuzzyName.do")
    @ResponseBody
    public Object queryContactsByFuzzyName(String contactsName) {
        List<Contacts> contactsList = contactsService.queryContactsByFuzzyName(contactsName);
        return contactsList;
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        // ??????properties??????????????????????????????????????????
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        // ??????????????????
        return possibility;
    }

    @RequestMapping("/workbench/transaction/queryCustomerNameByFuzzyName.do")
    @ResponseBody
    public Object queryCustomerNameByFuzzyName(String customerName) {
        List<String> customerNames = customerService.queryCustomerNameByFuzzyName(customerName);
        return customerNames;
    }

    @RequestMapping("/workbench/transaction/saveCreateTransaction.do")
    @ResponseBody
    public Object saveCreateTransaction(Tran tran, HttpSession session) {
         User user = (User) session.getAttribute(Constants.SESSION_USER);
        // ????????????
        tran.setId(UUIDUtils.getUUID());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));
        tran.setCreateBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.saveCreateTransaction(tran);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("???????????????????????????....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/toEditPage.do")
    public String toEditPage(String id, HttpServletRequest request) {
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        Tran tran = tranService.queryTransactionById(id);
        // ??????properties??????????????????????????????????????????
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(dicValueService.queryDicValueById(tran.getStage()).getValue());
        // ??????request??????
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("tran", tran);
        request.setAttribute("possibility", possibility);
        return "workbench/transaction/edit";
    }

    @RequestMapping("/workbench/transaction/saveEditTransaction.do")
    @ResponseBody
    public Object saveEditTransaction(Tran tran, HttpSession session) {
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        // ????????????
        tran.setEditTime(DateUtils.formatDateTime(new Date()));
        tran.setEditBy(user.getId());
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.saveEditTransaction(tran);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("???????????????????????????....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/deleteTranByIds.do")
    @ResponseBody
    public Object deleteTranByIds(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.deleteTranByIds(id); // ???????????????id???????????????????????????????????????????????????????????????
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) { // ?????????????????????????????????????????????
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("??????????????????????????????...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/toDetailPage.do")
    public String toDetailPage(String id, HttpServletRequest request) {
        // ??????service????????????????????????
        Tran tran = tranService.queryTranForDetailById(id);
        List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetailByTranId(id);
        List<TranHistory> historyList = tranHistoryService.queryTranHistoryForDetailByTranId(id);
        //??????tran?????????????????????????????????
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        // ???????????????????????????stageNo
        String stageOrderNo = dicValueService.queryDicValueById(tranService.queryTransactionById(id).getStage()).getOrderNo();
        //??????????????????request???
        request.setAttribute("tran",tran);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("historyList",historyList);
        request.setAttribute("possibility",possibility);
        request.setAttribute("stageOrderNo", stageOrderNo);
        // ??????service????????????????????????????????????
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        //????????????
        return "workbench/transaction/detail";
    }

}
