export default {
  common: {
    title: '启明数智大数据可视化平台',
    project:'项目',
    dashboard: '数据概览',
    chart: '图表设计',
    dataSource: '数据源',
    metadata:'元数据管理',
    profile:'个人中心', 
    sqlconsole:'报表设计',
    reportcenter:'报表中心',
    usercenter:'用户中心', 
    saveData:'保存数据',   
    addtable:'添加表',
    importtable:'导入表字段',
    downdrill:'数据钻取', 
    datav:'数据大屏',
    ganged:'图表联动演示',
    share: '分享',
    export:'导出',
    add: '添加',
    edit: '编辑',
    delete: '删除',
    cancel: '取消',
    confirm: '确认',
    name: '名称',
    desc: '描述',
    close: '关闭',
    operation: '操作',
    back: '后退',
    save: '保存',
    open: '开启',
    closed: '关闭',
    saveSuccess: '保存成功',
    deleteSuccess: '删除成功',
    openGuide: '开启新手引导',
    firstAddProject:'请从项目进入选择项目,为项目添加概览' 
  },
  reportcenter: {
    title:'数据报表'
  },
  sqlconsole:{
    createReport:'创建新的报表',
    editReport:'编辑报表',
    allReports:'所有报表',
    saveReport:'保存',
    reportName:'报表名称',
    reportDesc:'报表介绍',
    namePlaceholder:'请输入报表名称',
    descPlaceholder:'请输入报表介绍',
    isPublic:'是否公开',
    public:'公开',
    report:'我的报表'
  },
  project:{
    createproject:'创建项目'
  },
  auth: {
    useTestAccount: '不想注册？点我使用测试账号',
    signIn: '登录',
    signUp: '注册',
    toSignUp: '没有账号？去注册',
    toSignIn: '已有账号? 去登录',
    passwordPlaceholder: '请输入密码',
    emailPlaceholder: '请输入邮箱',
    usernamePlaceholder: '请输入用户名',
    logout: '登出',
    signUpSuccess: '注册成功，请登录'
  },
  navbar: {
    document: '文档',
    home: '首页'
  },
  home: {
    slogan: '启明数智大数据可视化分析平台.',
    startChart: '开始设计图表',
    startReport: '开始设计报表'
  },
  dashboard: {
    addDashboard: '添加数据概览',
    dashboardName: '概览名称',
    dashboardDesc: '概览描述',
    addOrEditDashboard: '添加或编辑数据概览',
    addChart: '添加图表',
    shareLink: '分享链接',
    emptyDashboardTip: '数据概览空空如也, 先去创建项目,然后创建项目数据概览,去创建新的图表吧!',
    dashboardNamePlaceholder: '请输入数据概览名称',
    dashboardDescPlaceholder: '请输入数据概览描述.',
    dashboardList: '数据概览列表',
    removeChartConfirm: '确定要把该图表从概览中移除 ?',
    chartQueryException: '图表：{0} 查询语句异常',
    deleteConfirm: '确定要删除[{0}]概览吗？将同时删除该概览的分享',
    isPublic:'是否公开',
    public:'公开',
  },
  chart: {
    myChart: '我的图表',
    createNewChart: '创建新的图表',
    editChart: '编辑图表',
    createChart: '创建图表',
    allCharts: '所有图表',
    addToDashboard: '添加到概览',
    chartName: '图表名称',
    chartDesc: '图表描述',
    namePlaceholder: '请输入图表名称',
    descPlaceholder: '请输入图表描述',
    dimensions: '维度',
    values: '指标',
    order: '排序',
    selectOrderBy: '选择排序方式',
    descend: '降序',
    ascend: '正序',
    filters: '筛选',
    chartType: '图表类型',
    addFilters: '添加筛选条件',
    filterField: '筛选字段',
    filterCantBeEmpty: '筛选字段和筛选方式不可为空',
    selectFilterField: '请选择筛选字段',
    filterOperator: '筛选操作符',
    selectFilterOperator: '请选择筛选操作符',
    comparedValue: '判断条件值',
    limit: '查询前 {0} 行',
    fieldExisted: '字段已存在',
    beforeLeaveConfirm: '确定要离开当前页面吗?系统可能不会保存您所做的更改',
    deleteConfirm: '确定要删除图表：{0}？',
    chartNameWarning: '保存失败，请输入图表名称'
  },
  dataSource: {
    sourcePlaceholder: '选择数据源',
    tablePlaceholder: '选择表',
    table: '表',
    fields: '字段',
    addDataSource: '添加数据源',
    baseName: '数据源名称',
    host: 'Host',
    port: '端口',
    user: '用户名',
    password: '密码',
    dataBase: '数据库',
    createdAt: '创建时间',
    manageTables: '管理表',
    tableName: '表名称',
    linked: '是否连接',
    deleteConfirm: '删除后不可撤销，确定要移除该数据源?'
  },
  guide: {
    dataPanel: '数据面板',
    dataPanelDesc: '这里是数据源以及数据源的字段列表，你可以在这里切换数据源、将字段拖拽到数据查询面板进行查询',
    formPanel: '数据分析面板',
    formPanelDesc: '这里进行数据分析，你可以在这里构建、调整查询维度、指标、过滤条件及排序方式，实现对数据的分析',
    dimensionInput: '维度编辑',
    dimensionInputDesc: '维度是指数据归纳的角度，如时间维度、类别维度等，维度一般不宜超过 2 个',
    fieldInput: '指标编辑',
    fieldInputDesc: '指标是指数据分析的内容，如个数、总和、最大最小值等，字段可以有多个，但是不宜过多以免影响可视化效果',
    vizPanel: '可视化面板',
    vizPanelDesc: '这里是可视化面板，可以在此对数据进行可视化，选择不同的图表类型和样式等等'
  },
  chartType: {
    table: '表格',
    tableDesc: '任意维度和数值',
    number: '翻牌器',
    numberDesc: '1个数值',
    line: '折线图',
    lineDesc: '1 或 2个维度;1或多个数值',
    bar: '柱状图',
    barDesc: '1 或 2个维度;1或多个数值',
    stackBar: '堆积柱状图',
    stackBarDesc: '1 或 2个维度;2或多个数值',
    pie: '饼图',
    pieDesc: '1个维度1个数值;0个维度多个数值',
    horizontalBar: '条形图',
    horizontalBarDesc: '1个维度;1或多个数值',
    map: '地图',
    mapDesc: '1区域个维度;1个数值'
  },
  config: {
    is: '等于',
    greater: '大于',
    less: '小于',
    greaterOrEqual: '大于等于',
    lessorEqual: '小于等于',
    distinct:'去重计数',
    isNot: '不等于',
    between: '区间',
    in: 'In',
    like: '包含',
    sum: '合计',
    avg: '平均',
    max: '最大值',
    min: '最小值',
    count: '计数',
    none: '-'
  }
}
