<template>
  <div class="container">
    <img src="../assets/logo.png">
    <h1>404</h1>
    <h2>Page Not Found</h2>
    <br>
    <el-button type="success" size="large" @click="$router.push('/chartpanel/create')">
      Go To Create.
    </el-button>
  </div>
</template>
<style lang="scss" scoped>
.container {
  text-align: center;
  padding-top: 200px;
  img {
    width: 200px;
  }
  h1 {
    font-size: 48px;
    color: #2566dd;
  }
  h2{
    font-size: 36px;
    color: #2566dd;
  }
}
</style>
