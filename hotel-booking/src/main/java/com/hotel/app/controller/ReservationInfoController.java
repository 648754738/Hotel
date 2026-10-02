package com.hotel.app.controller;

import com.hotel.app.common.annotation.Log;
import com.hotel.app.common.core.controller.BaseController;
import com.hotel.app.common.core.domain.AjaxResult;
import com.hotel.app.common.core.page.TableDataInfo;
import com.hotel.app.common.enums.BusinessType;
import com.hotel.app.common.utils.poi.ExcelUtil;
import com.hotel.app.domain.ReservationInfo;
import com.hotel.app.domain.dto.ReservationInfoDTO;
import com.hotel.app.service.IReservationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 预定信息Controller
 *
 * @author ruoyi
 * @date 2025-09-02
 */
@RestController
@RequestMapping("/reservationInfo/reservationInfo")
public class ReservationInfoController extends BaseController
{
    @Autowired
    private IReservationInfoService reservationInfoService;

    /**
     * 查询预定信息列表
     */
    @PreAuthorize("@ss.hasPermi('reservationInfo:reservationInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReservationInfo reservationInfo)
    {
        startPage();
        List<ReservationInfoDTO> list = reservationInfoService.selectReservationInfoDTOList(reservationInfo);
        return getDataTable(list);
    }

    /**
     * 导出预定信息列表
     */
    @PreAuthorize("@ss.hasPermi('reservationInfo:reservationInfo:export')")
    @Log(title = "预定信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ReservationInfo reservationInfo)
    {
        List<ReservationInfoDTO> list = reservationInfoService.selectReservationInfoDTOList(reservationInfo);
        ExcelUtil<ReservationInfoDTO> util = new ExcelUtil<ReservationInfoDTO>(ReservationInfoDTO.class);
        util.exportExcel(response, list, "预定信息数据");
    }

    /**
     * 获取预定信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('reservationInfo:reservationInfo:query')")
    @GetMapping(value = "/{reservationId}")
    public AjaxResult getInfo(@PathVariable("reservationId") Long reservationId)
    {
        return success(reservationInfoService.selectReservationInfoById(reservationId));
    }

    /**
     * 新增预定信息
     */
    @PreAuthorize("@ss.hasPermi('reservationInfo:reservationInfo:add')")
    @Log(title = "预定信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReservationInfo reservationInfo)
    {
        return toAjax(reservationInfoService.insertReservationInfo(reservationInfo));
    }

    /**
     * 修改预定信息
     */
    @PreAuthorize("@ss.hasPermi('reservationInfo:reservationInfo:edit')")
    @Log(title = "预定信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReservationInfo reservationInfo)
    {
        return toAjax(reservationInfoService.updateReservationInfo(reservationInfo));
    }

    /**
     * 删除预定信息
     */
    @PreAuthorize("@ss.hasPermi('reservationInfo:reservationInfo:remove')")
    @Log(title = "预定信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reservationIds}")
    public AjaxResult remove(@PathVariable Long[] reservationIds)
    {
        return toAjax(reservationInfoService.deleteReservationInfoByIds(reservationIds));
    }
}