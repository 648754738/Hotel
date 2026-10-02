package com.hotel.app.controller;

import com.hotel.app.common.annotation.Log;
import com.hotel.app.common.core.controller.BaseController;
import com.hotel.app.common.core.domain.AjaxResult;
import com.hotel.app.common.core.page.TableDataInfo;
import com.hotel.app.common.enums.BusinessType;
import com.hotel.app.common.utils.poi.ExcelUtil;
import com.hotel.app.domain.CustomerInfo;
import com.hotel.app.service.ICustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户信息Controller
 * 
 * @author ruoyi
 * @date 2025-09-02
 */
@RestController
@RequestMapping("/customerInfo/customerInfo")
public class CustomerInfoController extends BaseController
{
    @Autowired
    private ICustomerInfoService customerInfoService;

    /**
     * 查询客户信息列表
     */
    @PreAuthorize("@ss.hasPermi('customerInfo:customerInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerInfo customerInfo)
    {
        startPage();
        List<CustomerInfo> list = customerInfoService.selectCustomerInfoList(customerInfo);
        return getDataTable(list);
    }

    /**
     * 导出客户信息列表
     */
    @PreAuthorize("@ss.hasPermi('customerInfo:customerInfo:export')")
    @Log(title = "客户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CustomerInfo customerInfo)
    {
        List<CustomerInfo> list = customerInfoService.selectCustomerInfoList(customerInfo);
        ExcelUtil<CustomerInfo> util = new ExcelUtil<CustomerInfo>(CustomerInfo.class);
        util.exportExcel(response, list, "客户信息数据");
    }

    /**
     * 下载导入客户信息模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<CustomerInfo> util = new ExcelUtil<CustomerInfo>(CustomerInfo.class);
        util.importTemplateExcel(response, "客户信息数据");
    }

    /**
     * 导入客户信息数据
     */
    @PreAuthorize("@ss.hasPermi('customerInfo:customerInfo:import')")
    @Log(title = "客户信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<CustomerInfo> util = new ExcelUtil<CustomerInfo>(CustomerInfo.class);
        List<CustomerInfo> customerInfoList = util.importExcel(file.getInputStream());
        String operName = getLoginUser().getUsername();
        String message = "导入成功";
        int successCount = 0;
        int failureCount = 0;
        StringBuilder failureMsg = new StringBuilder();
        
        for (CustomerInfo customerInfo : customerInfoList) {
            try {
                // 检查必填字段
                if (customerInfo.getRealName() == null || customerInfo.getRealName().isEmpty()) {
                    failureMsg.append("真实姓名不能为空<br/>");
                    failureCount++;
                    continue;
                }
                
                if (customerInfo.getIdCard() == null || customerInfo.getIdCard().isEmpty()) {
                    failureMsg.append("身份证号不能为空<br/>");
                    failureCount++;
                    continue;
                }
                
                if (customerInfo.getPhone() == null || customerInfo.getPhone().isEmpty()) {
                    failureMsg.append("联系电话不能为空<br/>");
                    failureCount++;
                    continue;
                }
                
                // 设置默认值
                if (customerInfo.getGender() == null || customerInfo.getGender().isEmpty()) {
                    customerInfo.setGender("0"); // 默认男
                }
                
                // 新增或更新操作
                if (updateSupport) {
                    // 根据身份证号查询客户信息
                    CustomerInfo existingCustomer = customerInfoService.selectCustomerInfoByIdCard(customerInfo.getIdCard());
                    if (existingCustomer != null) {
                        // 如果存在则更新
                        customerInfo.setCustomerId(existingCustomer.getCustomerId());
                        customerInfo.setUpdateBy(operName);
                        customerInfoService.updateCustomerInfo(customerInfo);
                    } else {
                        // 如果不存在则新增
                        customerInfo.setCreateBy(operName);
                        customerInfoService.insertCustomerInfo(customerInfo);
                    }
                } else {
                    // 检查身份证号是否已存在
                    CustomerInfo existingCustomer = customerInfoService.selectCustomerInfoByIdCard(customerInfo.getIdCard());
                    if (existingCustomer != null) {
                        failureMsg.append("身份证号 " + customerInfo.getIdCard() + " 已存在<br/>");
                        failureCount++;
                        continue;
                    }
                    customerInfo.setCreateBy(operName);
                    customerInfoService.insertCustomerInfo(customerInfo);
                }
                successCount++;
            } catch (Exception e) {
                failureMsg.append("身份证号 " + customerInfo.getIdCard() + " 导入失败：" + e.getMessage() + "<br/>");
                failureCount++;
            }
        }
        
        if (failureCount > 0) {
            message = "导入完成，成功 " + successCount + " 条，失败 " + failureCount + " 条，失败信息如下：<br/>" + failureMsg.toString();
        } else {
            message = "导入成功，共 " + successCount + " 条数据";
        }
        
        return success(message);
    }

    /**
     * 获取客户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('customerInfo:customerInfo:query')")
    @GetMapping(value = "/{customerId}")
    public AjaxResult getInfo(@PathVariable("customerId") Long customerId)
    {
        return success(customerInfoService.selectCustomerInfoById(customerId));
    }

    /**
     * 新增客户信息
     */
    @PreAuthorize("@ss.hasPermi('customerInfo:customerInfo:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CustomerInfo customerInfo)
    {
        return toAjax(customerInfoService.insertCustomerInfo(customerInfo));
    }

    /**
     * 修改客户信息
     */
    @PreAuthorize("@ss.hasPermi('customerInfo:customerInfo:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerInfo customerInfo)
    {
        return toAjax(customerInfoService.updateCustomerInfo(customerInfo));
    }

    /**
     * 删除客户信息
     */
    @PreAuthorize("@ss.hasPermi('customerInfo:customerInfo:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{customerIds}")
    public AjaxResult remove(@PathVariable Long[] customerIds)
    {
        return toAjax(customerInfoService.deleteCustomerInfoByIds(customerIds));
    }
}