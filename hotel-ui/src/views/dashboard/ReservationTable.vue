<template>
  <div class="reservation-table">
    <el-table :data="tableData" style="width: 100%" :loading="loading" stripe>
      <el-table-column prop="reservationCode" label="预订编号" width="120" />
      <el-table-column prop="customerName" label="客户姓名" width="100" />
      <el-table-column prop="roomNumber" label="房间号" width="80" />
      <el-table-column prop="roomTypeName" label="房间类型" width="120" />
      <el-table-column prop="checkInDate" label="入住日期" width="120">
        <template slot-scope="scope">
          {{ scope.row.checkInDate ? $formatDate(scope.row.checkInDate) : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="checkOutDate" label="离店日期" width="120">
        <template slot-scope="scope">
          {{ scope.row.checkOutDate ? $formatDate(scope.row.checkOutDate) : '' }}
        </template>
      </el-table-column>
      <el-table-column prop="totalAmount" label="总金额(元)" width="100" />
      <el-table-column prop="reservationStatus" label="预订状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.reservationStatus)">
            {{ getStatusText(scope.row.reservationStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="150">
        <template slot-scope="scope">
          {{ scope.row.createTime ? $formatDateTime(scope.row.createTime) : '' }}
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-container" v-if="total > 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryParams.pageNum"
        :page-sizes="[10, 20, 30, 50]"
        :page-size="queryParams.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>
  </div>
</template>

<script>
import { getRecentWeekReservationsList } from '@/api/dashboard'

export default {
  name: 'ReservationTable',
  data() {
    return {
      tableData: [],
      loading: false,
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      getRecentWeekReservationsList()
        .then(response => {
          if (response.code === 200) {
            this.tableData = response.data || []
            this.total = this.tableData.length
          } else {
            this.tableData = []
            this.total = 0
            console.error('获取近7天预约数据失败:', response.msg)
          }
        })
        .catch(error => {
          console.error('获取近7天预约数据失败:', error)
          this.tableData = []
          this.total = 0
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    getStatusType(status) {
      const statusMap = {
        '0': 'info',    // 已预定
        '1': 'primary', // 已确认
        '2': 'success', // 已入住
        '3': 'danger',  // 已取消
        '4': 'warning'  // 已完成
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        '0': '已预定',
        '1': '已确认',
        '2': '已入住',
        '3': '已取消',
        '4': '已完成'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style scoped>
.reservation-table {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

/* 表格样式优化 */
::v-deep .el-table {
  border-radius: 8px;
  overflow: hidden;
}

::v-deep .el-table__header th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
}

::v-deep .el-table__row:hover {
  background-color: #f5f7fa;
}

/* 状态标签样式优化 */
::v-deep .el-tag {
  border-radius: 12px;
  padding: 0 10px;
  font-size: 12px;
}
</style>