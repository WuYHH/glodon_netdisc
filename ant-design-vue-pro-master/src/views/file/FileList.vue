<template>
  <div>
    <div style="margin-bottom: 16px">
      <a-button type="primary" :disabled="!hasSelected" :loading="loading" @click="downloadFiles">
        下载
      </a-button>
      <span style="margin-left: 8px">
        <template v-if="hasSelected">
          {{ `已选择 ${selectedRowKeys.length} 个文件` }}
        </template>
      </span>
    </div>
    <a-table
      bordered
      :data-source="dataSource"
      :columns="columns"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      :rowKey="record=>record.url">
      <template slot="name" slot-scope="text, record">
        <editable-cell :text="text" @change="updateFileName(record, 'name', $event)" />
      </template>
      <span slot="url" slot-scope="text, record">
        <a :href="record.url">{{ record.url }}</a>
      </span>
      <span slot="action" slot-scope="text, record">
        <!-- <a>{{ record.key }}</a> -->
        <a-button-group>
          <a-button type="primary" @click="showFile(record)" size="default">
            预览
          </a-button>
          <a-button type="primary" @click="dowdSelectLoadFile(record)" size="default">
            下载
          </a-button>
          <a-button type="danger" @click="deleteFile(record.id)" size="default">
            删除
          </a-button>
        </a-button-group>
      </span>
    </a-table>
    <a-drawer title="文件预览" placement="right" width="600px" :visible="infoVisible" @close="onClose">
      <h3 slot="author">{{ fileName }}</h3>
      <p slot="content">
        {{ content }}
      </p>
      <!-- <a-comment>
      </a-comment> -->
    </a-drawer>
  </div>
</template>
<script>
import * as fileApi from '@/api/file'
import VueDocPreview from 'vue-doc-preview'
import CryptoJS from 'crypto-js'

const EditableCell = {
  template: `
      <div class="editable-cell">
        <div v-if="editable" class="editable-cell-input-wrapper">
          <a-input :value="value" @change="handleChange" @pressEnter="check" /><a-icon
            type="check"
            class="editable-cell-icon-check"
            @click="check"
          />
        </div>
        <div v-else class="editable-cell-text-wrapper">
          {{ value || ' ' }}
          <a-icon type="edit" class="editable-cell-icon" @click="edit" />
        </div>
      </div>
    `,
  props: {
    text: String
  },
  data () {
    return {
      value: this.text,
      editable: false
    }
  },
  methods: {
    handleChange (e) {
      const value = e.target.value
      this.value = value
    },
    check () {
      this.editable = false
      this.$emit('change', this.value)
    },
    edit () {
      this.editable = true
    }
  }
}
export default {
  components: {
    EditableCell,
    VueDocPreview
  },
  data () {
    return {
      dataSource: [],
      count: 2,
      columns: [
        {
          title: '文件名称',
          dataIndex: 'name',
          key: 'name',
          width: '300px',
          scopedSlots: { customRender: 'name' }
        },
        {
          title: '文件大小',
          dataIndex: 'size',
          key: 'size',
          sorter: (a, b) => a.size - b.size
        },
        {
          title: '文件地址',
          dataIndex: 'url',
          key: 'url',
          width: 350,
          scopedSlots: { customRender: 'url' }
        },
        {
          title: '创建时间',
          key: 'create_time',
          dataIndex: 'create_time'
        },
        {
          title: '修改时间',
          key: 'update_time',
          dataIndex: 'update_time'
        },
        {
          title: '文件说明',
          key: 'detail',
          dataIndex: 'update_time'
        },
        {
          title: '操作',
          key: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      infoVisible: false,
      currentUrl: '',
      fileName: '',
      content: '',
      user: {},
      selectedRowKeys: [],
      loading: false
    }
  },
  computed: {
    userInfo () {
      return this.$store.getters.userInfo
    },
    hasSelected () {
      return this.selectedRowKeys.length > 0
    }
  },
  created () {
    this.user = this.userInfo
    this.getFileList()
  },
  methods: {
    getFileList () {
        fileApi.getFileList(this.user.id).then(res => {
        this.dataSource = res.data
        console.log('文件数据', res)
      })
    },
    deleteFile (key) {
      console.log('id', key)
      fileApi.deletFile(key).then(res => {
        if (res.code === 200) {
          this.$message.success('修改成功')
          this.getFileList()
        } else {
          this.$message.error(res.message)
        }
      })
    },
    showFile (record) {
      // fetch(record.url, { mode: 'no-cors' })
      //   .then(response => response.text())
      //   .then(content => {
      //     this.content = content
      //     console.log('内容', content)
      //   })
      //   .catch(error => {
      //     console.error('读取文件错误：', error)
      //   })
      // this.infoVisible = true
      this.currentUrl = CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(record.url))
      console.log(record.url, 'record.url')
      console.log(this.currentUrl, ' this.currentUrl')
      window.open('http://10.8.54.80:8012/onlinePreview?url=' + encodeURIComponent(this.currentUrl))
      // fileApi.showFile(record.id).then(res => {
      //   console.log('预览', res)
      // })
    },
    dowdSelectLoadFile (record) {
      fileApi.downloadFile(record.id).then(res => {
        console.log('下载', res)
      })
      console.log(record)
    },
    onClose () {
      this.infoVisible = false
    },
    updateFileName (record, dataIndex, value) {
      fileApi.updateFileName(record.id, value).then(res => {
        if (res.code === 200) {
          this.getFileList()
          this.$message.success('修改成功')
        } else {
          this.$message.error(res.message)
        }
      })
    },
    async downloadFiles () {
      this.loading = true
      var urlList = this.selectedRowKeys
      for (const url of urlList) {
        await this.downloadFile(url)
      }
      this.loading = false
    },
    async downloadFile (url) {
      const link = document.createElement('a')
      link.href = url
      link.target = '_self'
      link.download = url.substring(url.lastIndexOf('/') + 1) // 获取文件名作为下载名称
      await new Promise((resolve) => {
      link.addEventListener('click', () => {
          setTimeout(resolve, 100) // 增加延迟等待下载处理
        })
        link.click()
      })
    },
    onSelectChange (selectedRowKeys) {
      // console.log('selectedRowKeys changed: ', selectedRowKeys)
      this.selectedRowKeys = selectedRowKeys
    }
  }
}
</script>
<style>
.editable-cell {
  position: relative;
}

.editable-cell-input-wrapper,
.editable-cell-text-wrapper {
  padding-right: 24px;
}

.editable-cell-text-wrapper {
  padding: 5px 24px 5px 5px;
}

.editable-cell-icon,
.editable-cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
}

.editable-cell-icon {
  line-height: 18px;
  display: none;
}

.editable-cell-icon-check {
  line-height: 28px;
}

.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}

.editable-cell-icon:hover,
.editable-cell-icon-check:hover {
  color: #108ee9;
}
</style>
