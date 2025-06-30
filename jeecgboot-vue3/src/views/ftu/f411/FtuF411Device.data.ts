import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '通信模式',
    align:"center",
    dataIndex: 'communicationMode_dictText'
   },
   {
    title: '安装位置',
    align:"center",
    dataIndex: 'insLocation'
   },
   {
    title: '在线状态',
    align:"center",
    dataIndex: 'onlineStatus_dictText'
   },
   {
    title: '北斗卡号',
    align:"center",
    dataIndex: 'bdCard'
   },
   {
    title: '5G卡号',
    align:"center",
    dataIndex: 'apnCard'
   },
   {
    title: '设备名称',
    align:"center",
    dataIndex: 'deviceName'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
  {
    label: "安装位置",
    field: "insLocation",
    component: 'JInput',
  },
	{
      label: "在线状态",
      field: "onlineStatus",
      component: 'JSelectMultiple',
      componentProps:{
          dictCode:"online_status"
      },
      //colProps: {span: 6},
 	},
	{
      label: "北斗卡号",
      field: "bdCard",
      component: 'InputNumber',
      //colProps: {span: 6},
 	},
	{
      label: "5G卡号",
      field: "apnCard",
      component: 'Input',
      //colProps: {span: 6},
 	},
  {
    label: "设备名称",
    field: "deviceName",
    component: 'JInput',
  },
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '通信模式',
    field: 'communicationMode',
    component: 'InputNumber',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入通信模式!'},
          ];
     },
  },
  {
    label: '安装位置',
    field: 'insLocation',
    component: 'Input',
  },
  {
    label: '在线状态',
    field: 'onlineStatus',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"online_status"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入在线状态!'},
          ];
     },
  },
  {
    label: '北斗卡号',
    field: 'bdCard',
    component: 'InputNumber',
  },
  {
    label: '5G卡号',
    field: 'apnCard',
    component: 'Input',
  },
  {
    label: '设备名称',
    field: 'deviceName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入设备名称!'},
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
//子表单数据
//子表表格配置
export const ftuWarnInfoColumns: JVxeColumn[] = [
    {
      title: '告警信息',
      key: 'warnInfo',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '告警类别',
      key: 'deviceType',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '设备id',
      key: 'deviceId',
      type: JVxeTypes.input,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '告警时间',
      key: 'warnTime',
      type: JVxeTypes.datetime,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]


// 高级查询数据
export const superQuerySchema = {
  communicationMode: {title: '通信模式',order: 0,view: 'number', type: 'number',},
  insLocation: {title: '安装位置',order: 1,view: 'text', type: 'string',},
  onlineStatus: {title: '在线状态',order: 2,view: 'number', type: 'number',dictCode: 'online_status',},
  bdCard: {title: '北斗卡号',order: 3,view: 'number', type: 'number',},
  apnCard: {title: '5G卡号',order: 4,view: 'text', type: 'string',},
  deviceName: {title: '设备名称',order: 5,view: 'text', type: 'string',},
  //子表高级查询
  ftuWarnInfo: {
    title: '告警信息',
    view: 'table',
    fields: {
        warnInfo: {title: '告警信息',order: 0,view: 'text', type: 'string',},
        deviceType: {title: '告警类别',order: 1,view: 'text', type: 'string',},
        deviceId: {title: '设备id',order: 2,view: 'text', type: 'string',},
        warnTime: {title: '告警时间',order: 3,view: 'datetime', type: 'string',},
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
