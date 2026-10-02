<template>
  <div class="app-container">
    <div class="header-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" class="query-form">
        <el-form-item label="真实姓名" prop="realName">
          <el-input
            v-model="queryParams.realName"
            placeholder="请输入真实姓名"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input
            v-model="queryParams.idCard"
            placeholder="请输入身份证号"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input
            v-model="queryParams.phone"
            placeholder="请输入联系电话"
            clearable
            @keyup.enter.native="handleQuery"
          />
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
            v-hasPermi="['customerInfo:customerInfo:add']"
          >新增客户信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['customerInfo:customerInfo:edit']"
          >编辑客户信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['customerInfo:customerInfo:remove']"
          >删除客户信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['customerInfo:customerInfo:export']"
          >导出数据</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="info"
            plain
            icon="el-icon-upload"
            size="mini"
            @click="handleImport"
            v-hasPermi="['customerInfo:customerInfo:import']"
          >导入数据</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
    </div>

    <div class="table-container">
      <el-table v-loading="loading" :data="customerInfoList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="真实姓名" align="center" prop="realName" />
        <el-table-column label="身份证号" align="center" prop="idCard" />
        <el-table-column label="性别" align="center" prop="gender">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.gender"/>
          </template>
        </el-table-column>
        <el-table-column label="联系电话" align="center" prop="phone" />
        <el-table-column label="邮箱" align="center" prop="email" />
        <el-table-column label="创建人" align="center" prop="createBy" />
        <el-table-column label="创建时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-link
              :underline="false"
              type="primary"
              icon="el-icon-view"
              @click="handleDetail(scope.row)"
              class="table-link table-link-detail"
            >详情</el-link>
            <el-link
              :underline="false"
              type="success"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['customerInfo:customerInfo:edit']"
              class="table-link table-link-edit"
            >编辑</el-link>
            <el-link
              :underline="false"
              type="danger"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['customerInfo:customerInfo:remove']"
              class="table-link table-link-delete"
            >删除</el-link>
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

    <!-- 添加或修改客户信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" placeholder="请输入身份证号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择性别" style="width: 100%;">
                <el-option
                  v-for="dict in dict.type.sys_user_sex"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker clearable
                              v-model="form.birthday"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择出生日期"
                              style="width: 100%;">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="紧急联系人" prop="emergencyContact">
              <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="紧急电话" prop="emergencyPhone">
              <el-input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="头像" prop="avatar">
          <image-upload v-model="form.avatar" :limit="1" :file-size="5" :file-type="['png', 'jpg', 'jpeg']" />
        </el-form-item>

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
    <el-dialog title="客户信息详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <div class="detail-container">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span class="card-title">基本信息</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="真实姓名">{{ detailData.realName }}</el-descriptions-item>
                <el-descriptions-item label="身份证号">{{ detailData.idCard }}</el-descriptions-item>
                <el-descriptions-item label="性别">
                  <dict-tag :options="dict.type.sys_user_sex" :value="detailData.gender"/>
                </el-descriptions-item>
                <el-descriptions-item label="出生日期">{{ parseTime(detailData.birthday) }}</el-descriptions-item>
                <el-descriptions-item label="联系电话">{{ detailData.phone }}</el-descriptions-item>
                <el-descriptions-item label="邮箱">{{ detailData.email }}</el-descriptions-item>
                <el-descriptions-item label="地址" :span="2">{{ detailData.address }}</el-descriptions-item>
                <el-descriptions-item label="紧急联系人">{{ detailData.emergencyContact }}</el-descriptions-item>
                <el-descriptions-item label="紧急电话">{{ detailData.emergencyPhone }}</el-descriptions-item>
                <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
                <el-descriptions-item label="修改人">{{ detailData.updateBy }}</el-descriptions-item>
                <el-descriptions-item label="修改时间">{{ parseTime(detailData.updateTime) }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
              </el-descriptions>
            </el-card>

            <el-card class="box-card" style="margin-top: 20px;">
              <div slot="header" class="clearfix">
                <span class="card-title">客户头像</span>
              </div>
              <div v-if="detailData.avatar" class="image-preview-container">
                <el-image
                  :src="getFullImageUrl(detailData.avatar)"
                  :preview-src-list="[getFullImageUrl(detailData.avatar)]"
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
                <p>暂无头像</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 客户信息导入对话框 -->
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
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的客户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
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
import { listCustomerInfo, getCustomerInfo, delCustomerInfo, addCustomerInfo, updateCustomerInfo } from "@/api/customerInfo/customerInfo"
import ImageUpload from "@/components/ImageUpload"
import { getToken } from "@/utils/auth"

export default {
  name: "CustomerInfo",
  components: { ImageUpload },
  dicts: ['sys_user_sex'],
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
      // 客户信息表格数据
      customerInfoList: [],
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
        realName: null,
        idCard: null,
        phone: null
      },
      // 表单参数
      form: {},
      // 详情数据
      detailData: {},
      // 表单校验
      rules: {
        realName: [
          { required: true, message: "真实姓名不能为空", trigger: "blur" }
        ],
        idCard: [
          { required: true, message: "身份证号不能为空", trigger: "blur" }
        ],
        gender: [
          { required: true, message: "性别不能为空", trigger: "change" }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
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
        url: process.env.VUE_APP_BASE_API + "/customerInfo/customerInfo/importData"
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询客户信息列表 */
    getList() {
      this.loading = true
      listCustomerInfo(this.queryParams).then(response => {
        this.customerInfoList = response.rows
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
        customerId: null,
        realName: null,
        avatar: null,
        idCard: null,
        gender: null,
        birthday: null,
        address: null,
        phone: null,
        email: null,
        emergencyContact: null,
        emergencyPhone: null,
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
      this.ids = selection.map(item => item.customerId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加客户信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const customerId = row.customerId || this.ids
      getCustomerInfo(customerId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改客户信息"
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
          if (this.form.customerId != null) {
            updateCustomerInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCustomerInfo(this.form).then(response => {
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
      const customerIds = row.customerId || this.ids
      this.$modal.confirm('是否确认删除客户信息编号为"' + customerIds + '"的数据项？').then(function() {
        return delCustomerInfo(customerIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('customerInfo/customerInfo/export', {
        ...this.queryParams
      }, `customerInfo_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "客户信息导入"
      this.upload.open = true
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('customerInfo/customerInfo/importTemplate', {
      }, `customer_info_template.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
      this.getList()
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit()
    },
    /** 获取完整图片URL */
    getFullImageUrl(url) {
      if (!url) return ''
      if (url.startsWith('http')) {
        return url
      }
      return process.env.VUE_APP_BASE_API + url
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

/* 表格操作链接样式 */
.table-link {
  margin-right: 10px;
  font-size: 12px;
  transition: all 0.3s;
  cursor: pointer;
}

.table-link-detail {
  color: #409eff;
}

.table-link-detail:hover {
  color: #66b1ff;
  text-decoration: underline;
}

.table-link-edit {
  color: #67c23a;
}

.table-link-edit:hover {
  color: #85ce61;
  text-decoration: underline;
}

.table-link-delete {
  color: #f56c6c;
}

.table-link-delete:hover {
  color: #f78989;
  text-decoration: underline;
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
  flex-direction: column;
  gap: 15px;
}

.image-preview {
  width: 200px;
  height: 200px;
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
</style>