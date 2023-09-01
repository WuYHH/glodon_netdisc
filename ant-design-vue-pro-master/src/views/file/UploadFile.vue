<template>
  <div>
    <a-card class="card" title="上传文件">
      <a-upload-dragger
        name="file"
        :multiple="true"
        :action="uploadUrl"
      >
        <p class="ant-upload-drag-icon">
          <a-icon type="inbox" />
        </p>
        <p class="ant-upload-text">
          点击或者拖拽文件到此区域上传文件
        </p>
        <p class="ant-upload-hint">
          按Ctrl可以同时上传多个文件
        </p>
      </a-upload-dragger>
    </a-card>
  </div>
</template>
<script>
import * as fileApi from '@/api/file'
export default {
  data () {
    return {
      loading: true,
      uploadUrl: ''
    }
  },
  computed: {
    userInfo () {
      return this.$store.getters.userInfo
    }
  },
  created () {
    this.user = this.userInfo
    this.uploadUrl = 'http://10.8.48.95:8089/cloudNetDisc/file/uploadFile?userId=' + this.user.id
  },
  methods: {
    handleChange (info) {
      fileApi.uploadFile(info.fileList, this.user.id).then(res => {
        console.log('res', res)
      })
      // const status = info.file.status
      // if (status !== 'uploading') {
      // }
      // if (status === 'done') {
      //   this.$message.success(`${info.file.name} file uploaded successfully.`)
      // } else if (status === 'error') {
      //   this.$message.error(`${info.file.name} file upload failed.`)
      // }
    }
  }
}
</script>
<style scoped>
.card {
    height: 750px;
}
</style>
