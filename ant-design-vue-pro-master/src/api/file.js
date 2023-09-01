import request from '@/utils/request'

const fileApi = {
  uploadFile: '/cloudNetDisc/file/uploadFile',
  downloadFile: '/cloudNetDisc/file/download',
  updateFileName: '/cloudNetDisc/file/updateFileNameById',
  showFile: '/cloudNetDisc/file/getFileDetailsById',
  deletFile: '/cloudNetDisc/file/deleteFileById',
  getFileList: '/cloudNetDisc/file/getFileListByUserId'
}

export function getFileList (id) {
  return request({
    url: `${fileApi.getFileList}?userId=${id}`,
    method: 'get'
  })
}
export function downloadFile (id) {
  return request({
    url: `${fileApi.downloadFile}?id=${id}`,
    method: 'get'
  })
}

export function uploadFile (files, id) {
  return request({
    url: `${fileApi.uploadFile}?userId=${id}`,
    method: 'post',
    params: files,
    Headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
export function updateFileName (fileId, name) {
    return request({
      url: `${fileApi.updateFileName}?fileId=${fileId}&name=${name}`,
      method: 'get'
    })
  }
export function showFile (id) {
    return request({
        url: `${fileApi.showFile}?id=${id}`,
        method: 'get'
    })
}

export function deletFile (id) {
    return request({
        url: `${fileApi.deletFile}?fileId=${id}`,
        method: 'get'
    })
}
