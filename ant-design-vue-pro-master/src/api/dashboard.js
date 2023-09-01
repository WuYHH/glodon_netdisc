import request from '@/utils/request'

const userApi = {
  getFileCount: '/cloudNetDisc/file/count',
  getFileCountSize: '/cloudNetDisc/file/size',
  getFileType: '/cloudNetDisc/file/typeCount',
  getTypeCount: '/cloudNetDisc/file/getTypes',
  getUserCount: '/cloudNetDisc/user/count',
  getUserFileCount: '/cloudNetDisc/file/getUserTypes'
}

/**
 * }
 * @param parameter
 * @returns {*}
 */

export function getFileCount (id) {
  return request({
    url: `${userApi.getFileCount}?userId=${id}`,
    method: 'get'
  })
}
export function getFileCountSize (id) {
  return request({
    url: `${userApi.getFileCountSize}?userId=${id}`,
    method: 'get'
  })
}
export function getFileTypeCount (id) {
  return request({
    url: `${userApi.getFileType}?userId=${id}`,
    method: 'get'
  })
}
export function getTypeCount (id) {
  return request({
    url: `${userApi.getTypeCount}?userId=${id}`,
    method: 'get'
  })
}

export function getAdminFileCount () {
  return request({
    url: userApi.getFileCount,
    method: 'get'
  })
}
export function getAdminFileCountSize () {
  return request({
    url: userApi.getFileCountSize,
    method: 'get'
  })
}
export function getAdminFileTypeCount () {
  return request({
    url: userApi.getFileType,
    method: 'get'
  })
}
export function getAdminTypeCount () {
  return request({
    url: userApi.getTypeCount,
    method: 'get'
  })
}
export function getUserCount () {
  return request({
    url: userApi.getUserCount,
    method: 'get'
  })
}
export function getUserFileCount () {
  return request({
    url: userApi.getUserFileCount,
    method: 'get'
  })
}
