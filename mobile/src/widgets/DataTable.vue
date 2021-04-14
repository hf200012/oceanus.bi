<template>
  <el-table
    :data="data"
    :style="chartStyle"
    :height="chartStyle.height"
    highlight-current-row
    border
    stripe
    :header-cell-style="{padding: '5px 0'}"
    :cell-style="{padding: '5px 0'}"
  >
    <el-table-column
      sortable
      v-for="item in schema"
      :key="item.Column"
      :prop="item.Column"
      :label="item.cname"
      align="center"
    />
  </el-table>
</template>
<script>
export default {
  props: {
    data: {
      type: Array,
      default: () => [],
    },
    schema: {
      type: Array,
      default: () => [],
    },
    chartStyle: {
      require: false,
      type: Object,
      default: () => {
        return {
          height: "500px",
        };
      },
    },
  },
};
</script>

