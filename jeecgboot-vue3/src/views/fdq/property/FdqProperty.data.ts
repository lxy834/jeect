import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: '部门',
    align:"center",
    dataIndex: 'deptName'
   },
   {
    title: '车牌号',
    align:"center",
    dataIndex: 'plateNumber'
   },
   {
    title: '型号',
    align:"center",
    dataIndex: 'brand'
   },
   {
    title: '资产主人',
    align:"center",
    dataIndex: 'master'
   },
   {
    title: '联系方式',
    align:"center",
    dataIndex: 'phone'
   },
   {
    title: '地区局',
    align:"center",
    dataIndex: 'bureau'
   },
   {
    title: '控制器型号',
    align:"center",
    dataIndex: 'ctrlModel'
   },
   {
    title: '修正因数',
    align:"center",
    dataIndex: 'fixesFactor'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: '部门',
    field: 'deptName',
    component: 'Input',
  },
  {
    label: '车牌号',
    field: 'plateNumber',
    component: 'Input',
  },
  {
    label: '型号',
    field: 'brand',
    component: 'Input',
  },
  {
    label: '资产主人',
    field: 'master',
    component: 'Input',
  },
  {
    label: '联系方式',
    field: 'phone',
    component: 'Input',
  },
  {
    label: '地区局',
    field: 'bureau',
    component: 'Input',
  },
  {
    label: '控制器型号',
    field: 'ctrlModel',
    component: 'Input',
  },
  {
    label: '修正因数',
    field: 'fixesFactor',
    component: 'InputNumber',
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
export const fdqBasicFormSchema: FormSchema[] = [
  {
    label: '设备编号',
    field: 'assetNumber',
    component: 'Input',
  },
  // {
  //   label: '在线',
  //   field: 'online',
  //   component: 'JDictSelectTag',
  //   componentProps:{
  //       dictCode:""
  //    },
  // },
  // {
  //   label: '关联资产',
  //   field: 'plateNumber',
  //   component: 'Input',
  // },
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];
//子表表格配置
export const fdqControllerColumns: JVxeColumn[] = [
    {
      title: 'A项电压',
      key: 'voltageA',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: 'B项电压',
      key: 'voltageB',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: 'C项电压',
      key: 'voltageC',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '线压A',
      key: 'lineVoltageA',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '线压B',
      key: 'lineVoltageB',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '线压C',
      key: 'lineVoltageC',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '电流A',
      key: 'currentA',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '电流B',
      key: 'currentB',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '电流C',
      key: 'currentC',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '转速',
      key: 'rpm',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '发电频率',
      key: 'freqHz',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '有功A',
      key: 'activeA',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '有功B',
      key: 'activeB',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '有功C',
      key: 'activeC',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '功率因素A',
      key: 'factorA',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '功率因素B',
      key: 'factorB',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '功率因素C',
      key: 'factorC',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '电池电压',
      key: 'battery',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '水温',
      key: 'waterTemperature',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '发动机状态',
      key: 'status',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '运行小时',
      key: 'runningHours',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '下次维护剩余时间',
      key: 'nextRepair',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '启动次数',
      key: 'numberOfLaunches',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '总发电量',
      key: 'kwh',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
    {
      title: '无功',
      key: 'kwarh',
      type: JVxeTypes.inputNumber,
      disabled:true,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
    },
  ]


// 高级查询数据
export const superQuerySchema = {
  deptName: {title: '部门',order: 0,view: 'text', type: 'string',},
  plateNumber: {title: '车牌号',order: 1,view: 'text', type: 'string',},
  brand: {title: '型号',order: 2,view: 'text', type: 'string',},
  master: {title: '资产主人',order: 3,view: 'text', type: 'string',},
  phone: {title: '联系方式',order: 4,view: 'text', type: 'string',},
  bureau: {title: '地区局',order: 5,view: 'text', type: 'string',},
  ctrlModel: {title: '控制器型号',order: 6,view: 'text', type: 'string',},
  fixesFactor: {title: '修正因数',order: 7,view: 'number', type: 'number',},
  //子表高级查询
  fdqBasic: {
    title: '智慧终端',
    view: 'table',
    fields: {
        assetNumber: {title: '设备编号',order: 0,view: 'text', type: 'string',},
        online: {title: '在线',order: 1,view: 'number', type: 'number',dictCode: '',},
        plateNumber: {title: '关联资产',order: 2,view: 'text', type: 'string',},
    }
  },
  fdqController: {
    title: '历史数据',
    view: 'table',
    fields: {
        voltageA: {title: 'A项电压',order: 0,view: 'number', type: 'number',},
        voltageB: {title: 'B项电压',order: 1,view: 'number', type: 'number',},
        voltageC: {title: 'C项电压',order: 2,view: 'number', type: 'number',},
        lineVoltageA: {title: '线压A',order: 3,view: 'number', type: 'number',},
        lineVoltageB: {title: '线压B',order: 4,view: 'number', type: 'number',},
        lineVoltageC: {title: '线压C',order: 5,view: 'number', type: 'number',},
        currentA: {title: '电流A',order: 6,view: 'number', type: 'number',},
        currentB: {title: '电流B',order: 7,view: 'number', type: 'number',},
        currentC: {title: '电流C',order: 8,view: 'number', type: 'number',},
        rpm: {title: '转速',order: 9,view: 'number', type: 'number',},
        freqHz: {title: '发电频率',order: 10,view: 'number', type: 'number',},
        activeA: {title: '有功A',order: 11,view: 'number', type: 'number',},
        activeB: {title: '有功B',order: 12,view: 'number', type: 'number',},
        activeC: {title: '有功C',order: 13,view: 'number', type: 'number',},
        factorA: {title: '功率因素A',order: 14,view: 'number', type: 'number',},
        factorB: {title: '功率因素B',order: 15,view: 'number', type: 'number',},
        factorC: {title: '功率因素C',order: 16,view: 'number', type: 'number',},
        battery: {title: '电池电压',order: 17,view: 'number', type: 'number',},
        waterTemperature: {title: '水温',order: 18,view: 'number', type: 'number',},
        status: {title: '发动机状态',order: 19,view: 'number', type: 'number',},
        runningHours: {title: '运行小时',order: 20,view: 'number', type: 'number',},
        nextRepair: {title: '下次维护剩余时间',order: 21,view: 'number', type: 'number',},
        numberOfLaunches: {title: '启动次数',order: 22,view: 'number', type: 'number',},
        kwh: {title: '总发电量',order: 23,view: 'number', type: 'number',},
        kwarh: {title: '无功',order: 24,view: 'number', type: 'number',},
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
