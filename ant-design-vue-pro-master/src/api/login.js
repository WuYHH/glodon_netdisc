import request from '@/utils/request'

const userApi = {
  Login: '/cloudNetDisc/user/login',
  Logout: '/auth/logout',
  ForgePassword: '/auth/forge-password',
  Register: '/auth/register',
  twoStepCode: '/auth/2step-code',
  SendSms: '/account/sms',
  SendSmsErr: '/account/sms_err',
  // get my info
  UserInfo: '/user/info',
  UserMenu: '/user/nav',
  getUser: '/user/get',
  regist: '/cloudNetDisc/user/register',
  getInfo: '/cloudNetDisc/user/getUserInfo',
  getCaptcha: '/cloudNetDisc/user/captcha',
  updatePwd: 'cloudNetDisc/user/modify'
}

/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */
// export function login (parameter) {
//   return request({
//     url: userApi.Login,
//     method: 'post',
//     data: parameter
//   })
// }

// export function getInfo () {
//   return request({
//     url: userApi.UserInfo,
//     method: 'get',
//     headers: {
//       'Content-Type': 'application/json;charset=UTF-8'
//     }
//   })
// // }

// export function getCurrentUserNav () {
//   return request({
//     url: userApi.UserMenu,
//     method: 'get'
//   })
// }

export function logout () {
  return request({
    url: userApi.Logout,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}
export function getSmsCaptcha (parameter) {
  return request({
    url: userApi.SendSms,
    method: 'post',
    data: parameter
  })
}
/**
 * get user 2step code open?
 * @param parameter {*}
 */
export function get2step (parameter) {
  return request({
    url: userApi.twoStepCode,
    method: 'post',
    data: parameter
  })
}
export function login (parameter) {
  return request({
    url: userApi.Login,
    method: 'post',
    data: parameter
  })
}
export function getInfo (id) {
  return request({
    url: `${userApi.getInfo}?id=${id}`,
    method: 'get'
  })
}
export function regist (parameter) {
  return request({
    url: userApi.regist,
    method: 'post',
    data: parameter
  })
}
export function getCaptcha (parameter) {
  return request({
    url: userApi.getCaptcha,
    method: 'get'
  })
}
export function updatePwd (param) {
  return request({
    url: userApi.updatePwd,
    method: 'post',
    data: param
  })
}
