import i18n from '@/i18n'

export const sqlFunc = {
  sum: i18n.t('config.sum'),
  avg: i18n.t('config.avg'),
  max: i18n.t('config.max'),
  min: i18n.t('config.min'),
  count: i18n.t('config.count'),
  distinct:i18n.t('config.distinct'),
  none: i18n.t('config.none')
}

export const filterOperator = [
  { operator: '=', name: 'config.is', paramNum: 1 },
  { operator: '>', name: 'config.greater', paramNum: 1 },
  { operator: '<', name: 'config.less', paramNum: 1 },
  { operator: '>=', name: 'config.greaterOrEqual', paramNum: 1 },
  { operator: '<=', name: 'config.lessorEqual', paramNum: 1 },
  { operator: '!=', name: 'config.isNot', paramNum: 1 },
  { operator: 'BETWEEN', name: 'config.between', paramNum: 2 },
  { operator: 'IN', name: 'config.in', paramNum: -1 },
  { operator: 'LIKE', name: 'config.like', paramNum: 1 }
]

export function getFilterOperator() {
  return filterOperator.map(item => {
    const i18nItem = { ...item }
    i18nItem.name = i18n.t(item.name)
    return i18nItem
  })
}

export const dataType = [
  { name: 'tinyint', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none','distinct'] },
  { name: 'smallint', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] },
  { name: 'mediumint', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] },
  { name: 'int', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none','distinct'] },
  { name: 'bigint', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] },
  { name: 'float', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] },
  { name: 'double', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] },
  { name: 'real', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] },
  { name: 'decimal', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] },
  { name: 'timestamp', needQuotation: false, availableFunc: ['count', 'none','distinct'] },
  { name: 'date', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'time', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'datetime', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'year', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'char', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'varchar', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'varchar2', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'tinytext', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'text', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'mediumtext', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'longtext', needQuotation: true, availableFunc: ['count', 'none','distinct'] },
  { name: 'integer', needQuotation: false, availableFunc: ['sum', 'avg', 'max', 'min', 'count', 'none'] }
]
