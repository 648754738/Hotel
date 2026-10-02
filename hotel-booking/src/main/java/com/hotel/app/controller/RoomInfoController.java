package com.hotel.app.controller;

import com.hotel.app.common.annotation.Log;
import com.hotel.app.common.core.controller.BaseController;
import com.hotel.app.common.core.domain.AjaxResult;
import com.hotel.app.common.core.page.TableDataInfo;
import com.hotel.app.common.enums.BusinessType;
import com.hotel.app.common.utils.poi.ExcelUtil;
import com.hotel.app.domain.RoomInfo;
import com.hotel.app.domain.dto.RoomInfoDTO;
import com.hotel.app.service.IRoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客房信息Controller
 *
 * @author ruoyi
 * @date 2025-09-02
 */
@RestController
@RequestMapping("/info/info")
public class RoomInfoController extends BaseController
{
    @Autowired
    private IRoomInfoService roomInfoService;

    /**
     * 查询客房信息列表
     */
    @PreAuthorize("@ss.hasPermi('info:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(RoomInfo roomInfo)
    {
        startPage();
        List<RoomInfo> list = roomInfoService.selectRoomInfoList(roomInfo);
        return getDataTable(list);
    }

    /**
     * 查询客房信息列表（包含房间类型信息）
     */
    @PreAuthorize("@ss.hasPermi('info:info:list')")
    @GetMapping("/listWithTypeInfo")
    public TableDataInfo listWithTypeInfo(RoomInfo roomInfo)
    {
        startPage();
        List<RoomInfoDTO> list = roomInfoService.selectRoomInfoDTOList(roomInfo);
        return getDataTable(list);
    }

    /**
     * 导出客房信息列表
     */
    @PreAuthorize("@ss.hasPermi('info:info:export')")
    @Log(title = "客房信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoomInfo roomInfo)
    {
        List<RoomInfo> list = roomInfoService.selectRoomInfoList(roomInfo);
        ExcelUtil<RoomInfo> util = new ExcelUtil<RoomInfo>(RoomInfo.class);
        util.exportExcel(response, list, "客房信息数据");
    }

    /**
     * 导出客房信息列表（包含房间类型信息）
     */
    @PreAuthorize("@ss.hasPermi('info:info:export')")
    @Log(title = "客房信息", businessType = BusinessType.EXPORT)
    @PostMapping("/exportWithTypeInfo")
    public void exportWithTypeInfo(HttpServletResponse response, RoomInfo roomInfo)
    {
        List<RoomInfoDTO> list = roomInfoService.selectRoomInfoDTOList(roomInfo);
        ExcelUtil<RoomInfoDTO> util = new ExcelUtil<RoomInfoDTO>(RoomInfoDTO.class);
        util.exportExcel(response, list, "客房信息数据（含房间类型）");
    }

    /**
     * 下载导入客房信息模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<RoomInfo> util = new ExcelUtil<RoomInfo>(RoomInfo.class);
        util.importTemplateExcel(response, "客房信息数据");
    }

    /**
     * 导入客房信息数据
     */
    @PreAuthorize("@ss.hasPermi('info:info:import')")
    @Log(title = "客房信息", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<RoomInfo> util = new ExcelUtil<RoomInfo>(RoomInfo.class);
        List<RoomInfo> roomInfoList = util.importExcel(file.getInputStream());
        String operName = getLoginUser().getUsername();
        String message = "导入成功";
        int successCount = 0;
        int failureCount = 0;
        StringBuilder failureMsg = new StringBuilder();
        
        for (RoomInfo roomInfo : roomInfoList) {
            try {
                // 检查必填字段
                if (roomInfo.getRoomNumber() == null || roomInfo.getRoomNumber().isEmpty()) {
                    failureMsg.append("房间号不能为空<br/>");
                    failureCount++;
                    continue;
                }
                
                if (roomInfo.getTypeId() == null) {
                    failureMsg.append("房间类型ID不能为空<br/>");
                    failureCount++;
                    continue;
                }
                
                if (roomInfo.getFloor() == null) {
                    failureMsg.append("楼层不能为空<br/>");
                    failureCount++;
                    continue;
                }
                
                // 设置默认状态为空闲
                if (roomInfo.getRoomStatus() == null || roomInfo.getRoomStatus().isEmpty()) {
                    roomInfo.setRoomStatus("0");
                }
                
                // 检查房间号是否已存在
                RoomInfo existingRoom = roomInfoService.selectRoomInfoByRoomNumber(roomInfo.getRoomNumber());
                if (existingRoom != null) {
                    if (updateSupport) {
                        // 更新操作
                        roomInfo.setRoomId(existingRoom.getRoomId());
                        roomInfo.setUpdateBy(operName);
                        roomInfoService.updateRoomInfo(roomInfo);
                        successCount++;
                    } else {
                        failureMsg.append("房间号 " + roomInfo.getRoomNumber() + " 已存在<br/>");
                        failureCount++;
                    }
                } else {
                    // 新增操作
                    roomInfo.setCreateBy(operName);
                    roomInfoService.insertRoomInfo(roomInfo);
                    successCount++;
                }
            } catch (Exception e) {
                failureMsg.append("房间号 " + roomInfo.getRoomNumber() + " 导入失败：" + e.getMessage() + "<br/>");
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
     * 获取客房信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('info:info:query')")
    @GetMapping(value = "/{roomId}")
    public AjaxResult getInfo(@PathVariable("roomId") Long roomId)
    {
        return success(roomInfoService.selectRoomInfoById(roomId));
    }

    /**
     * 新增客房信息
     */
    @PreAuthorize("@ss.hasPermi('info:info:add')")
    @Log(title = "客房信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RoomInfo roomInfo)
    {
        return toAjax(roomInfoService.insertRoomInfo(roomInfo));
    }

    /**
     * 修改客房信息
     */
    @PreAuthorize("@ss.hasPermi('info:info:edit')")
    @Log(title = "客房信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RoomInfo roomInfo)
    {
        try {
            return toAjax(roomInfoService.updateRoomInfo(roomInfo));
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除客房信息
     */
    @PreAuthorize("@ss.hasPermi('info:info:remove')")
    @Log(title = "客房信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roomIds}")
    public AjaxResult remove(@PathVariable Long[] roomIds)
    {
        try {
            return toAjax(roomInfoService.deleteRoomInfoByIds(roomIds));
        } catch (RuntimeException e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}