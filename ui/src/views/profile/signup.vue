<template>
  <div class="login">
    <el-form ref="form" :model="form" :rules="rules" class="login-form">
      <h3 class="title">用户注册</h3>

      <el-form-item prop="user_name">
        <el-input v-model="form.user_name" type="text" placeholder="用户名" />
      </el-form-item>

      <el-form-item prop="nick_name">
        <el-input v-model="form.nick_name" type="text" placeholder="姓名" />
      </el-form-item>

      <el-form-item prop="email">
        <el-input v-model="form.email" type="email" placeholder="邮箱" />
      </el-form-item>

      <el-form-item prop="phonenumber">
        <el-input v-model="form.phonenumber" type="text" placeholder="手机号码" />
      </el-form-item>

      <el-form-item prop="password">
        <el-input v-model="form.password" type="password" placeholder="用户密码" />
      </el-form-item>

      <el-form-item prop="confirmPassword">
        <el-input v-model="form.confirmPassword" type="password" placeholder="确认密码" />
      </el-form-item>

      <el-form-item style="width:100%;">
        <el-button type="primary" @click="submitForm">注 册</el-button>
        <el-button @click="cancel">取 消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { signup } from "@/api/user";
export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.form.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      user: {
        userName: undefined,
        password: undefined,
      },
      form: [],
      // 表单校验
      rules: {
        user_name: [
          { required: true, message: "用户名不能为空", trigger: "blur" },
        ],
        nick_name: [
          { required: true, message: "用户姓名不能为空", trigger: "blur" },
        ],
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
          {
            min: 6,
            max: 20,
            message: "长度在 6 到 20 个字符",
            trigger: "blur",
          },
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    cancel() {
      this.$router.push("/login");
    },
    handleLogin() {
      this.loading = true;
      signup(this.user)
        .then((resp) => {
          this.loading = false;
          this.$message({
            type: "success",
            message: this.$t("auth.signUpSuccess"),
          });
          this.$router.push("/login");
        })
        .catch(() => {
          this.loading = false;
        });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../../assets/image/login-background.png");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
</style>
