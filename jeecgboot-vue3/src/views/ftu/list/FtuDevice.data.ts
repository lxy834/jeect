import {BasicColumn} from '/@/components/Table';
import {FormSchema} from '/@/components/Table';
import { rules} from '/@/utils/helper/validator';
import { render } from '/@/utils/common/renderUtils';
import {JVxeTypes,JVxeColumn} from '/@/components/jeecg/JVxeTable/types'
import { getWeekMonthQuarterYear } from '/@/utils';
//列表数据
export const columns: BasicColumn[] = [
   {
    title: 'CPU状态',
    align:"center",
    dataIndex: 'cpuStatus_dictText'
   },
   {
    title: '电源状态',
    align:"center",
    dataIndex: 'psStatus_dictText'
   },
   {
    title: '点位名称',
    align:"center",
    dataIndex: 'deviceName'
   },
   {
    title: '线路名称',
    align:"center",
    dataIndex: 'insLineName'
   },
   {
    title: '开关状态',
    align:"center",
    dataIndex: 'switchStatus_dictText'
   },
   {
    title: '故障指示',
    align:"center",
    dataIndex: 'fault'
   },
   {
    title: '对时时间',
    align:"center",
    dataIndex: 'timingTime'
   },
   {
    title: 'IP地址',
    align:"center",
    dataIndex: 'ip'
   },
   {
    title: '责任区',
    align:"center",
    dataIndex: 'areaOfRespone'
   },
   {
    title: '厂家',
    align:"center",
    dataIndex: 'factory'
   },
   {
    title: '投运时间',
    align:"center",
    dataIndex: 'commTime'
   },
   {
    title: '型号',
    align:"center",
    dataIndex: 'model'
   },
   {
    title: '变电站',
    align:"center",
    dataIndex: 'station'
   },
];
//查询数据
export const searchFormSchema: FormSchema[] = [
	{
      label: "CPU状态",
      field: "cpuStatus",
      component: 'JSelectMultiple',
      componentProps:{
          dictCode:"cpu_status"
      },
      //colProps: {span: 6},
 	},
	{
      label: "电源状态",
      field: "psStatus",
      component: 'JSelectMultiple',
      componentProps:{
          dictCode:"ps_status"
      },
      //colProps: {span: 6},
 	},
  {
    label: "点位名称",
    field: "deviceName",
    component: 'JInput',
  },
  {
    label: "线路名称",
    field: "insLineName",
    component: 'JInput',
  },
	{
      label: "开关状态",
      field: "switchStatus",
      component: 'JSelectMultiple',
      componentProps:{
          dictCode:"switch_status"
      },
      //colProps: {span: 6},
 	},
  {
    label: "故障指示",
    field: "fault",
    component: 'JInput',
  },
	{
      label: "IP地址",
      field: "ip",
      component: 'Input',
      //colProps: {span: 6},
 	},
  {
    label: "责任区",
    field: "areaOfRespone",
    component: 'JInput',
  },
  {
    label: "厂家",
    field: "factory",
    component: 'JInput',
  },
     {
      label: "投运时间",
      field: "commTime",
      component: 'RangePicker',
      componentProps: {
          valueType: 'Date',
          showTime:true
      },
      //colProps: {span: 6},
	},
  {
    label: "型号",
    field: "model",
    component: 'JInput',
  },
];
//表单数据
export const formSchema: FormSchema[] = [
  {
    label: 'CPU状态',
    field: 'cpuStatus',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"cpu_status"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入CPU状态!'},
          ];
     },
  },
  {
    label: '电源状态',
    field: 'psStatus',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"ps_status"
     },
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入电源状态!'},
          ];
     },
  },
  {
    label: '点位名称',
    field: 'deviceName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入点位名称!'},
          ];
     },
  },
  {
    label: '线路名称',
    field: 'insLineName',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入线路名称!'},
          ];
     },
  },
  {
    label: '开关状态',
    field: 'switchStatus',
    component: 'JDictSelectTag',
    componentProps:{
        dictCode:"switch_status"
     },
  },
  {
    label: '故障指示',
    field: 'fault',
    component: 'Input',
  },
  {
    label: '对时时间',
    field: 'timingTime',
    component: 'DatePicker',
    componentProps: {
       showTime:true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: 'IP地址',
    field: 'ip',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入IP地址!'},
          ];
     },
  },
  {
    label: '责任区',
    field: 'areaOfRespone',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入责任区!'},
          ];
     },
  },
  {
    label: '厂家',
    field: 'factory',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入厂家!'},
          ];
     },
  },
  {
    label: '投运时间',
    field: 'commTime',
    component: 'DatePicker',
    componentProps: {
       showTime:true,
       valueFormat: 'YYYY-MM-DD HH:mm:ss'
     },
  },
  {
    label: '型号',
    field: 'model',
    component: 'Input',
    dynamicRules: ({model,schema}) => {
          return [
                 { required: true, message: '请输入型号!'},
          ];
     },
  },
  {
    label: '变电站',
    field: 'station',
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
//子表单数据
export const ftuF411DeviceFormSchema: FormSchema[] = [
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
	{
	  label: '',
	  field: 'id',
	  component: 'Input',
	  show: false
	},
];
//子表表格配置
export const ftuElectlVolumeColumns: JVxeColumn[] = [
    {
      title: '有功',
      key: 'activePower',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '无功',
      key: 'reactivePower',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '功率因数',
      key: 'factor',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '电压',
      key: 'voltage',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
    {
      title: '电流',
      key: 'ftuCurrent',
      type: JVxeTypes.inputNumber,
      width:"200px",
      placeholder: '请输入${title}',
      defaultValue:'',
        validateRules: [
          { required: true, message: '${title}不能为空' },
        ],
    },
  ]
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
  cpuStatus: {title: 'CPU状态',order: 0,view: 'number', type: 'number',dictCode: 'cpu_status',},
  psStatus: {title: '电源状态',order: 1,view: 'number', type: 'number',dictCode: 'ps_status',},
  deviceName: {title: '点位名称',order: 2,view: 'text', type: 'string',},
  insLineName: {title: '线路名称',order: 3,view: 'text', type: 'string',},
  switchStatus: {title: '开关状态',order: 4,view: 'number', type: 'number',dictCode: 'switch_status',},
  fault: {title: '故障指示',order: 5,view: 'text', type: 'string',},
  timingTime: {title: '对时时间',order: 6,view: 'datetime', type: 'string',},
  ip: {title: 'IP地址',order: 7,view: 'text', type: 'string',},
  areaOfRespone: {title: '责任区',order: 8,view: 'text', type: 'string',},
  factory: {title: '厂家',order: 9,view: 'text', type: 'string',},
  commTime: {title: '投运时间',order: 10,view: 'datetime', type: 'string',},
  model: {title: '型号',order: 11,view: 'text', type: 'string',},
  station: {title: '变电站',order: 12,view: 'text', type: 'string',},
  //子表高级查询
  ftuF411Device: {
    title: '通信终端',
    view: 'table',
    fields: {
        communicationMode: {title: '通信模式',order: 0,view: 'number', type: 'number',},
        insLocation: {title: '安装位置',order: 1,view: 'text', type: 'string',},
        onlineStatus: {title: '在线状态',order: 2,view: 'number', type: 'number',dictCode: 'online_status',},
        bdCard: {title: '北斗卡号',order: 3,view: 'number', type: 'number',},
        apnCard: {title: '5G卡号',order: 4,view: 'text', type: 'string',},
        deviceName: {title: '设备名称',order: 5,view: 'text', type: 'string',},
    }
  },
  ftuElectlVolume: {
    title: '电气量信息',
    view: 'table',
    fields: {
        activePower: {title: '有功',order: 0,view: 'number', type: 'number',},
        reactivePower: {title: '无功',order: 1,view: 'number', type: 'number',},
        factor: {title: '功率因数',order: 2,view: 'number', type: 'number',},
        voltage: {title: '电压',order: 3,view: 'number', type: 'number',},
        ftuCurrent: {title: '电流',order: 4,view: 'number', type: 'number',},
    }
  },
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