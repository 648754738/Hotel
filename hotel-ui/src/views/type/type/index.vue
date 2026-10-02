<template>
  <div class="app-container">
    <div class="header-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px" class="query-form">
        <el-form-item label="类型名称" prop="typeName">
          <el-input
            v-model="queryParams.typeName"
            placeholder="请输入类型名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option
              v-for="dict in dict.type.room_tyoe_status"
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
            v-hasPermi="['type:type:add']"
          >新增客房类型</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['type:type:edit']"
          >编辑客房类型</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['type:type:remove']"
          >删除客房类型</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['type:type:export']"
          >导出数据</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
    </div>

    <div class="table-container">
      <el-table v-loading="loading" :data="typeList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="类型名称" align="center" prop="typeName" >
          <template slot-scope="scope">
            <span class="type-name">{{ scope.row.typeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="床位数量" align="center" prop="bedCount"  />
        <el-table-column label="最大入住" align="center" prop="maxOccupancy"  />
        <el-table-column label="房间面积" align="center" prop="area" >
          <template slot-scope="scope">
            <span>{{ scope.row.area }}㎡</span>
          </template>
        </el-table-column>
        <el-table-column label="房间价格" align="center" prop="price" >
          <template slot-scope="scope">
            <span class="price-text">¥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" >
          <template slot-scope="scope">
            <dict-tag :options="dict.type.room_tyoe_status" :value="scope.row.status"/>
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
                v-hasPermi="['type:type:edit']"
                class="operation-btn edit-btn"
              >编辑</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['type:type:remove']"
                class="operation-btn delete-btn"
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

    <!-- 添加或修改客房类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="类型名称" prop="typeName">
              <el-input v-model="form.typeName" placeholder="请输入类型名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="床位数量" prop="bedCount">
              <el-input-number v-model="form.bedCount" :min="1" :max="10" label="床位数量"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最大入住人数" prop="maxOccupancy">
              <el-input-number v-model="form.maxOccupancy" :min="1" :max="20" label="最大入住人数"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间面积(㎡)" prop="area">
              <el-input-number v-model="form.area" :min="1" :max="500" label="房间面积"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="房间价格(元)" prop="price">
              <el-input-number v-model="form.price" :min="1" :max="10000" label="房间价格"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%;">
                <el-option
                  v-for="dict in dict.type.room_tyoe_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="类型描述" prop="typeDesc">
          <el-input v-model="form.typeDesc" type="textarea" placeholder="请输入类型描述" :rows="2" />
        </el-form-item>

        <el-form-item label="房间设施" prop="facility">
          <el-checkbox-group v-model="selectedFacilities" @change="handleFacilityChange">
            <el-checkbox v-for="facility in facilityOptions" :key="facility.value" :label="facility.value">
              {{ facility.label }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="房间图片" prop="imageUrls">
          <image-upload v-model="form.imageUrls" :limit="3" :file-size="5" :file-type="['png', 'jpg', 'jpeg']" />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" class="custom-submit-btn">确 定</el-button>
        <el-button @click="cancel" class="custom-cancel-btn">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="客房类型详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <div class="detail-container">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span class="card-title">基本信息</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="类型名称">
                  <span class="detail-type-name">{{ detailData.typeName }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="类型描述">{{ detailData.typeDesc }}</el-descriptions-item>
                <el-descriptions-item label="床位数量">{{ detailData.bedCount }}</el-descriptions-item>
                <el-descriptions-item label="最大入住人数">{{ detailData.maxOccupancy }}</el-descriptions-item>
                <el-descriptions-item label="房间面积">{{ detailData.area }}㎡</el-descriptions-item>
                <el-descriptions-item label="房间价格">
                  <span class="detail-price">¥{{ detailData.price }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="状态">
                  <dict-tag :options="dict.type.room_tyoe_status" :value="detailData.status"/>
                </el-descriptions-item>
                <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
                <el-descriptions-item label="修改人">{{ detailData.updateBy }}</el-descriptions-item>
                <el-descriptions-item label="修改时间">{{ parseTime(detailData.updateTime) }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
              </el-descriptions>
            </el-card>

            <el-card class="box-card" style="margin-top: 20px;">
              <div slot="header" class="clearfix">
                <span class="card-title">房间设施</span>
              </div>
              <div class="facility-container">
                <el-tag v-for="facility in parseFacilities(detailData.facility)" :key="facility" type="success" class="facility-tag">
                  {{ facility }}
                </el-tag>
                <div v-if="!detailData.facility || parseFacilities(detailData.facility).length === 0" class="no-facility">
                  暂无设施信息
                </div>
              </div>
            </el-card>

            <el-card class="box-card" style="margin-top: 20px;">
              <div slot="header" class="clearfix">
                <span class="card-title">房间图片</span>
              </div>
              <div v-if="detailData.imageUrls" class="image-preview-container">
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
              <div v-else class="no-image">
                <i class="el-icon-picture-outline" style="font-size: 48px; color: #C0C4CC;"></i>
                <p>暂无图片</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false" class="custom-close-btn">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listType, getType, delType, addType, updateType } from "@/api/type/type"
import ImageUpload from "@/components/ImageUpload"

export default {
  name: "Type",
  components: { ImageUpload },
  dicts: ['room_tyoe_status'],
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
      // 客房类型表格数据
      typeList: [],
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
        typeName: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 详情数据
      detailData: {},
      // 表单校验
      rules: {
        typeName: [
          { required: true, message: "类型名称不能为空", trigger: "blur" }
        ],
        typeDesc: [
          { required: true, message: "类型描述不能为空", trigger: "blur" }
        ],
        bedCount: [
          { required: true, message: "床位数量不能为空", trigger: "blur" }
        ],
        maxOccupancy: [
          { required: true, message: "最大入住人数不能为空", trigger: "blur" }
        ],
        area: [
          { required: true, message: "房间面积不能为空", trigger: "blur" }
        ],
        price: [
          { required: true, message: "房间价格不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      },
      // 房间设施选项
      facilityOptions: [
        { value: '免费WiFi', label: '免费WiFi' },
        { value: '空调', label: '空调' },
        { value: '电视', label: '电视' },
        { value: '独立卫浴', label: '独立卫浴' },
        { value: '浴缸', label: '浴缸' },
        { value: '迷你吧', label: '迷你吧' },
        { value: '办公区', label: '办公区' },
        { value: '客厅', label: '客厅' },
        { value: '餐厅', label: '餐厅' },
        { value: '桑拿', label: '桑拿' },
        { value: '厨房', label: '厨房' },
        { value: '按摩浴缸', label: '按摩浴缸' }
      ],
      // 选中的设施
      selectedFacilities: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询客房类型列表 */
    getList() {
      this.loading = true
      listType(this.queryParams).then(response => {
        this.typeList = response.rows
        this.total = response.total
        this.loading = false
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
        typeId: null,
        typeName: null,
        typeDesc: null,
        bedCount: null,
        maxOccupancy: null,
        area: null,
        price: null,
        facility: null,
        imageUrl: null,
        imageUrls: null,
        status: null,
        delFlag: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.selectedFacilities = []
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
      this.ids = selection.map(item => item.typeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加客房类型"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const typeId = row.typeId || this.ids
      getType(typeId).then(response => {
        this.form = response.data
        // 解析设施数据
        if (this.form.facility) {
          this.selectedFacilities = this.form.facility.split(',').map(item => item.trim()).filter(item => item)
        }
        this.open = true
        this.title = "修改客房类型"
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
          // 合并选中的设施
          this.form.facility = this.selectedFacilities.join(',')
          if (this.form.typeId != null) {
            updateType(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addType(this.form).then(response => {
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
      const typeIds = row.typeId || this.ids
      this.$modal.confirm('是否确认删除客房类型编号为"' + typeIds + '"的数据项？').then(function() {
        return delType(typeIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('type/type/export', {
        ...this.queryParams
      }, `type_${new Date().getTime()}.xlsx`)
    },
    /** 处理设施选择变化 */
    handleFacilityChange(value) {
      this.selectedFacilities = value
    },
    /** 解析设施数据用于详情显示 */
    parseFacilities(facilityStr) {
      if (!facilityStr) return []
      return facilityStr.split(',').map(item => item.trim()).filter(item => item)
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

.type-name {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
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

/* 房间设施样式 */
.facility-container {
  padding: 15px 0;
}

.facility-tag {
  margin: 0 5px 10px 0;
  font-size: 14px;
  padding: 8px 12px;
}

.no-facility {
  color: #909399;
  font-style: italic;
  text-align: center;
  padding: 20px 0;
}
