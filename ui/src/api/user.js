import fetch from '@/utils/fetch'

export function login(data) {
  return fetch({
    url: '/rest/login',
    method: 'POST',
    data
  })
}

export function signup(data) {
  return fetch({
    url: '/user/signup',
    method: 'POST',
    data
  })
}

export function getInfo() {
  return fetch({
    url: '/rest/getInfo',
    method: 'GET'
  })
}

export function logUserOut() {
  return fetch({
    url: '/rest/logout',
    method: 'GET'
  })
}
