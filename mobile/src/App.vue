<template>
  <div id="app">
    <router-view />
     <i18n />
  </div>
</template>
<script>
import i18n from '@/components/i18n'
import { mapGetters } from 'vuex'
import Cookies from "js-cookie";
import store from './store'

export default {
  name: 'App',
  components: { i18n },
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
}
</script>
<style lang="scss"></style>
