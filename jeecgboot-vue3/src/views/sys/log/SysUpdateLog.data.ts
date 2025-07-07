import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '系统版本号',
    align:"center",
    dataIndex: 'sysVersion'
   },
   {
    title: '更新内容',
    align:"center",
    dataIndex: 'updateContext'
   },
   {
    title: '发布者',
    align:"center",
    dataIndex: 'publisher'
   },
   {
    title: '发布时间',
    align:"center",
    dataIndex: 'publishTime'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '系统版本号',
    field: 'sysVersion',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入系统版本号!'},
          ];
     },
  },
  {
    label: '更新内容',
    field: 'updateContext',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入更新内容!'},
          ];
     },
  },
  {
    label: '发布者',
    field: 'publisher',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入发布者!'},
          ];
     },
  },
  {
    label: '发布时间',
    field: 'publishTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入发布时间!'},
          ];
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
  sysVersion: {title: '系统版本号',order: 0,view: 'text', type: 'string',},
  updateContext: {title: '更新内容',order: 1,view: 'text', type: 'string',},
  publisher: {title: '发布者',order: 2,view: 'text', type: 'string',},
  publishTime: {title: '发布时间',order: 3,view: 'datetime', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}