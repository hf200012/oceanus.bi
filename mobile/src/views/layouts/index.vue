<template>
  <div id="app">
    <div class="layout-footer">
      <TabBar :data="tabbars" @change="handleChange" />
    </div>
    <router-view />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getToken } from "@/utils/auth";

import Cookies from "js-cookie";
import store from '../../store'
import TabBar from '@/components/TabBar'
export default {
  name: 'AppLayout',
  computed: {
    ...mapGetters([
      'avatar',
      'username',
      'isadmin'
    ]),
    defaultActive() {
      return this.$route.path
    }
  },
  data() {
    return {
      tabbars: [
        {
          title: '',
          to: {
            name: 'Home'
          },
          icon: 'iconfont iconguanli'
        },
        {
          title: '',
          to: {
            name: 'dashboard_list'
          },
          icon: 'iconfont iconyibiaopan'
        },
        {
          title: '',
          to: {
            name: 'report'
          },
          icon: 'iconfont iconbaobiao'
        },
        {
          title: '',
          to: {
            name: 'profile'
          },
          icon: 'iconfont icongroup'
        }
      ]
    }
  },
  components: {
    TabBar
  },
  methods: {
    handleChange(v) {
      console.log('tab value:', v)
    }
  }
}
</script>
