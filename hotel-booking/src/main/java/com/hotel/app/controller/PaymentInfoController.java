package com.hotel.app.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.hotel.app.common.annotation.Log;
import com.hotel.app.common.core.controller.BaseController;
import com.hotel.app.common.core.domain.AjaxResult;
import com.hotel.app.common.enums.BusinessType;
import com.hotel.app.domain.PaymentInfo;
import com.hotel.app.service.IPaymentInfoService;
import com.hotel.app.common.utils.poi.ExcelUtil;
import com.hotel.app.common.core.page.TableDataInfo;

/**
 * 支付信息Controller
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@RestController
@RequestMapping("/payment/info")
public class PaymentInfoController extends BaseController {
    @Autowired
    private IPaymentInfoService paymentInfoService;

    /**
     * 查询支付信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentInfo paymentInfo) {
        startPage();
        List<PaymentInfo> list = paymentInfoService.selectPaymentInfoList(paymentInfo);
        return getDataTable(list);
    }

    /**
     * 导出支付信息列表
     */
    @PreAuthorize("@ss.hasPermi('payment:info:export')")
    @Log(title = "支付信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentInfo paymentInfo) {
        List<PaymentInfo> list = paymentInfoService.selectPaymentInfoList(paymentInfo);
        ExcelUtil<PaymentInfo> util = new ExcelUtil<PaymentInfo>(PaymentInfo.class);
        util.exportExcel(response, list, "支付信息数据");
    }

    /**
     * 获取支付信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:info:query')")
    @GetMapping(value = "/{paymentId}")
    public AjaxResult getInfo(@PathVariable("paymentId") Long paymentId) {
        return AjaxResult.success(paymentInfoService.selectPaymentInfoById(paymentId));
    }

    /**
     * 新增支付信息
     */
    @PreAuthorize("@ss.hasPermi('payment:info:add')")
    @Log(title = "支付信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentInfo paymentInfo) {
        return toAjax(paymentInfoService.insertPaymentInfo(paymentInfo));
    }

    /**
     * 修改支付信息
     */
    @PreAuthorize("@ss.hasPermi('payment:info:edit')")
    @Log(title = "支付信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentInfo paymentInfo) {
        return toAjax(paymentInfoService.updatePaymentInfo(paymentInfo));
    }

    /**
     * 删除支付信息
     */
    @PreAuthorize("@ss.hasPermi('payment:info:remove')")
    @Log(title = "支付信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{paymentIds}")
    public AjaxResult remove(@PathVariable Long[] paymentIds) {
        return toAjax(paymentInfoService.deletePaymentInfoByIds(paymentIds));
    }
}