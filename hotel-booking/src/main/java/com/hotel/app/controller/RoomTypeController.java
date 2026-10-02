package com.hotel.app.controller;

import com.hotel.app.common.annotation.Log;
import com.hotel.app.common.core.controller.BaseController;
import com.hotel.app.common.core.domain.AjaxResult;
import com.hotel.app.common.core.page.TableDataInfo;
import com.hotel.app.common.enums.BusinessType;
import com.hotel.app.common.utils.poi.ExcelUtil;
import com.hotel.app.domain.RoomType;
import com.hotel.app.service.IRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客房类型Controller
 *
 * @author ruoyi
 * @date 2023-08-01
 */
@RestController
@RequestMapping("/type/type")
public class RoomTypeController extends BaseController {
    @Autowired
    private IRoomTypeService roomTypeService;

    /**
     * 查询客房类型列表
     */
    @PreAuthorize("@ss.hasPermi('type:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(RoomType roomType) {
        startPage();
        List<RoomType> list = roomTypeService.selectRoomTypeList(roomType);
        return getDataTable(list);
    }

    /**
     * 导出客房类型列表
     */
    @PreAuthorize("@ss.hasPermi('type:type:export')")
    @Log(title = "客房类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoomType roomType) {
        List<RoomType> list = roomTypeService.selectRoomTypeList(roomType);
        ExcelUtil<RoomType> util = new ExcelUtil<RoomType>(RoomType.class);
        util.exportExcel(response, list, "客房类型数据");
    }

    /**
     * 获取客房类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('type:type:query')")
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable("typeId") Long typeId) {
        return AjaxResult.success(roomTypeService.selectRoomTypeById(typeId));
    }

    /**
     * 新增客房类型
     */
    @PreAuthorize("@ss.hasPermi('type:type:add')")
    @Log(title = "客房类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RoomType roomType) {
        return toAjax(roomTypeService.insertRoomType(roomType));
    }

    /**
     * 修改客房类型
     */
    @PreAuthorize("@ss.hasPermi('type:type:edit')")
    @Log(title = "客房类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RoomType roomType) {
        try {
            return toAjax(roomTypeService.updateRoomType(roomType));
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除客房类型
     */
    @PreAuthorize("@ss.hasPermi('type:type:remove')")
    @Log(title = "客房类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{typeIds}")
    public AjaxResult remove(@PathVariable Long[] typeIds) {
        try {
            return toAjax(roomTypeService.deleteRoomTypeByIds(typeIds));
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}