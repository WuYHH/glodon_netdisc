<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16" type="flex" justify="center">
      <a-col :order="isMobile ? 2 : 1" :md="24" :lg="16">

        <a-form :form="form" layout="vertical">
          <a-form-item
            label="原密码"
          >
            <a-input-password
              v-decorator="[
                'oldPwd',
                { rules: [{ required: true, message: '请输入原密码' }] },
              ]"/>
          </a-form-item>
          <a-form-item
            label="新密码"
          >
            <a-input-password
              v-decorator="[
                'newPwd',
                { rules: [{ required: true, message: '请输入新密码' }] },
              ]"/>
          </a-form-item>
          <a-form-item
            label="新密码"
          >
            <a-input-password
              v-decorator="[
                'tnewPwd',
                { rules: [{ required: true, message: '请再次输入新密码' }] },
              ]"/>
          </a-form-item>

          <a-form-item>
            <a-button type="primary" @click="submit">提交</a-button>
          </a-form-item>
        </a-form>

      </a-col>
      <a-col :order="1" :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <div class="ant-upload-preview" @click="$refs.modal.edit(1)" >
          <a-icon type="cloud-upload-o" class="upload-icon"/>
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="option.img"/>
        </div>
      </a-col>

    </a-row>

    <avatar-modal ref="modal" @ok="setavatar"/>

  </div>
</template>

<script>
import AvatarModal from './AvatarModal'
import { baseMixin } from '@/store/app-mixin'
import * as api from '@/api/login'
export default {
  mixins: [baseMixin],
  components: {
    AvatarModal
  },
  data () {
    return {
      // cropper
      preview: {},
      option: {
        img: '/avatar2.jpg',
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 180,
        autoCropHeight: 180,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      },
      user: {},
      form: this.$form.createForm(this, { name: 'pwdForm' })
    }
  },
  computed: {
    userInfo () {
      return this.$store.getters.userInfo
    }
  },
  created () {
    this.user = this.userInfo
  },
  methods: {
    setavatar (url) {
      this.option.img = url
    },
    submit () {
      this.form.validateFields((err, values) => {
        if (!err) {
          console.log(values)
          if (values.newPwd !== values.tnewPwd) {
            this.$message.error('两次密码不一致')
          } else {
            values['account'] = this.user.account
            values['oldPassword'] = values.oldPwd
            values['newPassword'] = values.newPwd
            api.updatePwd(values).then(res => {
            if (res.code === 200) {
              this.$message.success('修改成功')
              return this.$store.dispatch('Logout').then(() => {
              this.$router.push({ name: 'login' })
             })
            } else {
              this.$message.error(res.message)
            }
          })
          }
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-wrapper {
    height: 200px;
    width: 100%;
  }

  .ant-upload-preview {
    position: relative;
    margin: 0 auto;
    width: 100%;
    max-width: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;

    .upload-icon {
      position: absolute;
      top: 0;
      right: 10px;
      font-size: 1.4rem;
      padding: 0.5rem;
      background: rgba(222, 221, 221, 0.7);
      border-radius: 50%;
      border: 1px solid rgba(0, 0, 0, 0.2);
    }
    .mask {
      opacity: 0;
      position: absolute;
      background: rgba(0,0,0,0.4);
      cursor: pointer;
      transition: opacity 0.4s;

      &:hover {
        opacity: 1;
      }

      i {
        font-size: 2rem;
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -1rem;
        margin-top: -1rem;
        color: #d6d6d6;
      }
    }

    img, .mask {
      width: 100%;
      max-width: 180px;
      height: 100%;
      border-radius: 50%;
      overflow: hidden;
    }
  }
</style>
