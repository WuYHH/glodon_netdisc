import request from '@/utils/request'

const userApi = {
  getUserList: 'cloudNetDisc/user/getUserList',
  diableUser: 'cloudNetDisc/user/setStatus'
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

export function getUserList () {
  return request({
    url: userApi.getUserList,
    method: 'get'
  })
}
export function diableUser (id) {
    return request({
      url: `${userApi.diableUser}?id=${id}`,
      method: 'post'
    })
  }
