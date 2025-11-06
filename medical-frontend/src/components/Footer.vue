<template>
  <footer class="footer">
    <div class="footer-container">
      <div class="footer-content">
        <p>&copy; 2025 医疗问诊管理系统. All rights reserved.</p>
        <p class="footer-links">
          <a href="#">关于我们</a>
          <span>|</span>
          <a href="#">联系方式</a>
          <span>|</span>
          <a href="#">隐私政策</a>
          <span>|</span>
          <a href="#">服务条款</a>
        </p>
      </div>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
.footer {
  background-color: #2c3e50;
  color: #fff;
  padding: 30px 0;
  margin-top: auto;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.footer-content {
  text-align: center;
}

.footer-content p {
  margin: 8px 0;
  font-size: 14px;
}

.footer-links {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
}

.footer-links a {
  color: #fff;
  transition: color 0.3s;
}

.footer-links a:hover {
  color: var(--primary-light);
}
</style>




