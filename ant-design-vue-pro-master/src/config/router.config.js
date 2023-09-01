// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView } from '@/layouts'
import { bxAnaalyse } from '@/core/icons'

// const RouteView = {
//   name: 'RouteView',
//   render: h => h('router-view')
// }

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: 'glodon网盘' },
    redirect: '/file/fileList',
    children: [
      // dashboard
      {
        path: '/dashboard',
        name: 'dashboard',
        redirect: '/dashboard/',
        component: RouteView,
        meta: { title: '仪表盘', keepAlive: true, icon: bxAnaalyse },
        children: [
          {
            path: '/dashboard/analysis',
            name: 'NormalAnalysis',
            component: () => import('@/views/dashboard/NormalAnalysis'),
            meta: { title: '分析页(普)', keepAlive: false, permission: ['userDashboard'] }
          },
          {
            path: '/dashboard/AdminAnalysis',
            name: 'AdminAnalysis',
            component: () => import('@/views/dashboard/AdminAnalysis'),
            meta: { title: '分析页(管)', keepAlive: false, permission: ['adminDashboard'] }
          }
        ]
      },
      {
        path: '/file',
        component: RouteView,
        redirect: '/file/fileList',
        name: 'file',
        meta: { title: '文件管理', icon: 'user', keepAlive: true, permission: ['file'] },
        children: [
          {
            path: '/file/FileList',
            name: 'fileList',
            component: () => import('@/views/file/FileList'),
            meta: { title: '文件列表', hidden: true, permission: ['file'] }
          },
          {
            path: '/file/UploadFile',
            name: 'uploadFile',
            component: () => import('@/views/file/UploadFile'),
            meta: { title: '文件上传', hidden: true, permission: ['file'] }
          }
        ]
      },
      {
        path: '/userMange',
        component: RouteView,
        redirect: '/userManage/UserList',
        name: 'file',
        meta: { title: '用户管理', icon: 'user', keepAlive: true, permission: ['user'] },
        children: [
          {
            path: '/userManage/UserList',
            name: 'userList',
            component: () => import('@/views/userManage/UserList'),
            meta: { title: '用户列表', hidden: true, permission: ['user'] }
          }
        ]
      },
      // account
      {
        path: '/account',
        component: RouteView,
        redirect: '/account/center',
        name: 'account',
        meta: { title: 'menu.account', icon: 'user', keepAlive: true, permission: ['self'] },
        children: [
          {
            path: '/account/settings',
            name: 'settings',
            component: () => import('@/views/account/settings/Index'),
            meta: { title: 'menu.account.settings', hideHeader: true, permission: ['self'] },
            redirect: '/account/settings/basic',
            hideChildrenInMenu: true,
            children: [
              {
                path: '/account/settings/basic',
                name: 'BasicSettings',
                component: () => import('@/views/account/settings/BasicSetting'),
                meta: { title: 'account.settings.menuMap.basic', hidden: true, permission: ['self'] }
              }
            ]
          }
        ]
      }

    ]
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/RegisterResult')
      },
      {
        path: 'recover',
        name: 'recover',
        component: undefined
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }
]
