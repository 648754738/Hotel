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
import com.hotel.app.domain.CheckInInfo;
import com.hotel.app.service.ICheckInInfoService;
import com.hotel.app.common.utils.poi.ExcelUtil;
import com.hotel.app.common.core.page.TableDataInfo;

/**
 * 入住信息Controller
 * 
 * @author ruoyi
 * @date 2023-08-01
 */
@RestController
@RequestMapping("/checkin/info")
public class CheckInInfoController extends BaseController {
    @Autowired
    private ICheckInInfoService checkInInfoService;

    /**
     * 查询入住信息列表
     */
    @PreAuthorize("@ss.hasPermi('checkin:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(CheckInInfo checkInInfo) {
        startPage();
        List<CheckInInfo> list = checkInInfoService.selectCheckInInfoList(checkInInfo);
        return getDataTable(list);
    }

    /**
     * 导出入住信息列表
     */
    @PreAuthorize("@ss.hasPermi('checkin:info:export')")
    @Log(title = "入住信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckInInfo checkInInfo) {
        List<CheckInInfo> list = checkInInfoService.selectCheckInInfoList(checkInInfo);
        ExcelUtil<CheckInInfo> util = new ExcelUtil<CheckInInfo>(CheckInInfo.class);
        util.exportExcel(response, list, "入住信息数据");
    }

    /**
     * 获取入住信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('checkin:info:query')")
    @GetMapping(value = "/{checkInId}")
    public AjaxResult getInfo(@PathVariable("checkInId") Long checkInId) {
        return AjaxResult.success(checkInInfoService.selectCheckInInfoById(checkInId));
    }

    /**
     * 新增入住信息
     */
    @PreAuthorize("@ss.hasPermi('checkin:info:add')")
    @Log(title = "入住信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CheckInInfo checkInInfo) {
        return toAjax(checkInInfoService.insertCheckInInfo(checkInInfo));
    }

    /**
     * 修改入住信息
     */
    @PreAuthorize("@ss.hasPermi('checkin:info:edit')")
    @Log(title = "入住信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CheckInInfo checkInInfo) {
        return toAjax(checkInInfoService.updateCheckInInfo(checkInInfo));
    }

    /**
     * 删除入住信息
     */
    @PreAuthorize("@ss.hasPermi('checkin:info:remove')")
    @Log(title = "入住信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{checkInIds}")
    public AjaxResult remove(@PathVariable Long[] checkInIds) {
        return toAjax(checkInInfoService.deleteCheckInInfoByIds(checkInIds));
    }
}