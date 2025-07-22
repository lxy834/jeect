import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '资产车牌',
    align:"center",
    dataIndex: 'plateNumber'
   },
   {
    title: '工单类型',
    align:"center",
    dataIndex: 'orderType'
   },
   {
    title: '目标用户',
    align:"center",
    dataIndex: 'targetUser'
   },
   {
    title: '市局',
    align:"center",
    dataIndex: 'deptName'
   },
   {
    title: '地区局',
    align:"center",
    dataIndex: 'bureau'
   },
   {
    title: '工单状态',
    align:"center",
    dataIndex: 'orderStatus'
   },
   {
    title: '发电时间',
    align:"center",
    dataIndex: 'beginTime'
   },
   {
    title: '结束时间',
    align:"center",
    dataIndex: 'endTime'
   },
   {
    title: '发电量',
    align:"center",
    dataIndex: 'stat'
   },
   {
    title: '油耗',
    align:"center",
    dataIndex: 'fuel'
   },
   {
    title: '行驶里程',
    align:"center",
    dataIndex: 'mileage'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '资产车牌',
    field: 'plateNumber',
    component: 'Input',
  },
  {
    label: '工单类型',
    field: 'orderType',
    component: 'Input',
  },
  {
    label: '目标用户',
    field: 'targetUser',
    component: 'Input',
  },
  {
    label: '市局',
    field: 'deptName',
    component: 'Input',
  },
  {
    label: '地区局',
    field: 'bureau',
    component: 'Input',
  },
  {
    label: '工单状态',
    field: 'orderStatus',
    component: 'Input',
    dynamicDisabled:true
  },
  {
    label: '发电时间',
    field: 'beginTime',
    component: 'DatePicker',
    componentProps: {
       showTime:true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicDisabled:true
  },
  {
    label: '结束时间',
    field: 'endTime',
    component: 'DatePicker',
    componentProps: {
       showTime:true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicDisabled:true
  },
  {
    label: '发电量',
    field: 'stat',
    component: 'InputNumber',
    dynamicDisabled:true
  },
  {
    label: '油耗',
    field: 'fuel',
    component: 'InputNumber',
    dynamicDisabled:true
  },
  {
    label: '行驶里程',
    field: 'mileage',
    component: 'InputNumber',
    dynamicDisabled:true
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];
//子表单数据
//子表表格配置
export const fdqOrderStepColumns: JVxeColumn[] = [
    {
      title: '节点信息',
      key: 'stepInfo',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '节点时间',
      key: 'stepTime',
      type: JVxeTypes.datetime,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '关联的工单id',
      key: 'orderId',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]


// 高级查询数据
export const superQuerySchema = {
  plateNumber: {title: '资产车牌',order: 0,view: 'text', type: 'string',},
  orderType: {title: '工单类型',order: 1,view: 'text', type: 'string',},
  targetUser: {title: '目标用户',order: 2,view: 'text', type: 'string',},
  deptName: {title: '市局',order: 3,view: 'text', type: 'string',},
  bureau: {title: '地区局',order: 4,view: 'text', type: 'string',},
  orderStatus: {title: '工单状态',order: 5,view: 'text', type: 'string',},
  beginTime: {title: '发电时间',order: 6,view: 'datetime', type: 'string',},
  endTime: {title: '结束时间',order: 7,view: 'datetime', type: 'string',},
  stat: {title: '发电量',order: 8,view: 'number', type: 'number',},
  fuel: {title: '油耗',order: 9,view: 'number', type: 'number',},
  mileage: {title: '行驶里程',order: 10,view: 'number', type: 'number',},
  //子表高级查询
  fdqOrderStep: {
    title: '任务详情',
    view: 'table',
    fields: {
        stepInfo: {title: '节点信息',order: 0,view: 'text', type: 'string',},
        stepTime: {title: '节点时间',order: 1,view: 'datetime', type: 'string',},
        orderId: {title: '关联的工单id',order: 2,view: 'text', type: 'string',},
    }
  },
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
// 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}