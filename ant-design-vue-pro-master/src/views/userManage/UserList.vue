<template>
  <div>
    <a-card class="card" title="用户列表">
      <a-table :columns="columns" :data-source="data">
        <span slot="status" slot-scope="text, record">
          <div class="noraml_text" v-if="record.status == 0">正常</div>
          <div class="disable_text" v-if="record.status == 1">禁用</div>
        </span>
        <span slot="action" slot-scope="text, record">
          <a-button type="primary" @click="disableUser(record)" size="default" v-if="record.status===0">
            禁用
          </a-button>
        </span>
      </a-table>
    </a-card>
  </div>
</template>
<script>
import * as userApi from '@/api/user'
// import user from '@/locales/lang/zh-CN/user'
const columns = [
    {
    title: '名称',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '账号',
    dataIndex: 'account',
    key: 'account'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'ucreateTime'
  },
  {
    title: '修改时间',
    key: 'updteTime',
    dataIndex: 'updateTime'
  },
  {
    title: '状态',
    key: 'status',
    scopedSlots: { customRender: 'status' },
    sorter: (a, b) => a.status - b.status
  },
  {
    title: '操作',
    key: 'action',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  data () {
    return {
        loading: true,
        data: [],
        columns
    }
},
created () {
  this.user = this.userInfo
  this.getUserList()
},
methods: {
  getUserList () {
    userApi.getUserList().then(res => {
    this.data = res.data
  })
  },
  disableUser (record) {
    userApi.diableUser(record.id).then(res => {
      if (res.code === 200) {
        this.getUserList()
      }
    })
  }
}
}
</script>
<style scoped>
.card {
    height: 795px;
}
.noraml_text {
    color: rgb(85, 133, 223); /* 使用预定义的颜色名 */
}
.disable_text {
    color: red; /* 使用预定义的颜色名 */
}
</style>
