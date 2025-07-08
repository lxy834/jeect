import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '告警信息',
    align:"center",
    dataIndex: 'warnInfo'
   },
   {
    title: '告警类别',
    align:"center",
    dataIndex: 'deviceType'
   },
   {
    title: '设备id',
    align:"center",
    dataIndex: 'deviceId'
   },
   {
    title: '告警时间',
    align:"center",
    dataIndex: 'warnTime'
   },
   {
    title: '设备名称',
    align:"center",
    dataIndex: 'deviceName'
   },
   {
    title: '点位',
    align:"center",
    dataIndex: 'insLocation'
   },
   {
    title: '线路位置',
    align:"center",
    dataIndex: 'lineLocation'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
	{
      label: "设备名称",
      field: 'deviceName',
      component: 'Input',
      //colProps: {span: 6},
 	},
	{
      label: "点位",
      field: 'insLocation',
      component: 'Input',
      //colProps: {span: 6},
 	},
	{
      label: "线路位置",
      field: 'lineLocation',
      component: 'Input',
      //colProps: {span: 6},
 	},
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '告警信息',
    field: 'warnInfo',
    component: 'Input',
  },
  {
    label: '告警类别',
    field: 'deviceType',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"device_type"
     },
  },
  {
    label: '设备id',
    field: 'deviceId',
    component: 'Input',
  },
  {
    label: '告警时间',
    field: 'warnTime',
    component: 'DatePicker',
    componentProps: {
       showTime: true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '设备名称',
    field: 'deviceName',
    component: 'Input',
  },
  {
    label: '点位',
    field: 'insLocation',
    component: 'Input',
  },
  {
    label: '线路位置',
    field: 'lineLocation',
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
  warnInfo: {title: '告警信息',order: 0,view: 'text', type: 'string',},
  deviceType: {title: '告警类别',order: 1,view: 'list', type: 'string',dictCode: 'device_type',},
  deviceId: {title: '设备id',order: 2,view: 'text', type: 'string',},
  warnTime: {title: '告警时间',order: 3,view: 'datetime', type: 'string',},
  deviceName: {title: '设备名称',order: 4,view: 'text', type: 'string',},
  insLocation: {title: '点位',order: 5,view: 'text', type: 'string',},
  lineLocation: {title: '线路位置',order: 6,view: 'text', type: 'string',},
};

/**
* 流程表单调用这个方法获取formSchema
* @param param
*/
export function getBpmFormSchema(_formData): FormSchema[]{
  // 默认和原始表单保持一致 如果流程中配置了权限数据，这里需要单独处理formSchema
  return formSchema;
}
