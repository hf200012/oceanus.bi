<template>
  <div class="container">
    <img src="../assets/logo.png" />
    <h1>{{ $t('home.slogan') }}</h1>
    <el-button
      type="success"
      size="large"
      @click="$router.push('/chartpanel/create')"
    >{{ $t('home.startChart') }}</el-button>

     <el-button
      type="success"
      size="large"
      @click="$router.push('/sqlconsole/create')"
    >{{ $t('home.startReport') }}</el-button>
  </div>
</template>
<style lang="scss" scoped>
.container {
  text-align: center;
  padding-top: 100px;
}
</style>
