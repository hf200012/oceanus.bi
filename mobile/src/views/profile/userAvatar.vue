<template>
  <div>
    <img v-bind:src="options.img" @click="editCropper()" title="点击上传头像" class="img-circle img-lg" />
    
  </div>
</template>

<script>
import store from "@/store";
import { VueCropper } from "vue-cropper";
import { uploadAvatar } from "@/api/userinfo";

export default {
  components: { VueCropper },
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      // 是否显示弹出层
      open: false,
      // 是否显示cropper
      visible: false,
      // 弹出层标题
      title: "修改头像",
      options: {
        img: store.getters.avatar, //裁剪图片的地址
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 200, // 默认生成截图框宽度
        autoCropHeight: 200, // 默认生成截图框高度
        fixedBox: true // 固定截图框大小 不允许改变
      },
      previews: {}
    };
  },
  methods: {
    // 编辑头像
    editCropper() {
      this.open = true;
    },
    // 打开弹出层结束时的回调
    modalOpened() {
      this.visible = true;
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf("image/") == -1) {
        this.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.options.img = reader.result;
        };
      }
    },
    // 上传图片
    uploadImg() {
      this.$refs.cropper.getCropBlob(data => {
        let formData = new FormData();
        formData.append("avatarfile", data);
        uploadAvatar(formData).then(response => {
          if (response.code === 200) {
            this.open = false;
            this.options.img = process.env.VUE_APP_BASE_API + response.imgUrl;
            store.commit('SET_AVATAR', this.options.img);
            this.msgSuccess("修改成功");
          }
          this.visible = false;
        });
      });
    },
    // 实时预览
    realTime(data) {
      this.previews = data;
    }
  }
};
</script>