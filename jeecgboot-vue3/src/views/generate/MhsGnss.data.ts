import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '设备编号',
    align:"center",
    dataIndex: 'deviceCode'
   },
   {
    title: '东西',
    align:"center",
    dataIndex: 'east'
   },
   {
    title: '南北',
    align:"center",
    dataIndex: 'north'
   },
   {
    title: '沉降',
    align:"center",
    dataIndex: 'offset'
   },
   {
    title: 'cdm解算时间',
    align:"center",
    dataIndex: 'cmdTime'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
	{
      label: "设备编号",
      field: 'deviceCode',
      component: 'Input',
      //colProps: {span: 6},
 	},
     {
      label: "cdm解算时间",
      field: "cmdTime",
      component: 'RangePicker',
      componentProps: {
          valueType: 'Date',
          showTime:true
      },
      //colProps: {span: 6},
	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '设备编号',
    field: 'deviceCode',
    component: 'Input',
  },
  {
    label: '东西',
    field: 'east',
    component: 'InputNumber',
  },
  {
    label: '南北',
    field: 'north',
    component: 'InputNumber',
  },
  {
    label: '沉降',
    field: 'offset',
    component: 'InputNumber',
  },
  {
    label: 'cdm解算时间',
    field: 'cmdTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
	// TODO 主键隐藏字段，目前写死为ID
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];

// 高级查询数据
export const superQuerySchema = {
  deviceCode: {title: '设备编号',order: 0,view: 'text', type: 'string',},
  east: {title: '东西',order: 1,view: 'number', type: 'number',},
  north: {title: '南北',order: 2,view: 'number', type: 'number',},
  offset: {title: '沉降',order: 3,view: 'number', type: 'number',},
  cmdTime: {title: 'cdm解算时间',order: 4,view: 'datetime', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}