import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '服务器名称',
    align:"center",
    dataIndex: 'serverName'
   },
   {
    title: 'IP',
    align:"center",
    dataIndex: 'ip'
   },
   {
    title: '端口',
    align:"center",
    dataIndex: 'port'
   },
   {
    title: '存放内容',
    align:"center",
    dataIndex: 'contextInfo'
   },
   {
    title: '备注',
    align:"center",
    dataIndex: 'remark'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '服务器名称',
    field: 'serverName',
    component: 'Input',
  },
  {
    label: 'IP',
    field: 'ip',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入IP!'},
          ];
     },
  },
  {
    label: '端口',
    field: 'port',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入端口!'},
          ];
     },
  },
  {
    label: '存放内容',
    field: 'contextInfo',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入存放内容!'},
          ];
     },
  },
  {
    label: '备注',
    field: 'remark',
    component: 'Input',
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
  serverName: {title: '服务器名称',order: 0,view: 'text', type: 'string',},
  ip: {title: 'IP',order: 1,view: 'text', type: 'string',},
  port: {title: '端口',order: 2,view: 'text', type: 'string',},
  contextInfo: {title: '存放内容',order: 3,view: 'text', type: 'string',},
  remark: {title: '备注',order: 4,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}