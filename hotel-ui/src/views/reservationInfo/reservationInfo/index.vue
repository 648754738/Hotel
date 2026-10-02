<template>
  <div class="app-container">
    <div class="header-container">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" class="query-form">
        <el-form-item label="客户名称" prop="customerId">
          <el-select v-model="queryParams.customerId" placeholder="请选择客户" clearable filterable>
            <el-option
              v-for="customer in customerList"
              :key="customer.customerId"
              :label="customer.realName + ' (' + (customer.gender === '0' ? '男' : '女') + ')'"
              :value="customer.customerId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="房间号" prop="roomId">
          <el-select v-model="queryParams.roomId" placeholder="请选择房间" clearable filterable>
            <el-option
              v-for="room in roomList"
              :key="room.roomId"
              :label="room.roomNumber"
              :value="room.roomId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预定状态" prop="reservationStatus">
          <el-select v-model="queryParams.reservationStatus" placeholder="请选择预定状态" clearable>
            <el-option
              v-for="dict in dict.type.hotel_reservation_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态" prop="paymentStatus">
          <el-select v-model="queryParams.paymentStatus" placeholder="请选择支付状态" clearable>
            <el-option
              v-for="dict in dict.type.hotel_payment_status"
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
            v-hasPermi="['reservationInfo:reservationInfo:add']"
          >新增预定信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['reservationInfo:reservationInfo:edit']"
          >编辑预定信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['reservationInfo:reservationInfo:remove']"
          >删除预定信息</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['reservationInfo:reservationInfo:export']"
          >导出数据</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
    </div>

    <div class="table-container">
      <el-table v-loading="loading" :data="reservationInfoList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="预定编号" align="center" prop="reservationCode" />
        <el-table-item label="客户名称" align="center" prop="customerName" />
        <el-table-column label="房间号" align="center" prop="roomNumber" />
        <el-table-column label="房间类型" align="center" prop="roomTypeName" />
        <el-table-column label="入住日期" align="center" prop="checkInDate" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.checkInDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="离店日期" align="center" prop="checkOutDate" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.checkOutDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="入住晚数" align="center" prop="nights" />
        <el-table-column label="总金额" align="center" prop="totalAmount" />
        <el-table-column label="预定状态" align="center" prop="reservationStatus">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.hotel_reservation_status" :value="scope.row.reservationStatus"/>
          </template>
        </el-table-column>
        <el-table-column label="支付状态" align="center" prop="paymentStatus">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.hotel_payment_status" :value="scope.row.paymentStatus"/>
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
              v-hasPermi="['reservationInfo:reservationInfo:edit']"
              class="table-link table-link-edit"
            >编辑</el-link>
            <el-link
              :underline="false"
              type="danger"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['reservationInfo:reservationInfo:remove']"
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

    <!-- 添加或修改预定信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户" prop="customerId">
              <el-select v-model="form.customerId" placeholder="请选择客户" style="width: 100%;" filterable>
                <el-option
                  v-for="customer in customerList"
                  :key="customer.customerId"
                  :label="customer.realName + ' (' + (customer.gender === '0' ? '男' : '女') + ')'"
                  :value="customer.customerId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="房间" prop="roomId">
              <el-select v-model="form.roomId" placeholder="请选择房间" style="width: 100%;" filterable @change="handleRoomChange">
                <el-option
                  v-for="room in freeRoomList"
                  :key="room.roomId"
                  :label="room.roomNumber + ' (' + room.typeName + ', 最多' + room.maxOccupancy + '人)'"
                  :value="room.roomId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入住日期" prop="checkInDate">
              <el-date-picker clearable
                              v-model="form.checkInDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择入住日期"
                              style="width: 100%;"
                              :disabled-date="disabledCheckInDate"
                              @change="calculateAmountAndNights">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="离店日期" prop="checkOutDate">
              <el-date-picker clearable
                              v-model="form.checkOutDate"
                              type="date"
                              value-format="yyyy-MM-dd"
                              placeholder="请选择离店日期"
                              style="width: 100%;"
                              :disabled-date="disabledCheckOutDate"
                              @change="calculateAmountAndNights">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="入住晚数" prop="nights">
              <el-input-number v-model="form.nights" :min="1" :max="30" label="入住晚数" style="width: 100%;" :disabled="true"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总金额" prop="totalAmount">
              <el-input v-model="form.totalAmount" placeholder="请输入总金额" style="width: 100%;" :disabled="true">
                <template slot="prepend">¥</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>

        <el-form-item label="联系邮箱" prop="contactEmail">
          <el-input v-model="form.contactEmail" placeholder="请输入联系邮箱" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预定状态" prop="reservationStatus">
              <el-select v-model="form.reservationStatus" placeholder="请选择预定状态" style="width: 100%;">
                <el-option
                  v-for="dict in dict.type.hotel_reservation_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="支付状态" prop="paymentStatus">
              <el-select v-model="form.paymentStatus" placeholder="请选择支付状态" style="width: 100%;">
                <el-option
                  v-for="dict in dict.type.hotel_payment_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="特殊要求" prop="specialRequest">
          <el-input v-model="form.specialRequest" type="textarea" placeholder="请输入特殊要求" :rows="3" />
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
    <el-dialog title="预定信息详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <div class="detail-container">
        <el-row :gutter="20">
          <!-- 左侧基本信息 -->
          <el-col :span="16">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span class="card-title">基本信息</span>
              </div>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="预定编号">{{ detailData.reservationCode }}</el-descriptions-item>
                <el-descriptions-item label="客户名称">{{ detailData.customerName }}</el-descriptions-item>
                <el-descriptions-item label="房间号">{{ detailData.roomNumber }}</el-descriptions-item>
                <el-descriptions-item label="房间类型">{{ detailData.roomTypeName }}</el-descriptions-item>
                <el-descriptions-item label="入住日期">{{ parseTime(detailData.checkInDate, '{y}-{m}-{d}') }}</el-descriptions-item>
                <el-descriptions-item label="离店日期">{{ parseTime(detailData.checkOutDate, '{y}-{m}-{d}') }}</el-descriptions-item>
                <el-descriptions-item label="入住晚数">{{ detailData.nights }}</el-descriptions-item>
                <el-descriptions-item label="总金额">¥{{ detailData.totalAmount }}</el-descriptions-item>
                <el-descriptions-item label="预定状态">
                  <dict-tag :options="dict.type.hotel_reservation_status" :value="detailData.reservationStatus"/>
                </el-descriptions-item>
                <el-descriptions-item label="支付状态">
                  <dict-tag :options="dict.type.hotel_payment_status" :value="detailData.paymentStatus"/>
                </el-descriptions-item>
                <el-descriptions-item label="联系电话">{{ detailData.contactPhone }}</el-descriptions-item>
                <el-descriptions-item label="联系邮箱">{{ detailData.contactEmail }}</el-descriptions-item>
                <el-descriptions-item label="特殊要求" :span="2">{{ detailData.specialRequest }}</el-descriptions-item>
                <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
                <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
                <el-descriptions-item label="修改人">{{ detailData.updateBy }}</el-descriptions-item>
                <el-descriptions-item label="修改时间">{{ parseTime(detailData.updateTime) }}</el-descriptions-item>
                <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
              </el-descriptions>
            </el-card>

            <!-- 房间设施信息 -->
            <el-card class="box-card" style="margin-top: 20px;" v-if="detailData.roomTypeName">
              <div slot="header" class="clearfix">
                <span class="card-title">房间设施</span>
              </div>
              <div class="facility-container">
                <el-tag v-for="(facility, index) in getFacilityList(detailData.facility)"
                        :key="index"
                        type="info"
                        class="facility-tag">
                  {{ facility }}
                </el-tag>
                <div v-if="!detailData.facility || getFacilityList(detailData.facility).length === 0" class="no-facility">
                  暂无设施信息
                </div>
              </div>
            </el-card>
          </el-col>

          <!-- 右侧二维码和房间图片 -->
          <el-col :span="8">
            <!-- 二维码卡片 -->
            <el-card class="box-card qr-code-card">
              <div slot="header" class="clearfix">
                <span class="card-title">预订二维码</span>
              </div>
              <div class="qr-code-container">
                <div v-if="detailData.qrCodeData" class="qr-code-image">
                  <img :src="detailData.qrCodeData" alt="预订二维码" class="qr-code-img">
                </div>
                <div v-else class="qr-code-placeholder">
                  <i class="el-icon-picture-outline"></i>
                  <p>暂无二维码</p>
                </div>
                <div class="qr-code-info">
                  <p class="qr-code-tip">请使用手机扫描二维码</p>
                  <p class="qr-code-desc">可用于支付、入住验证等操作</p>
                </div>
              </div>
            </el-card>

            <!-- 房间图片卡片 -->
            <el-card class="box-card image-card" style="margin-top: 20px;">
              <div slot="header" class="clearfix">
                <span class="card-title">房间图片</span>
              </div>
              <div class="image-preview-container">
                <div v-if="detailData.imageUrls">
                  <el-image
                    v-for="(url, index) in getImageUrlList(detailData.imageUrls)"
                    :key="index"
                    :src="getFullImageUrl(url)"
                    :preview-src-list="getImageUrlList(detailData.imageUrls)"
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
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReservationInfo, getReservationInfo, delReservationInfo, addReservationInfo, updateReservationInfo,
  listFreeRooms, listCustomers } from "@/api/reservationInfo/reservationInfo"
import { listInfoWithTypeInfo } from "@/api/info/info"
import { listCustomerInfo } from "@/api/customerInfo/customerInfo"


export default {
  name: "ReservationInfo",
  dicts: ['hotel_reservation_status', 'hotel_payment_status'],
  components: {
  },
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
      // 预定信息表格数据
      reservationInfoList: [],
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
        customerId: null,
        roomId: null,
        reservationStatus: null,
        paymentStatus: null,
      },
      // 表单参数
      form: {},
      // 详情数据
      detailData: {},
      // 表单校验
      rules: {
        customerId: [
          { required: true, message: "客户不能为空", trigger: "change" }
        ],
        roomId: [
          { required: true, message: "房间不能为空", trigger: "change" }
        ],
        checkInDate: [
          { required: true, message: "入住日期不能为空", trigger: "blur" }
        ],
        checkOutDate: [
          { required: true, message: "离店日期不能为空", trigger: "blur" }
        ],
        nights: [
          { required: true, message: "入住晚数不能为空", trigger: "blur" }
        ],
        contactPhone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        contactEmail: [
          { required: true, message: "联系邮箱不能为空", trigger: "blur" }
        ],
      },
      // 客户列表
      customerList: [],
      // 房间列表
      roomList: [],
      // 空闲房间列表
      freeRoomList: [],
      // 日期限制
      minDate: new Date()
    }
  },
  created() {
    this.getList()
    this.getCustomerList()
    this.getRoomList()
    this.getFreeRoomList()
  },
  methods: {
    /** 查询预定信息列表 */
    getList() {
      this.loading = true
      listReservationInfo(this.queryParams).then(response => {
        this.reservationInfoList = response.rows
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
        reservationId: null,
        customerId: null,
        roomId: null,
        reservationCode: null,
        checkInDate: null,
        checkOutDate: null,
        nights: 1,
        totalAmount: null,
        reservationStatus: "0", // 默认已预定
        specialRequest: null,
        contactPhone: null,
        contactEmail: null,
        paymentStatus: "0", // 默认未支付
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
      this.ids = selection.map(item => item.reservationId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      // 重新获取空闲房间列表
      this.getFreeRoomList()
      this.open = true
      this.title = "添加预定信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const reservationId = row.reservationId || this.ids
      getReservationInfo(reservationId).then(response => {
        this.form = response.data
        // 重新获取空闲房间列表
        this.getFreeRoomList()
        this.open = true
        this.title = "修改预定信息"
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
          if (this.form.reservationId != null) {
            updateReservationInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addReservationInfo(this.form).then(response => {
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
      const reservationIds = row.reservationId || this.ids
      this.$modal.confirm('是否确认删除预定信息编号为"' + reservationIds + '"的数据项？').then(function() {
        return delReservationInfo(reservationIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('reservationInfo/reservationInfo/export', {
        ...this.queryParams
      }, `reservationInfo_${new Date().getTime()}.xlsx`)
    },
    /** 获取客户列表 */
    getCustomerList() {
      listCustomerInfo({}).then(response => {
        this.customerList = response.rows
      })
    },
    /** 获取房间列表 */
    getRoomList() {
      listInfoWithTypeInfo({}).then(response => {
        this.roomList = response.rows
      })
    },
    /** 获取空闲房间列表 */
    getFreeRoomList() {
      listFreeRooms({ roomStatus: "0" }).then(response => {
        // 获取包含房间类型信息的空闲房间
        listInfoWithTypeInfo({ roomStatus: "0" }).then(res => {
          this.freeRoomList = res.rows
        })
      })
    },
    /** 房间选择变化 */
    handleRoomChange(value) {
      // 当房间选择变化时，重新计算金额
      this.calculateAmountAndNights()
    },
    /** 禁用入住日期 */
    disabledCheckInDate(time) {
      return time.getTime() < Date.now() - 8.64e7
    },
    /** 禁用离店日期 */
    disabledCheckOutDate(time) {
      if (!this.form.checkInDate) {
        return time.getTime() < Date.now() - 8.64e7
      }
      return time.getTime() < new Date(this.form.checkInDate).getTime()
    },
    /** 计算入住晚数和总金额 */
    calculateAmountAndNights() {
      // 检查必要字段是否已填写
      if (!this.form.checkInDate || !this.form.checkOutDate || !this.form.roomId) {
        return
      }

      // 计算入住晚数
      const checkIn = new Date(this.form.checkInDate)
      const checkOut = new Date(this.form.checkOutDate)

      // 计算天数差
      const timeDiff = checkOut.getTime() - checkIn.getTime()
      const nights = Math.ceil(timeDiff / (1000 * 3600 * 24))

      // 如果晚数小于1，则设置为1
      this.form.nights = nights > 0 ? nights : 1

      // 获取选中的房间信息
      const selectedRoom = this.freeRoomList.find(room => room.roomId === this.form.roomId)

      // 如果找到了房间信息且有价格，则计算总金额
      if (selectedRoom && selectedRoom.price) {
        this.form.totalAmount = (this.form.nights * selectedRoom.price).toFixed(2)
      }
    },
    /** 获取设施列表 */
    getFacilityList(facilityStr) {
      if (!facilityStr) return []
      return facilityStr.split(',').map(item => item.trim()).filter(item => item)
    },
    /** 获取图片URL列表 */
    getImageUrlList(imageUrls) {
      if (!imageUrls) return []
      return imageUrls.split(',').map(url => url.trim()).filter(url => url)
    },
    /** 获取完整图片URL */
    getFullImageUrl(url) {
      if (!url) return ''
      // 如果已经是完整URL，直接返回
      if (url.startsWith('http')) {
        return url
      }
      // 否则拼接基础URL
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

/* 二维码卡片样式 */
.qr-code-card {
  height: 100%;
}

.qr-code-container {
  text-align: center;
  padding: 20px 0;
}

.qr-code-image {
  margin-bottom: 20px;
}

.qr-code-img {
  max-width: 100%;
  height: auto;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
  background: #fff;
}

.qr-code-placeholder {
  height: 200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #c0c4cc;
  border: 1px dashed #ebeef5;
  border-radius: 4px;
  margin-bottom: 20px;
}

.qr-code-placeholder .el-icon-picture-outline {
  font-size: 48px;
  margin-bottom: 10px;
}

.qr-code-info {
  text-align: center;
}

.qr-code-tip {
  font-weight: 600;
  margin: 0 0 5px 0;
  color: #606266;
}

.qr-code-desc {
  font-size: 12px;
  color: #909399;
  margin: 0;
}

/* 房间设施样式 */
.facility-container {
  padding: 15px 0;
}

.facility-tag {
  margin: 0 5px 10px 0;
}

.no-facility {
  color: #909399;
  font-style: italic;
  text-align: center;
  padding: 20px 0;
}

/* 房间图片样式 */
.image-card {
  height: 100%;
}

.image-preview-container {
  padding: 15px 0;
}

.image-preview {
  width: 100%;
  height: 150px;
  border-radius: 4px;
  margin-bottom: 10px;
  background-color: #f5f7fa;
}

.image-preview:last-child {
  margin-bottom: 0;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.no-image {
  text-align: center;
  padding: 20px 0;
  color: #909399;
}
</style>
