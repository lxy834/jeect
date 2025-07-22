import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: 'GPS时间',
    align:"center",
    dataIndex: 'gpsTime'
   },
   {
    title: '运行时长',
    align:"center",
    dataIndex: 'runningTime'
   },
   {
    title: '心跳频率',
    align:"center",
    dataIndex: 'heartRate'
   },
   {
    title: '系统温度',
    align:"center",
    dataIndex: 'systemTemp'
   },
   {
    title: '外部电压',
    align:"center",
    dataIndex: 'externalVoltage'
   },
   {
    title: '内部电压',
    align:"center",
    dataIndex: 'internalVoltage'
   },
   {
    title: '信号质量',
    align:"center",
    dataIndex: 'commQuality'
   },
   {
    title: '固件版本',
    align:"center",
    dataIndex: 'appVersion'
   },
   {
    title: '车牌号',
    align:"center",
    dataIndex: 'plateNumber'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: 'GPS时间',
    field: 'gpsTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '运行时长',
    field: 'runningTime',
    component: 'InputNumber',
  },
  {
    label: '心跳频率',
    field: 'heartRate',
    component: 'InputNumber',
  },
  {
    label: '系统温度',
    field: 'systemTemp',
    component: 'InputNumber',
  },
  {
    label: '外部电压',
    field: 'externalVoltage',
    component: 'InputNumber',
  },
  {
    label: '内部电压',
    field: 'internalVoltage',
    component: 'InputNumber',
  },
  {
    label: '信号质量',
    field: 'commQuality',
    component: 'InputNumber',
  },
  {
    label: '固件版本',
    field: 'appVersion',
    component: 'Input',
  },
  {
    label: '车牌号',
    field: 'plateNumber',
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
  gpsTime: {title: 'GPS时间',order: 0,view: 'datetime', type: 'string',},
  runningTime: {title: '运行时长',order: 1,view: 'number', type: 'number',},
  heartRate: {title: '心跳频率',order: 2,view: 'number', type: 'number',},
  systemTemp: {title: '系统温度',order: 3,view: 'number', type: 'number',},
  externalVoltage: {title: '外部电压',order: 4,view: 'number', type: 'number',},
  internalVoltage: {title: '内部电压',order: 5,view: 'number', type: 'number',},
  commQuality: {title: '信号质量',order: 6,view: 'number', type: 'number',},
  appVersion: {title: '固件版本',order: 7,view: 'text', type: 'string',},
  plateNumber: {title: '车牌号',order: 8,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}