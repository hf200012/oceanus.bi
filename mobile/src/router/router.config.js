/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/',
    component: () => import('@/views/layouts/index'),
    redirect: '/home',
    meta: {
      title: '首页',
      keepAlive: false
    },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/home/index'),
        meta: { title: '首页', keepAlive: false }
      },
      {
        path: '/dashboard_list',
        name: 'dashboard_list',
        component: () => import('@/views/home/dashboard'),
        meta: { title: '数据概览', keepAlive: false }
      },
      {
        path: '/report',
        name: 'report',
        component: () => import('@/views/home/reportCenter'),
        meta: { title: '报表中心', keepAlive: false }
      },
      {
        path: '/about',
        name: 'About',
        component: () => import('@/views/home/about'),
        meta: { title: '关于我', keepAlive: false }
      },
      {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login'),
        meta: { title: 'login', keepAlive: false }
      },
      {
        path: '/dashboard',
        name: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: 'dashboard', keepAlive: false }
      },
      {
        path: '/reportcenter',
        name: 'reportcenter',
        component: () => import('@/views/reportcenter/index'),
        meta: { title: 'reportcenter', keepAlive: false }
      },
      {
        path: '/profile',
        name: 'profile',
        component: () => import('@/views/profile/index'),
        meta: { title: 'profile', keepAlive: false }
      }
    ]
  }
]
