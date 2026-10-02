<template>
  <div class="app-container">
    <div class="header-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" class="query-form">
        <el-form-item label="房间号" prop="roomNumber">
          <el-input
            v-model="queryParams.roomNumber"
            placeholder="请输入房间号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="房间状态" prop="roomStatus">
          <el-select v-model="queryParams.roomStatus" placeholder="请选择房间状态" clearable>
            <el-option
              v-for="dict in dict.type.hotel_room_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8 toolbar-row">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['info:info:add']"
          >新增客房信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['info:info:edit']"
          >编辑客房信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['info:info:remove']"
          >删除客房信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['info:info:export']"
          >导出数据</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExportWithTypeInfo"
            v-hasPermi="['info:info:export']"
          >导出详细数据</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="info"
            plain
            icon="el-icon-upload"
            size="mini"
            @click="handleImport"
            v-hasPermi="['info:info:import']"
          >导入数据</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
    </div>

    <div class="table-container">
      <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="房间号" align="center" prop="roomNumber" />
        <el-table-column label="房间类型" align="center" prop="typeName" />
        <el-table-column label="楼层" align="center" prop="floor" />
        <el-table-column label="房间状态" align="center" prop="roomStatus">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.hotel_room_status" :value="scope.row.roomStatus"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <div class="operation-buttons">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-view"
                @click="handleDetail(scope.row)"
                class="operation-btn detail-btn"
              >详情</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['info:info:edit']"
                class="operation-btn edit-btn"
                :disabled="scope.row.roomStatus !== '0'"
              >编辑</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['info:info:remove']"
                class="operation-btn delete-btn"
                :disabled="scope.row.roomStatus !== '0'"
              >删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改客房信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房间号" prop="roomNumber">
              <el-input v-model="form.roomNumber" placeholder="请输入房间号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间类型" prop="typeId">
              <el-select v-model="form.typeId" placeholder="请选择房间类型" style="width: 100%;">
                <el-option
                  v-for="item in roomTypeList"
                  :key="item.typeId"
                  :label="item.typeName + ' (最多' + item.maxOccupancy + '人)'"
                  :value="item.typeId"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="楼层" prop="floor">
              <el-input-number v-model="form.floor" :min="1" :max="50" label="楼层"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间状态" prop="roomStatus">
              <el-select v-model="form.roomStatus" placeholder="请选择房间状态" style="width: 100%;">
                <el-option
                  v-for="dict in dict.type.hotel_room_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="客房信息详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <div class="detail-container">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span class="card-title">基本信息</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="房间号">{{ detailData.roomNumber }}</el-descriptions-item>
                <el-descriptions-item label="房间类型">{{ detailData.typeName }}</el-descriptions-item>
                <el-descriptions-item label="楼层">{{ detailData.floor }}层</el-descriptions-item>
                <el-descriptions-item label="房间状态">
                  <dict-tag :options="dict.type.hotel_room_status" :value="detailData.roomStatus"/>
                </el-descriptions-item>
                <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
                <el-descriptions-item label="修改人">{{ detailData.updateBy }}</el-descriptions-item>
                <el-descriptions-item label="修改时间">{{ parseTime(detailData.updateTime) }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
              </el-descriptions>
            </el-card>

            <el-card class="box-card" style="margin-top: 20px;" v-if="detailData.typeName">
              <div slot="header" class="clearfix">
                <span class="card-title">房间类型信息</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="类型名称">{{ detailData.typeName }}</el-descriptions-item>
                <el-descriptions-item label="类型描述">{{ detailData.typeDesc }}</el-descriptions-item>
                <el-descriptions-item label="床位数量">{{ detailData.bedCount }}</el-descriptions-item>
                <el-descriptions-item label="最大入住人数">{{ detailData.maxOccupancy }}</el-descriptions-item>
                <el-descriptions-item label="房间面积">{{ detailData.area }}㎡</el-descriptions-item>
                <el-descriptions-item label="房间价格">
                  <span class="detail-price">¥{{ detailData.price }}</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>

            <el-card class="box-card" style="margin-top: 20px;" v-if="detailData.imageUrls">
              <div slot="header" class="clearfix">
                <span class="card-title">房间图片</span>
              </div>
              <div class="image-preview-container">
                <el-image
                  v-for="(url, index) in detailData.imageUrls.split(',')"
                  :key="index"
                  :src="getFullImageUrl(url)"
                  :preview-src-list="getPreviewList(detailData.imageUrls)"
                  class="image-preview"
                  fit="cover"
                  lazy
                >
                  <div slot="placeholder" class="image-slot">
                    <i class="el-icon-loading"></i>
                  </div>
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
              <div v-if="!detailData.imageUrls || detailData.imageUrls.split(',').length === 0" class="no-image">
                <i class="el-icon-picture-outline" style="font-size: 48px; color: #C0C4CC;"></i>
                <p>暂无图片</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的客房信息数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="handleTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listInfoWithTypeInfo, getInfo, delInfo, addInfo, updateInfo, importTemplate, importData, exportWithTypeInfo } from "@/api/info/info"
import { listType } from "@/api/type/type"
import { getToken } from "@/utils/auth"
import { saveAs } from 'file-saver'

export default {
  name: "Info",
  dicts: ['hotel_room_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 客房信息表格数据
      infoList: [],
      // 房间类型列表
      roomTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      detailOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomNumber: null,
        typeId: null,
        roomStatus: null,
      },
      // 表单参数
      form: {},
      // 详情数据
      detailData: {},
      // 表单校验
      rules: {
        roomNumber: [
          { required: true, message: "房间号不能为空", trigger: "blur" }
        ],
        typeId: [
          { required: true, message: "房间类型不能为空", trigger: "change" }
        ],
        floor: [
          { required: true, message: "楼层不能为空", trigger: "blur" }
        ],
        roomStatus: [
          { required: true, message: "房间状态不能为空", trigger: "change" }
        ]
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/info/info/importData"
      }
    }
  },
  created() {
    this.getList()
    this.getRoomTypeList()
  },
  methods: {
    /** 查询客房信息列表 */
    getList() {
      this.loading = true
      listInfoWithTypeInfo(this.queryParams).then(response => {
        this.infoList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 查询房间类型列表 */
    getRoomTypeList() {
      listType({ status: "0" }).then(response => {
        this.roomTypeList = response.rows
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        roomId: null,
        roomNumber: null,
        typeId: null,
        floor: null,
        roomStatus: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roomId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加客房信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const roomId = row.roomId || this.ids
      getInfo(roomId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改客房信息"
      })
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.detailData = { ...row }
      this.detailOpen = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roomId != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const roomIds = row.roomId || this.ids
      this.$modal.confirm('是否确认删除客房信息编号为"' + roomIds + '"的数据项？').then(function() {
        return delInfo(roomIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('info/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    },
    /** 导出详细数据按钮操作 */
    handleExportWithTypeInfo() {
      this.$modal.confirm('是否确认导出包含房间类型信息的客房数据？').then(() => {
        return exportWithTypeInfo({ ...this.queryParams })
      }).then(response => {
        this.download(response.msg)
      }).catch(() => {})
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "客房信息导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    handleTemplate() {
      importTemplate().then(response => {
        const blob = new Blob([response]);
        saveAs(blob, "客房信息导入模板.xlsx");
      });
    },
    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    /** 提交上传文件 */
    submitFileForm() {
      this.$refs.upload.submit();
    },
    /** 获取完整图片URL */
    getFullImageUrl(url) {
      if (!url) return ''
      if (url.startsWith('http')) {
        return url
      }
      return process.env.VUE_APP_BASE_API + url
    },
    /** 获取预览图片列表 */
    getPreviewList(imageUrls) {
      if (!imageUrls) return []
      return imageUrls.split(',').map(url => this.getFullImageUrl(url))
    }
  }
}
</script>

<style scoped>
.app-container {
  background: #f5f7fa;
  padding: 20px;
}

.header-container {
  background: #ffffff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.query-form {
  margin-bottom: 15px;
}

.toolbar-row {
  margin-top: 15px;
}

.table-container {
  background: #ffffff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.detail-price {
  font-weight: 700;
  font-size: 18px;
  color: #e6a23c;
}

/* 表格操作按钮样式 */
.operation-buttons {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
}

.operation-btn {
  padding: 4px 8px;
  margin: 0 2px;
  font-size: 12px;
}

.detail-btn {
  color: #409eff;
}

.edit-btn {
  color: #67c23a;
}

.delete-btn {
  color: #f56c6c;
}

/* 详情页面样式 */
.detail-container {
  padding: 20px;
}

.box-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.box-card:not(:first-child) {
  margin-top: 20px;
}

.card-title {
  font-weight: 600;
  font-size: 16px;
  color: #303133;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

/* 图片预览样式 */
.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.image-preview {
  width: calc(33.333% - 10px);
  height: 150px;
  border-radius: 4px;
  cursor: pointer;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

.no-image {
  text-align: center;
  padding: 30px 0;
  color: #909399;
}

@media (max-width: 768px) {
  .image-preview {
    width: calc(50% - 8px);
  }
}

@media (max-width: 480px) {
  .image-preview {
    width: 100%;
  }
}
</style>