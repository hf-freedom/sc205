<template>
  <el-container class="app-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <h3>停车场车位调度系统</h3>
      </div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        style="border-right: none"
      >
        <el-menu-item index="/dashboard">
          <span>实时监控</span>
        </el-menu-item>
        <el-menu-item index="/vehicle">
          <span>车辆管理</span>
        </el-menu-item>
        <el-menu-item index="/space">
          <span>车位管理</span>
        </el-menu-item>
        <el-menu-item index="/monthly-card">
          <span>月卡管理</span>
        </el-menu-item>
        <el-menu-item index="/reservation">
          <span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/statistics">
          <span>统计报表</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-main class="main">
      <router-view />
    </el-main>
  </el-container>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

.app-container {
  height: 100%;
}

.aside {
  background-color: #545c64;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
}

.main {
  margin-left: 220px;
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #4a5158;
  color: #fff;
  border-bottom: 1px solid #4a5158;
}

.logo h3 {
  font-size: 16px;
  font-weight: 500;
}
</style>
